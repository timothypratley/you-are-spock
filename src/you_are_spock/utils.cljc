(ns you-are-spock.utils
  #?(:cljs (:require-macros [you-are-spock.utils]))
  (:require [datascript.core :as d]
            [clojure.pprint :as pprint]
            [meander.match.gamma :as m]
            [meander.strategy.gamma :as s]))

(defmacro T [form & conditions]
  (list* 'and form
         (for [condition conditions]
           (list 'guard condition))))
#_(macroexpand-1 '(T ?x (int? ?x)))

(def datascript-rule-body
  (comp
    (s/bottom-up
      (s/attempt
        (s/match
          (and (?k ?target)
               (guard (keyword? ?k)))
          [?target ?k '?result])))
    (s/bottom-up
      (s/attempt
        (s/match
          (and (?r (?a ?b))
               (guard (not (#{'and 'or} ?r))))
          (let [v (gensym "?bridge_")]
            (list 'and (list ?r v)
                  (list ?a ?b v))))))))
#_(pprint/pprint
    (datascript-rule-body
      '(or (:entity/parent ?x)
           (:entity/parent (ancestor ?x)))))

(defn datascript-rule [relation-name args body]
  [(list* relation-name (conj args '?result))
   (datascript-rule-body body)])

;; Unnesting completely would require new rules...
;; can or/and be made to work with or-join?
(def datascript-rules
  (comp
    ;; Replace any top level and with implied
    (s/bottom-up
      (s/attempt
        (s/match
          [?rule-head (~'and . !clauses ...)]
          (into [?rule-head] !clauses))))
    ;; Replace any top level or with multiple rules
    (s/bottom-up
      (s/attempt
        (s/match
          [[?rule-head (~'or . !clauses ...)]]
          (vec (for [clause !clauses]
                 [?rule-head clause])))))))
#_ (datascript-rules
     '[[(f)
        (or :a :b)]])

#_(defn q [rules]
  '~(datascript-rules
      [(datascript-rule relation-name args body)])
  )

(defmacro defrules [relation-name args body]
  `(defn ~relation-name [db# x#]
     (map #(d/entity db# %)
          (d/q '{:find [[~'?result ...]]
                 :in [~'$ ~'% ~@args]
                 :where [(~relation-name ~@args ~'?result)]}
               db#
               '~(datascript-rules
                   [(datascript-rule relation-name args body)])
               x#))))

#_(macroexpand-1 '(defrules ancestor [?x]
                    (or (:entity/parent ?x)
                        (:entity/parent (ancestor ?x)))))

#_(pprint/pprint
  (macroexpand-1
    '(q foo [x] (:entity/parent ?x))))
#_(def m1 '(relation ancestor [?x] (parent (ancestor ?x))))
#_(println "MMM" m1 \newline)
#_(pprint/pprint (macroexpand-1 m1))
#_(def m2 '(relation ancestor [?x]
                   (or (:entity/parent ?x)
                       (:entity/parent (ancestor ?x)))))
#_(println "MMM2" m2)
#_(pprint/pprint (macroexpand-1 m2))
