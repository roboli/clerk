Clerk
=====

Syntatic sugar over [Secretary](www.github.com/gf3/secretary) router to make it [Om](www.github.com/swannodette/om) ready.

###Usage

Require the clerk library.

```clojure
(ns your-app.core
  (:require [clerk.core :refer-macros [defcom-route defrouter]]))

```
Define your routes.

```clojure
(defcom-route route destruct component options)

```
Basically the `defcom-route` macro takes four parameters, the `route` and `destruct` are the same parameters the `defroute` macro from Secretary expects. The `component` and `options` are the backing component and options map that the `root` function from Om expects.

Define your router.

```clojure
(defrouter name value target)

```
The `defrouter` macro expects the `name` for your router var, the `value` which is (taken from Om's documentation) *"either a tree of associative ClojureScript data structures or an atom wrapping a tree of associative ClojureScript data structures"*. And `target` is just a shortcut to the `:target` value from the `options` map.

Finally, start your application.

```clojure
(clerk.core/start router route)

```
The `start` function expects your router var and a defined route. Which will start your application rendering your component defined in the provided route and start listening to navigation events.
