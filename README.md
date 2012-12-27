clojure-zeroconf
================

jmDNS-based zeroconf in Clojure.

For documentation and usage see the [Marginalia documentation][docs].

Very quick start:

```clojure
(ns user
  (:require (cassiel.zeroconf [core :as c])))

(def a (c/listen "_ssh._tcp.local."))

(pprint @(:state a))

{"Arnavutköy" {:server "Arnavutk-y.local.", :port 22},
 "Sultanahmet" {:server "Sultanahmet.local.", :port 22},
 "Topkapı" {:server "Topkap-3.local.", :port 22}}

((:close a))
```

[docs]: https://github.com/downloads/cassiel/clojure-zeroconf/uberdoc.html
