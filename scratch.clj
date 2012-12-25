(ns user
  (:import [javax.jmdns JmDNS JmmDNS$Factory ServiceListener ServiceTypeListener]))

(def jmmDNS
  (JmmDNS$Factory/getInstance))

(.getNames jmmDNS)

(.getInterfaces jmmDNS)

(vec (.getServiceInfos jmmDNS "_monome-osc._udp." nil))



(.close jmmDNS)




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

(-> @a (.getInfo) (.getName))

(-> @a (.getInfo) (.getPort))

(-> @a (.getInfo) (.getNiceTextString))

(-> @a (.getInfo) (.getServer))

(-> @a (.getInfo) (.getKey))

(-> @a (.getInfo) (.hasData))


(.close jmDNS)


(.printServices jmDNS)


(.list jmDNS "_nfs._tcp.local.")

(.list jmDNS "_monome-osc._udp.local.")


(.addServiceListener
 jmDNS
 "_nfs._tcp.local."
 (reify ServiceListener
   (serviceAdded [this x]
     (do (prn "serviceAdded" x)
         (reset! a x)))
   (serviceRemoved [this x] (prn "serviceRemoved" x))
   (serviceResolved [this x]
     (do (prn "serviceResolved" x)
         (reset! a x)))))

(.requestServiceInfo jmDNS "_nfs._tcp.local." (-> @a (.getInfo) (.getName)))




jmDNS

(.getHostName jmDNS)
(str "interface" (.getInterface jmDNS))
