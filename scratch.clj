(ns user
  (:require (cassiel.zeroconf [client :as cl]
                              [server :as s]))
  (:import [javax.jmdns JmDNS ServiceListener ServiceInfo]))

;; --- Manual tests (vestigial).

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

;; --- Actual package tests.

(ns user
  (:require (cassiel.zeroconf [client :as cl])))

(def a (cl/listen "_monome-osc._udp.local."))
(def a (cl/listen "_ssh._tcp.local."))

(cl/examine a)

(cl/close a)

(def a (cl/listen "_monome-osc._udp.local."
                 :watch (fn [old new]
                          (prn "old keys" (keys old) "new keys" (keys new)))))

(keys {:A 1})

(pprint (cl/examine a))

;; --- Server tests: native.

(def jmDNS (JmDNS/create))

(def info (ServiceInfo/create "_cubewar._udp.local." "Test Cubewar" 8765 "A test Cubewar server"))

(.registerService jmDNS info)

(.unregisterService jmDNS info)

;; --- Test interface.

(def xxx
  (s/server :type "_cubewar._udp.local."
            :name "Test Cubewar"
            :port 8765
            :text "A test Cubewar server"))

(s/open xxx)

(s/close xxx)
