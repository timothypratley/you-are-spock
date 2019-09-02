(ns you-are-spock.model.ai)

;; decision trees are arbitrary conditional branching
;; unlike maps, order is important
;; using the same order means branches are explored systematically
;; but perhaps some randomness would be desirable?
;; different actors have different decision trees
;; can actions only happen on tick? what about reacting to someone entering a room?

(def decision-trees
  '{"default" [[crouching? [[fighting? [[10 stand-up]]]]]
              [standing? [[10 wander]]]
              [tired? [[10 rest]
                       [20 sleep]]]
              [flying? [[10 rest]
                        [10 sit]
                        [10 stand]]]
              [angry? [[20 attack]]]
              [happy? [[30 smile]]]]})

(defn met? [condition character]
  (if (number? condition)
    (<= (rand-int 100) condition)
    (condition character)))

(defn act [character [[condition consequence :as branch] & more]]
  (when branch
    (if (met? condition character)
      (if (vector? consequence)
        (recur character (into consequence more))
        (consequence character))
      (recur character more))))
