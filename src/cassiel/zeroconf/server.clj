(ns cassiel.zeroconf.server
  (:import [javax.jmdns JmDNS ServiceInfo]))

(defprotocol SERVER
  "Open/close a service registration."
  (open [this] "Open registration")
  (close [this] "Close this registration"))

(defn server
  "Service, with info. Can be opened and closed.

Usage:

    (require '(cassiel.zeroconf [server :as s]))
    (def server (s/server :type \"_cubewar._udp.local.\"
                          :name \"Test Cubewar\"
                          :port 8765
                          :text \"A test Cubewar server\"))
    (s/open server)
    (s/close server)"
  [& {:keys [type name port text]}]

  (let [jmDNS (JmDNS/create)
        make-info (fn [] (ServiceInfo/create type name port text))
        INFO (atom nil)]

    (reify SERVER
      (open [this]
        (.registerService jmDNS (reset! INFO (make-info))))

      (close [this]
        (.unregisterService jmDNS @INFO)))))
