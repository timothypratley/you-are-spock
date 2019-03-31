(defproject you-are-spock "0.1.0-SNAPSHOT"
  :description "Spock encounters many puzzling situations which he must overcome with his only weapon: Logic."
  :url "http://github.com/timothypratley/you-are-spock"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :min-lein-version "2.7.1"

  :dependencies [[datascript "0.18.2"]
                 [meander/gamma "0.0.5"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/core.async  "0.4.490"]
                 [org.clojure/core.logic "0.8.11"]
                 [reagent "0.8.1"]]

  :plugins [[lein-figwheel "0.5.18"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]
                :figwheel {:on-jsload "you-are-spock.core/on-js-reload"
                           :open-urls ["http://localhost:3449/index.html"]}

                :compiler {:main you-are-spock.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/you_are_spock.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :preloads [devtools.preload]}}
               {:id "min"
                :source-paths ["src"]
                :compiler {:output-to "resources/public/js/compiled/you_are_spock.js"
                           :main you-are-spock.core
                           :optimizations :advanced
                           :pretty-print false}}]}

  :figwheel {:css-dirs ["resources/public/css"]}


  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.10"]
                                  [figwheel-sidecar "0.5.18"]
                                  [cider/piggieback "0.4.0"]]
                   :source-paths ["src" "dev"]
                   ;; :plugins [[cider/cider-nrepl "0.12.0"]]
                   :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
                   :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                                     :target-path]}})
