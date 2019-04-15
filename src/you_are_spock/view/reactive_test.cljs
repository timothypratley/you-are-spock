(ns you-are-spock.view.reactive-test
  (:require [cljs.pprint :as pprint]))

(defn reactive-test [label state on-click]
  [:div
   [:hr]
   [:h3 label]
   [:button {:on-click on-click} "TX novelty"]
   [:pre [:code (with-out-str (pprint/pprint state))]]
   [:hr]])
