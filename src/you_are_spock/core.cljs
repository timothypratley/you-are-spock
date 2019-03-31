(ns you-are-spock.core
  (:require [reagent.core :as reagent]
            [goog.dom :as dom]
            [you-are-spock.model.main :as model]
            [you-are-spock.view.main :as view.main]
            [datascript.core :as d]))

;; app-state is a reactive copy of db
(defonce app-state
  (reagent/atom @model/conn))

(d/listen! model/conn ::tx
           (fn [tx-report]
             (reset! app-state (:db-after tx-report))))

(reagent/render-component
  [view.main/main-view app-state]
  (dom/getElement "app"))

(defn on-js-reload [])
