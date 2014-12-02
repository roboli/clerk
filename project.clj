(defproject clerk "0.1.0-SNAPSHOT"
  :description "Syntatic sugar over Secretary router to make it Om ready."
  :url "http://github.com/roboli/clerk"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [om "0.7.3"]
                 [secretary "1.2.1"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src/clj" "src/cljs"]

  :cljsbuild { 
    :builds [{:id "clerk"
              :source-paths ["src/cljs"]
              :compiler {
                :output-to "clerk.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}

             ;; examples
             {:id "greeting"
              :source-paths ["src/cljs" "examples/greeting/src"]
              :compiler {
                         :output-to "examples/greeting/app.js"
                         :output-dir "examples/greeting/out"
                         :source-map "examples/greeting/app.js.map"
                         :optimizations :none}}

             {:id "simple-site"
              :source-paths ["src/cljs" "examples/simple-site/src"]
              :compiler {
                         :output-to "examples/simple-site/app.js"
                         :output-dir "examples/simple-site/out"
                         :source-map "examples/simple-site/app.js.map"
                         :optimizations :none}}]})
