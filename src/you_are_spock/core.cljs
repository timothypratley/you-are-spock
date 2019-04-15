(ns you-are-spock.core
  (:require [reagent.core :as reagent]
            [goog.dom :as dom]
            [you-are-spock.controller :as controller]
            [datascript.core :as d]))

(reagent/render-component
  (controller/mount)
  (dom/getElement "app"))

(defn on-js-reload [])
