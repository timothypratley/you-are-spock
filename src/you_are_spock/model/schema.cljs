(ns you-are-spock.model.schema)

(def schema
  {:entity/location {:db/valueType :db.type/ref
                     :db/cardinality :db.cardinality/one}

   :character/name {:db/unique :db.unique/identity}
   :character/hand {:db/isComponent true
                    :db/valueType :db.type/ref
                    :db/cardinality :db.cardinality/many}
   :character/posture {}
   :character/looking-at {:db/valueType :db.type/ref}

   :character.hand/name {}
   :character.hand/holding {:db/valueType :db.type/ref
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
   #_#_:portal.link/portal {:db/valueType :db.type/ref
                        :db/cardinality :db.cardinality/one}
   :portal.link/direction {}
   :portal.link/from {:db/valueType :db.type/ref
                      :db/cardinality :db.cardinality/one}
   :portal.link/to {:db/valueType :db.type/ref
                    :db/cardinality :db.cardinality/one}})
