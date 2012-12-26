(ns user
  (:require (cassiel.clojure-zeroconf [core :as c]))
  (:import [javax.jmdns JmDNS ServiceListener]))

; --- Manual tests (vestigial).

(def jmDNS (JmDNS/create))

(def a (atom nil))

(.addServiceListener
 jmDNS
 "_monome-osc._udp.local."
 (reify ServiceListener
   (serviceAdded [this x]
     (do (prn "serviceAdded" x)
         (reset! a x)))
   (serviceRemoved [this x] (prn "serviceRemoved" x))
   (serviceResolved [this x]
     (do (prn "serviceResolved" x)
         (reset! a x)))))

(.requestServiceInfo jmDNS "_monome-osc._udp.local." (-> @a (.getInfo) (.getName)))

(.getServiceInfo jmDNS "_monome-osc._udp.local." (-> @a (.getInfo) (.getName)))

@a

(-> @a (.getInfo) (.getName))

(-> @a (.getInfo) (.getPort))

(-> @a (.getInfo) (.getNiceTextString))

(-> @a (.getInfo) (.getServer))

(-> @a (.getInfo) (.getKey))

(-> @a (.getInfo) (.hasData))


(.close jmDNS)

(.printServices jmDNS)

; --- Actual package tests.

(def a (c/listen "_monome-osc._udp.local."))
(def a (c/listen "_ssh._tcp.local."))

@(:state a)

((:close a))