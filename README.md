clojure-zeroconf
================

jmDNS-based zeroconf in Clojure.

For documentation and usage see the [Marginalia documentation][docs].

Artifact information [here][clojars].

Very quick start (client):

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

Server:

```clojure
(ns user
  (:require (cassiel.zeroconf [server :as s])))


(def server (s/server :type "_cubewar._udp.local."
                      :name "Test Cubewar"
                      :port 8765
                      :text "A test Cubewar server"))
(s/open server)
(s/close server)
```

## Releases

### 1.2.0, 2013-10-17

Merge [from Adam Clements](https://github.com/cassiel/clojure-zeroconf/pull/1). Clojure dependency `1.5.1`.

### 1.1.0, 2013-03-08

Added server (register/unregister) component. Bumped Clojure dependency to `1.5.0`.

### 1.0.0, 2012-12-28

Initial client release.


## License

Copyright © 2013 Nick Rothwell, nick@cassiel.eu

Distributed under the Eclipse Public License, the same as Clojure.

[docs]: http://cassiel.github.com/clojure-zeroconf/uberdoc.html
[clojars]: https://clojars.org/eu.cassiel/clojure-zeroconf
