(ns you-are-spock.model.main
  (:require [datascript.core :as d]
            [you-are-spock.model.logic :as l]
            [you-are-spock.model.schema :as schema]
            [you-are-spock.model.seed :as seed]
            [you-are-spock.model.queries :as q]))

(defn character [db]
  (d/entity db [:character/name "Spock"]))

(defn actions [conn character]
  (l/actions conn character))

(defn qt [db {:keys [query transform]}]
  (cond-> (d/q query db)
    transform (transform)))

(def conn
  (d/create-conn schema/schema))

(d/transact! conn seed/initial-world)

#_#_#_#_#_
(prn "Postures" (qt @conn (q/enum :enum/postures)))
(prn "Bob's posture" (:character/posture (d/entity @conn [:character/name "Bob"])))
(prn "Bob" (qt @conn (q/character "Bob")))
(prn "Bob's equipment" (qt @conn (q/equipment (q/character "Bob"))))
(prn "Where is Bob?" (d/pull @conn '[*] (:db/id (:entity/location (d/entity @conn [:character/name "Bob"])))))

;; RESULTS
; "Postures" [:posture/standing :posture/crouching :posture/sitting :posture/lying-down]
; "Bob's posture" :posture/standing
; "Bob" {:db/id 11, :character/hand [{:db/id 9} {:db/id 10}], :character/looking-at {:db/id 8}, :character/name "Bob", :character/posture :posture/standing}
; "Bob's equipment" {:db/id 7, :weapon/quality 1, :weapon/type :weapon/sword}
; "Where is Bob?" {:db/id 12, :room/contents [{:db/id 8} {:db/id 11}], :room/description "It stinks down here!", :room/exits [:exit/north :exit/up], :room/name "Sewer"}

; What can Bob do?

;; Hands are components, which means they get deleted if the character is deleted:
#_#_#_#_
(prn "Hands before:" (count (d/q '{:find [(pull ?h [*])]
                                  :where [[?h :character.hand/name _]]}
                                @conn)))
(d/transact! conn [[:db.fn/retractEntity (doto (:db/id (d/entity @conn [:character/name "Bob"]))
                                           (->> (prn "RETRACT")))]])
(prn "Bob should not exit:" (d/entity @conn [:character/name "Bob"]))
(prn "Hands after:" (count (d/q '{:find [(pull ?h [*])]
                                  :where [[?h :character.hand/name _]]}
                                @conn)))
