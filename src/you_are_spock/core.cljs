(ns you-are-spock.core
  (:require [reagent.core :as reagent]
            [goog.dom :as dom]
            [you-are-spock.controller :as controller]))

(reagent/render-component
  [controller/root controller/app-state]
  (dom/getElement "app"))

(defn on-js-reload [])
