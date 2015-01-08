(ns http-server.client
  (:require [org.httpkit.server :refer [with-channel send! run-server]]
            [org.httpkit.client :as http]))


(defn -main [& args]
  (let [uri (nth args 0)
        n (Integer/parseInt (nth args 1))
        t (Integer/parseInt (nth args 2))
        drag (Integer/parseInt (nth args 3))]
    (prn "Start")
    (doseq [_ (range t)]
      (future
        (doseq [_ (range n)]
          (http/get uri)
          (Thread/sleep drag))))
    (prn "Done")
    @(future (while true))))

