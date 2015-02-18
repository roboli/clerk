Clerk
=====

Syntatic sugar over [Secretary](https://github.com/gf3/secretary) router to make it [Om](https://github.com/omcljs/om) ready.

Note: Please read both libraries' documentation if your not familiar with them before proceeding.

###Installation

Currently only a snapshot is available, include it in your `project.clj` dependencies:

```clojure
[org.clojars.roboli/clerk "0.1.0-SNAPSHOT"]

```

###Usage

#####Require the clerk library:

```clojure
(ns your-app.core
  (:require [clerk.core :refer-macros [defcom-route defrouter]]))

```
#####Define your routes:

```clojure
(defcom-route route destruct component options)

```
Basically the `defcom-route` macro takes four parameters, the `route` and `destruct` are the same parameters the `defroute` macro from Secretary expects. The `component` and `options` are the backing component and options map that the `root` function from Om expects. Any parameters defined within the `destruct` param, can be pass in to your component in the `options` map (See the example below).

#####Define your router:

```clojure
(defrouter name value target)

```
The `defrouter` macro expects the `name` for your router var, the `value` which is (taken from Om's documentation) *"either a tree of associative ClojureScript data structures or an atom wrapping a tree of associative ClojureScript data structures"*. And `target` is just a shortcut to the `:target` value from the `options` map.

#####Finally, start your application:

```clojure
(clerk.core/start router route)

```
The `start` function expects your router var and a defined route. Which will start your application rendering your component defined in the provided route and start listening to navigation events.

###Example

```clojure
(ns greeting.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [clerk.core :as c :refer-macros [defcom-route defrouter]]))

(enable-console-print!)

(def app-state (atom {:text "Enter name "
                      :greet "Hi there "}))

(defn home [cursor owner opts]
  (reify
    om/IRenderState
    (render-state [_ state]
      (dom/div nil
               (dom/p nil (:text cursor))
               (dom/label #js {:htmlFor "name"} "Name:")
               (dom/input #js {:id "name"
                               :type "text"
                               :onChange (fn [e]
                                           (om/set-state! owner :form-name
                                                          (.. e -target -value)))})
               (dom/button #js {:onClick (fn [_] (set! (.. js/window -location -href)
                                                       (str "#/greet/" (:form-name state))))}
                           "Send")))))

(defn greet [cursor owner opts]
  (om/component
   (dom/div nil
            (dom/p nil (str (:greet cursor) (:name opts))))))

;; Add routes as in Secretary
(defcom-route "/" [] home {:init-state {:form-name nil}})
(defcom-route "/greet/:name" [name] greet {:opts {:name name}})

;; Define the router
(defrouter my-router app-state (. js/document (getElementById "app")))

;; Start the application rendering the home component
(c/start my-router "/")

```
### Warning

This library hasn't been used in production, so its subject to change.

### License

Distributed under the Eclipse Public License, the same as Clojure.
