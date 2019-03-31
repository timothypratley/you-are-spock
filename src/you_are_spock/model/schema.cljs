(ns you-are-spock.model.schema)

(def schema
  {:entity/location {:db/valueType :db.type/ref
                     :db/cardinality :db.cardinality/one}

   :character/name {:db/unique :db.unique/identity}
   :character/posture {}
   :character/looking-at {:db/valueType :db.type/ref}
   :character/hand {:db/isComponent true
                    :db/valueType :db.type/ref
                    :db/cardinality :db.cardinality/many}

   :character.hand/name {}
   :character.hand/holding {:db/valueType :db.type/ref
                            :db/cardinality :db.cardinality/one}

   :entity/parent {:db/valueType :db.type/ref
                   :db/cardinality :db.cardinality/one}

   :weapon/type {}
   :weapon/quality {}

   :room/name {}
   :room/description {}

   :portal/name {}
   :portal/closed {}
   :portal/climbable {}
   :portal/links {:db/isComponent true
                  :db/valueType :db.type/ref
                  :db/cardinality :db.cardinality/many}

   :portal.link/direction {}
   :portal.link/from {:db/valueType :db.type/ref
                      :db/cardinality :db.cardinality/one}
   :portal.link/to {:db/valueType :db.type/ref
                    :db/cardinality :db.cardinality/one}})
