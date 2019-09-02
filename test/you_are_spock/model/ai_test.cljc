(ns you-are-spock.model.ai-test
  (:require [clojure.test :refer [deftest is]]
            [you-are-spock.model.ai :as ai]))

(def frog
  {:character/posture :posture/crouching})

;; TODO: 10 10 100 is misleading, 10% chance after 10% is not 10%
(def frog-decision-tree
  [[10 (fn ribbert [frog]
         [:say "ribbert"])]
   [10 (fn hop [frog]
         [:emote "hops"])]
   [100 (fn wander [frog]
          [:go :east])]])

(deftest frog-act-test
  (with-redefs [rand-int (constantly 0)]
    (is (= [:say "ribbert"]
           (ai/act frog frog-decision-tree))
        "A conditional number is a chance for that branch to occur"))
  (with-redefs [rand-int (constantly 100)]
    (is (= [:go :east]
           (ai/act frog frog-decision-tree))
        "To always take a branch, use 100 as the condition")))

(def socrates
  {:character/posture :posture/sitting
   :character/thinking true})

(def socrates-decision-tree
  [[(fn thinking? [socrates]
      (:character/thinking socrates))
    (fn i-am [socrates]
      [:say "I am"])]
   [100 (fn i-am-not [socrates]
          [:say "I am not"])]])

(deftest socrates-act-test
  (is (= [:say "I am"]
         (ai/act socrates socrates-decision-tree))
      "Conditionals can be functions of the entity")
  (is (= [:say "I am not"]
         (ai/act (dissoc socrates :character/thinking) socrates-decision-tree))
      "Conditionals determine when to use a branch or not based on the entity"))
