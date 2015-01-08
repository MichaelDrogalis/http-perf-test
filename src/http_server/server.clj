(ns http-server.server
  (:require [org.httpkit.server :refer [with-channel send! run-server]]
            [org.httpkit.client :as http]))

(def counter (ref 0))

(defn async-handler [ring-request]
  (with-channel ring-request channel
    (do (dosync (commute counter inc))
        (send! channel {:status 200
                        :headers {"Content-Type" "text/plain"}
                        :thread 4
                        :body "Response"}))))

(defn -main [& args]
  (future
    (loop []
      (Thread/sleep 30000)
      (println (format "Served %s requests in 30 seconds" @counter))
      (dosync (alter counter (constantly 0)))
      (recur)))
  
  (run-server async-handler {:port 8081}))

