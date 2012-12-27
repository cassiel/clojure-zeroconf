(ns cassiel.zeroconf.core
  (:import [javax.jmdns JmDNS ServiceListener]))

(defn request-info
  "Issue a request to get the (rest of the) service info.
   We could just do a `(.getServiceInfo)` but this is slightly
  less antisocial (it won't block)."
  ^{:private true}
  [jmDNS service-name info]
  (.requestServiceInfo jmDNS service-name (.getName info)))

(defn add-info
  "Add information for a service to this state."
  ^{:private true}
  [state info]
  (let [name (.getName info)
        server (.getServer info)
        port (.getPort info)]
    (swap! state assoc name {:server server :port port})))

(defn remove-info
  "Remove information for a service from this state."
  ^{:private true}
  [state info]
  (swap! state dissoc (.getName info)))

;; Create a listener for a service name (including the `.local.` suffix).
;; Returns a record with the state (an atom) and a close function
;; for closing down the listener:
;;
;;     (require '(cassiel.zeroconf [core :as c]))
;;     (def listener (c/listen <service-name>))
;;     (do-something @(:state listener))
;;
;;     ((:close listener))
;;
;; To add a watcher, add a function as a keyword argument:
;;
;;     (c/listen <service-name> :watch <fn>)
;;
;; The watch function just takes two arguments: the old and new values
;; of the service map.

(defn listen
  [service-name & {:keys [watch]}]
  (let [jmDNS (JmDNS/create)
        state (atom {})]
    (when-not
        (nil? watch)
      (add-watch state service-name watch))
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
