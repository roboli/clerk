(ns clerk.core-test
  (:require [cemerick.cljs.test :as t :refer-macros [deftest is]]
            [secretary.core :as sc]
            [clerk.core :as c :refer-macros [defcom-route defrouter]]))

(defn add-route! [r f] [r f])
(defn root [f v o] [f v o])

(deftest wrappers
  (with-redefs [secretary.core/add-route! add-route!
                om.core/root root]
    (is (= ["/" :f] (c/add-route! "/" :f)))
    (is (= [:f :v :o] (c/root :f :v :o)))))

(deftest com-route-macro
  (with-redefs [om.core/root root]
    (defcom-route "/:id" [id] :component {:opts {:id id}})
    (let [f (sc/dispatch! "/7")]
      (is (= [:component :cursor {:opts {:id "7"}
                                  :target :target}] (f :cursor :target))))))

(deftest router-macro
  (defrouter router :cursor :targer)
  (is (= true (satisfies? c/IRouter router))))
