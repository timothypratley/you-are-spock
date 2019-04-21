(ns you-are-spock.model.seed)

(def initial-world
  [{:enum/postures [:posture/standing :posture/crouching :posture/sitting :posture/lying-down]}
   {:enum/weapons [:weapon/axe :weapon/sword :weapon/spear :weapon/bow]}
   {:enum/exits [:direction/north :direction/south :direction/east :direction/west :direction/up :direction/down]}
   {:enum/difficulty [:difficulty/easy :difficulty/moderate :difficulty/hard]}

   {:db/id -100
    :character/name "A"}
   {:db/id -101
    :character/name "B"
    :entity/parent -100}
   {:db/id -102
    :character/name "C"
    :entity/parent -101}
   {:db/id -103
    :character/name "D"
    :entity/parent -102}

   {:db/id -9
    :room/name "a green field"
    :room/description "The grass is lush here."}

   {:db/id -10
    :room/name "a green field"
    :room/description "The grass has been freshly cut here."}

   {:db/id -11
    :room/name "a green field"
    :room/description "The grass is overgrown here."}

   {:db/id -12
    :room/name "a green field"
    :room/description "The grass is brown here."}

   {:portal/name "an iron gate"
    :portal/closed false
    :portal/climbable :difficulty/easy
    :portal/links [{:portal.link/direction :direction/south
                    :portal.link/from -9
                    :portal.link/to -10}
                   {:portal.link/direction :direction/north
                    :portal.link/from -10
                    :portal.link/to -9}]}

   {:portal/name "an iron gate"
    :portal/closed false
    :portal/climbable :difficulty/easy
    :portal/links [{:portal.link/direction :direction/south
                    :portal.link/from -11
                    :portal.link/to -12}
                   {:portal.link/direction :direction/north
                    :portal.link/from -12
                    :portal.link/to -11}]}

   {:portal/name "an iron gate"
    :portal/closed false
    :portal/climbable :difficulty/easy
    :portal/links [{:portal.link/direction :direction/east
                    :portal.link/from -9
                    :portal.link/to -11}
                   {:portal.link/direction :direction/west
                    :portal.link/from -11
                    :portal.link/to -9}]}

   {:portal/name "an iron gate"
    :portal/closed true
    :portal/climbable :difficulty/easy
    :portal/links [{:portal.link/direction :direction/east
                    :portal.link/from -10
                    :portal.link/to -12}
                   {:portal.link/direction :direction/west
                    :portal.link/from -12
                    :portal.link/to -10}]}

   {:db/id -8
    :character/name "Spock"
    :character/hands [{:character.hand/name "left"}
                      {:character.hand/name "right"}]
    :character/posture :posture/standing
    :entity/location -9}

   {:db/id -3
    :weapon/name "a rusty old sword"
    :weapon/type :weapon/sword
    :weapon/quality 1}

   {:db/id -4
    :weapon/name "a menacing axe"
    :weapon/type :weapon/axe
    :weapon/quality 2
    :entity/location -9}

   {:db/id -7
    :character/name "Bob"
    :character/hand [{:character.hand/name "left"
                      :character.hand/tattoo "Clojure logo"
                      :character.hand/holding -3}
                     {:character.hand/name "right"}]
    :character/posture :posture/standing
    :character/looking-at -4
    :entity/location -9}])

(defn create-ancestors []
  [{:db/id 100
    :character/name "Justice"
    :entity/parent {:character/name "Mother"
                    :entity/parent {:character/name "GrandMother"}}
    :entity/_parent [{:character/name "Good Child"}
                     {:character/name "Bad Child"}]}
   {:db/id 200
    :character/name "Orphan"}
   {:db/id 300
    :character/name "Chaos"
    :entity/parent {:character/name "Loki"}
    :entity/_parent [{:character/name "War"
                      :entity/_parent [{:character/name "Death"}]}]}])
