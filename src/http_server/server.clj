(ns http-server.server
  (:require [org.httpkit.server :refer [with-channel send! run-server]]
            [org.httpkit.client :as http]))

(def counter-1 (ref 0))
(def counter-2 (ref 0))
(def counter-3 (ref 0))

(defn async-handler-1 [ring-request]
  (with-channel ring-request channel
    (do (dosync (commute counter-1 inc))
        (send! channel {:status 200
                        :headers {"Content-Type" "text/plain"}
                        :thread 2
                        :queue-size 100000
                        :body "Response"}))))

(defn async-handler-2 [ring-request]
  (with-channel ring-request channel
    (do (dosync (commute counter-2 inc))
        (send! channel {:status 200
                        :headers {"Content-Type" "text/plain"}
                        :thread 2
                        :queue-size 100000
                        :body "Response"}))))

(defn async-handler-3 [ring-request]
  (with-channel ring-request channel
    (do (dosync (commute counter-3 inc))
        (send! channel {:status 200
                        :headers {"Content-Type" "text/plain"}
                        :thread 2
                        :queue-size 100000
                        :body "Response"}))))

(defn -main [& args]
  (future
    (loop []
      (Thread/sleep 30000)
      (println (format "Served %s requests in 30 seconds" @counter-1))
      (println (format "Served %s requests in 30 seconds" @counter-2))
      (println (format "Served %s requests in 30 seconds" @counter-3))
      (prn "===")
      (dosync (alter counter-1 (constantly 0)))
      (dosync (alter counter-2 (constantly 0)))
      (dosync (alter counter-3 (constantly 0)))
      (recur)))
  
  (run-server async-handler-1 {:port 8081})
  (run-server async-handler-2 {:port 8082})
  (run-server async-handler-3 {:port 8083}))

