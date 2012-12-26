(ns cassiel.clojure-zeroconf.core
  (:import [javax.jmdns JmDNS ServiceListener]))

(defn request-info
  ^{:doc "Issue a request to get the (rest of the) service info.
          We could just do a getServiceInfo() but this is slightly
          less antisocial."
    :private true}
  [jmDNS service-name info]
  (.requestServiceInfo jmDNS service-name (.getName info)))

(defn add-info
  ^{:doc "Add information for a service to this state."}
  [state info]
  (let [name (.getName info)
        server (.getServer info)
        port (.getPort info)]
    (swap! state assoc name {:server server :port port})))

(defn remove-info
  ^{:doc "Remove information for a service from this state."}
  [state info]
  (swap! state dissoc (.getName info)))

(defn listen
  ^{:doc "Create a listener for a service name (including .local. part).
          Returns a record with the state (an atom) and a close function
          for closing down the listener."}
  [service-name]
  (let [jmDNS (JmDNS/create)
        state (atom {})]
    (.addServiceListener
     jmDNS
     service-name
     (reify ServiceListener
       (serviceAdded [this event]
         (let [info (.getInfo event)]
           (if (.hasData info)          ; Unlikely; perhaps never true.
             (add-info state info)
             (request-info jmDNS service-name info))))

       (serviceRemoved [this event]
         (remove-info state (.getInfo event)))

       (serviceResolved [this event]    ; Assume the info. is now complete.
         (add-info state (.getInfo event)))))

    {:state state
     :close #(.close jmDNS)}))
