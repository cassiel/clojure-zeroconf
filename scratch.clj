(ns user
  (:require (cassiel.zeroconf [core :as c]))
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

;; (require '(cassiel.zeroconf [core :as c]))

(def a (c/listen "_monome-osc._udp.local."))
(def a (c/listen "_ssh._tcp.local."))

@(:state a)

((:close a))

(def a (c/listen "_monome-osc._udp.local."
                 :watch (fn [ref a old new]
                          (prn "service" ref "new keys" (keys new)))))

(keys {:A 1})

(pprint @(:state a))


(doseq
    [[a b] {:A 1 :B 2}]
  (prn a " -> " b)
  )
