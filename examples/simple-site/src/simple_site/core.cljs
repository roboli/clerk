(ns simple-site.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [clerk.core :as c :refer-macros [defcom-route defrouter]]))

(enable-console-print!)

(def app-state (atom {:content {:about "Hello, this is about..."
                                :contact "Your data"}
                      :nav {:active :home}}))

(defn nav-menu [cursor owner opts]
  (om/component
   (let [active (get-in cursor [:nav :active])]
     (dom/div nil
              (dom/ul nil
                      (dom/li nil
                              (dom/a #js {:href "#/"
                                          :className (if (= active :home) "active")}
                                     "Home"))
                      (dom/li nil
                              (dom/a #js {:href "#/about"
                                          :className (if (= active :about) "active")}
                                     "About"))
                      (dom/li nil
                              (dom/a #js {:href "#/contact/"
                                          :className (if (= active :contact) "active")}
                                     "Contact")))))))

(defn home [cursor owner opts]
  (reify
    om/IWillMount
    (will-mount [_]
      (om/update! cursor [:nav :active] :home))

    om/IRender
    (render [_]
      (dom/div nil
               (dom/h2 nil "Home")
               (om/build (:menu opts) cursor)
               (dom/p nil "Hello people!")))))

(defn about [cursor owner opts]
  (reify
    om/IWillMount
    (will-mount [_]
      (om/update! cursor [:nav :active] :about))
    
    om/IRender
    (render [_]
      (dom/div nil
               (dom/h2 nil "About")
               (om/build (:menu opts) cursor)
               (dom/p nil (get-in cursor [:content :about]))))))

(defn contact [cursor owner opts]
  (reify
    om/IWillMount
    (will-mount [_]
      (om/update! cursor [:nav :active] :contact))
    
    om/IRenderState
    (render-state [_ state]
      (dom/div nil
               (dom/h2 nil "Contact")
               (om/build (:menu opts) cursor)
               (if-not (= (:name opts) "")
                 (dom/p nil (str "Thank you, " (:name opts) ", will contact you."))
                 (dom/fieldset nil
                               (dom/legend nil (get-in cursor [:content :contact]))
                               (dom/label #js {:htmlFor "name"} "Name:")
                               (dom/input #js {:id "name"
                                               :type "text"
                                               :onChange (fn [e]
                                                           (om/set-state! owner :form-name
                                                                          (.. e -target -value)))})
                               (dom/br nil)
                               (dom/label #js {:htmlFor "comments"} "Comments:")
                               (dom/textarea #js {:id "comments"})
                               (dom/br nil)
                               (dom/button #js {:onClick (fn [_] (set! (.. js/window -location -href)
                                                                       (str "#/contact/" (:form-name state))))}
                                           "Send")))))))

(defcom-route "/" [] home {:opts {:menu nav-menu}})

(defcom-route "/about" [] about {:opts {:menu nav-menu}})

(defcom-route "/contact/*name" [name] contact {:init-state {:form-name nil}
                                               :opts {:menu nav-menu
                                                      :name name}})

(defrouter my-router app-state (. js/document (getElementById "app")))

(c/start my-router "/")
