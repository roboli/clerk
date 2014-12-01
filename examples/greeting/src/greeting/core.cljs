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

(defcom-route "/" [] home {:init-state {:form-name nil}})

(defcom-route "/greet/:name" [name] greet {:opts {:name name}})

(defrouter my-router app-state (. js/document (getElementById "app")))

(c/start my-router "/")
