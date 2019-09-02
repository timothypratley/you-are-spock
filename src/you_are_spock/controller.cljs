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

(defn on-tx [tx-report]
  (reset! app-state (:db-after tx-report)))

(d/listen! model/conn ::tx on-tx)

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

(defn all-character-views [db]
  (mapv
   #(view-character (d/entity db %))
   (all-characters db)))

;; TODO: move this to justice, not used here
(defn test-rdbfn [label relevant?]
  (reagent/with-let [a (reagent/atom (all-character-views @model/conn))
                     on-change (fn [x]
                                 (reset! a x))
                     cleanup (re/rdbfn model/conn all-character-views relevant? on-change)]
    [rt/reactive-test
     label
     @a
     (fn [e]
       (d/transact! model/conn [(randomize-parent @model/conn)]))]
    (finally (cleanup))))

;; maybe instead of ticks, reactions should be immediate(ish), but actions take time.

(defn tick [app-state]
  (prn "TICK!")
  (doseq [c (j/q #_'{:character/name _} '(:character/_name _))]
    ;; do stuff!
    ;;(d/touch c)
    ))

(defn start-ticker [app-state]
  (let [id (js/setInterval (fn a-tick [] (tick app-state)) 1000)]
    (fn stop-ticker []
      (js/clearInterval id))))

(defn root [app-state]
  (reagent/with-let [stop-ticker (start-ticker app-state)]
    [view.main/main-view app-state]
    (finally (stop-ticker))))
