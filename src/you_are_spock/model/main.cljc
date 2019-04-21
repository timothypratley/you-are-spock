(ns you-are-spock.model.main
  (:require [datascript.core :as d]
            [justice.core :as j]
            [you-are-spock.model.logic :as l]
            [you-are-spock.model.schema :as schema]
            [you-are-spock.model.seed :as seed]))

(defn character [db]
  (d/entity db [:character/name "Spock"]))

(defn actions [conn character]
  (l/actions conn character))

(def conn
  (d/create-conn schema/schema))

(d/transact! conn seed/initial-world)
(d/transact! conn (seed/create-ancestors))

(j/attach conn)


#_(prn 'Postures (first (j/q '(:enum/postures _))))
;; via query:
#_(first (d/q '{:find [[?result ...]]
                :where [[_ :enum/postures ?result]]}
           @conn))

#_(prn "Bob's posture" (:character/posture (d/entity @conn [:character/name "Bob"])))

;; describing the relation via the schema; intent and operation is clear:
#_(j/q @conn '(:character.hand/holding
               (:character/hand [:character/name "Bob"])))
;; navigating the data directly; you need to write the shape of the relations into the code:
#_(for [hand (:character/hand (d/entity @conn [:character/name "Bob"]))
        :let [item (:character.hand/holding hand)]
        :when item]
    item)
;; querying
#_(map #(d/entity @conn %)
    (d/q {:find '[[?item ...]]
          :where [[[:character/name "Bob"] :character/hand '?h]
                  ['?h :character.hand/holding '?item]]}
      @conn))

#_(prn "Where is Bob?" (d/touch (:entity/location (d/entity @conn [:character/name "Bob"]))))

; What can Bob do?

