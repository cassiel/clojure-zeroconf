(ns user
  (:require (cassiel [zeroconf :as zc]))
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

(ns user
  (:require (cassiel [zeroconf :as zc])))

(def a (zc/listen "_monome-osc._udp.local."))
(def a (zc/listen "_ssh._tcp.local."))

(zc/examine a)

(zc/close a)

(def a (zc/listen "_monome-osc._udp.local."
                 :watch (fn [old new]
                          (prn "old keys" (keys old) "new keys" (keys new)))))

(keys {:A 1})

(pprint (zc/examine a))

(doseq
    [[a b] {:A 1 :B 2}]
  (prn a " -> " b)
  )
