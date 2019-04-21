(ns you-are-spock.controller
  (:require [datascript.core :as d]
            [justice.reactive :as re]
            [reagent.core :as reagent]
            [you-are-spock.model.main :as model]
            [you-are-spock.view.main :as view.main]
            [you-are-spock.view.reactive-test :as rt]
            [justice.core :as j]))

;; app-state is a reactive copy of db
(defonce app-state
  (reagent/atom @model/conn))

(d/listen! model/conn ::tx
           (fn [tx-report]
             (reset! app-state (:db-after tx-report))))

;; hooks style
#_(let [[value update-value] (js/React.useState nil)]
  (js/React.useEffect
    (create-reactive-entity-effect-hook conn q relevant? update-value)))

(defn test-rentity []
  (reagent/with-let
   [ ;; set up some state to track the entity
    a (reagent/atom (into {} (d/entity @model/conn 8)))
    on-change (fn [x]
                (reset! a (into {} x)))
    cleanup (re/rentity model/conn 8 on-change)]

    [rt/reactive-test
     "rentity"
     @a
     (fn tx-novelty [e]
       (d/transact! model/conn [{:db/id 8
                                 :character/rand (rand-int 100)}]))]

    (finally (cleanup))))

(defn all-characters [db]
  (d/q '{:find [[?e ...]]
         :where [[?e :character/name]]}
    db))

(defn randomize-parent [db]
  (let [characters (all-characters db)
        parent (rand-nth characters)
        child (rand-nth characters)]
    {:db/id child
     :entity/parent parent}))

(def view-character (juxt :character/name (comp :character/name :entity/parent)))

(defn test-rdbfn [label relevant?]
  (reagent/with-let
   [a (reagent/atom (mapv
                      #(view-character (d/entity @model/conn %))
                      (all-characters @model/conn)))
    on-change (fn [x]
                (if (fn? x)
                  (swap! a x)
                  (reset! a x)))
    f (fn [db]
        (mapv
          #(view-character (d/entity db %))
          (all-characters db)))
    cleanup (re/rdbfn model/conn f relevant? on-change)]

    [rt/reactive-test
     label
     @a
     (fn [e]
       (d/transact! model/conn [(randomize-parent @model/conn)]))]

    (finally (cleanup))))

(defn tick [app-state]
  (prn "TICK!")
  (doseq [c (j/q '(:character/_name _))]
    (prn (d/touch c))))

(defn start-ticker [app-state]
  (let [id (js/setInterval (fn a-tick [] (tick app-state)) 1000)]
    (fn stop-ticker []
      (js/clearInterval id))))

(defn root [app-state]
  (reagent/with-let [stop-ticker (start-ticker app-state)]
    [:div
     #_[test-rentity]
     #_[test-rdbfn "rdbfn watching attributes" #(re/tx-contains-attribute? :entity/parent %)]
     ;; TODO: alternate strategies for detecting change
     #_[test-rdbfn "rdbfn watching attributes" #(re/tx-relates? #{} #_eids :entity/parent %)]
     [view.main/main-view app-state]]
    (finally
      (stop-ticker))))
