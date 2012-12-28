clojure-zeroconf
================

jmDNS-based zeroconf in Clojure.

For documentation and usage see the [Marginalia documentation][docs].

Artifact information [here][clojars].

Very quick start:

```clojure
(ns user
  (:require (cassiel.zeroconf [client :as cl])))

(def a (cl/listen "_ssh._tcp.local."))

(pprint (cl/examine a))

{"Arnavutköy" {:server "Arnavutk-y.local.", :port 22},
 "Sultanahmet" {:server "Sultanahmet.local.", :port 22},
 "Topkapı" {:server "Topkap-3.local.", :port 22}}

(cl/close a)
```

## License

Copyright © 2012 Nick Rothwell, nick@cassiel.eu

Distributed under the Eclipse Public License, the same as Clojure.

[docs]: http://cassiel.github.com/clojure-zeroconf/uberdoc.html
[clojars]: https://clojars.org/eu.cassiel/clojure-zeroconf
