(ns you-are-spock.model.logic
  (:require [clojure.string :as string]
            [datascript.core :as d]))

(defn exits [character]
  (let [portal-links (:portal.link/_from (:entity/location character))]
    (for [link portal-links]
      {:name (:portal/name (:portal/_links link))
       :direction (:portal.link/direction link)})))

(defn usable-exits [character]
  (and
    (= (:character/posture character) :posture/standing)
    (when-let [room (:entity/location character)]
      (let [portal-links (:portal.link/_from room)]
        (for [portal-link portal-links
              :let [destination (:portal.link/to portal-link)
                    portal (:portal/_links portal-link)]
              :when destination
              :when (not (:portal/closed portal))
              :when (>= (:room/size destination 10)
                        (count (:entity/_location destination)))]
          portal-link)))))

(defn exit? [character direction]
  [;; must not be fighting, must be standing (is fighting a posture?)
   [character :character/posture :posture/standing]
   ;; must be in a room with an exit in that direction to another room
   ['?room :room/contents character]
   ['?room :room/exits direction]
   '[?room :exit/north ?target-room]
   ;; must be able to fit in the target room
   '[?target-room :room/contents ?contents]
   '[(count ?contents) ?occupancy]
   '[?target-room :room/size ?size]
   '[(< ?occupancy ?size)]])

;; can get directions as well as check validity of a specific direction.

(defn attack? [character victim]
  '[;; must be standing
    [?character :character/posture :posture/standing]
    ;; must be in the same room as victim
    [?room :room/contents ?character]
    [?room :room/contents ?victim]])

(defn rest? [character]
  '[;; must not be fighting or resting
    [?character :character/posture ?posture]
    [(not (contains? #{:posture/fighting :posture/resting} ?posture))]])

(defn take? [character object]
  '[;; object must be takeable
    [?object :object/takeable true]
    ;; character must be in the same room as object
    [?room :room/contents ?character]
    [?room :room/contents ?object]
    ;; character must have an empty hand
    [?character :character/hand ?hand]
    [?hand :character.hand/holding nil]
    ;; character must have 2 empty hands if object is large
    ;; objects held by someone/something else result in grappling
    ])

;; rules allow parameterization, but couldn't we get that just with a function anyway?
#_(d/q '{:find [?sku]
       :in [$ % ?char]
       :where (exit? ?character ?direction)})

(defn move [conn character destination]
  (d/transact! conn [[:db/add (:db/id character) :entity/location (:db/id destination)]]))

(defn fn-name [f]
  (-> f
      (.-name)
      (demunge)
      (string/split #"/")
      (last)))

(defn actions [conn character]
  (concat
    (for [portal-link (usable-exits character)]
      [#(move conn character (:portal.link/to portal-link))
       (str "move " (name (:portal.link/direction portal-link)))])))
