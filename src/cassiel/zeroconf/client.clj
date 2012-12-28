(ns cassiel.zeroconf.client
  (:import [javax.jmdns JmDNS ServiceListener]))

(defprotocol LISTENER
  "Interface to zeroconf listener, with state and close function."
  (examine [this] "Return the value inside the state atom.")
  (close [this] "Close the listener."))

(defn request-info
  "Issue a request to get the (rest of the) service info.
We could just do a `(.getServiceInfo)` but this is slightly
less antisocial (it won't block)."

  ^{:private true}
  [jmDNS service-name info]
  (.requestServiceInfo jmDNS service-name (.getName info)))

(defn add-info
  "Add information for a service to this state. The key is the server name."
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

(defn listen
  "Create a listener for a service name (including the `.local.` suffix).
Returns an object under protocol `LISTENER` with a function to
examine the state and a `close` function
for closing down the listener.

    (require '(cassiel.zeroconf [client :as cl]))
    (def listener (cl/listen <service-name>))
    (do-something (cl/examine listener))

The state is a map from strings (the published server names)
to maps containing `:server` and `:port`. Note that it can take a while
for all server information to be generated (the request calls are
asychronous).

Closing:

    (cl/close listener)

The `close` call is asynchronous and returns immediately; the
actual close is performed by an agent thread.

To add a watcher, add a function as a keyword argument:

    (cl/listen <service-name> :watch <fn>)

The watch function just takes two arguments: the old and new values
of the service map. On a call to `close`, any watcher is removed
immediately."

  [service-name & {:keys [watch]}]
  (let [async-agent (agent nil)
        jmDNS (JmDNS/create)
        state (atom {})]
    (when-not
        (nil? watch)
      (add-watch state nil
                 (fn [_k _a old new] (watch old new))))

    (send async-agent
          (fn [_]
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

            true))

    (reify LISTENER
      (examine [this] @state)

      (close
        [this]
        (remove-watch state nil)
        (send async-agent
              (fn [x]
                (when x (.close jmDNS))
                false))
        nil))))
