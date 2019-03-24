(ns you-are-spock.model.queries)

(defn enum [attribute]
  {:query
   {:find ['?value]
    :where [['_ attribute '?value]]}
   :transform ffirst})

(defn character [name]
  {:query
   {:find '[(pull ?character [*])]
    :where [['?character :character/name name]]}
   :transform ffirst})

(defn equipment [{:keys [db/id]}]
  {:query
   {:find '[(pull ?item [*])]
    :where [[id :character/hand '?h]
            ['?h :character.hand/holding '?item]]}
   :transform ffirst})
