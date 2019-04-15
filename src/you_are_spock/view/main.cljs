(ns you-are-spock.view.main
  (:require [clojure.string :as string]
            [you-are-spock.model.logic :as l]
            [you-are-spock.model.main :as model]
            [clojure.string :as str]))



(defn stress-test-view []
  (let []
    )
  )

(defn room-view [app-state]
  ;; TODO: what if I'm not in a room? Inside a bag?
  (let [character (model/character @app-state)
        room (:entity/location character)
        portal-links (:portal.link/_from room)
        actions (model/actions model/conn character)]
    [:dl
     [:dt "You are in " (:room/name room)]
     [:dd (:room/description room)]
     [:dt "Contents:"]
     [:dd (into [:ul]
                (for [e (:entity/_location room)
                      :when (not= e character)]
                  [:li
                   (or
                     (some-> e :character/name (str " [character]"))
                     (some-> e :weapon/name str/capitalize (str  " [weapon]"))
                     e)]))]
     [:dt "Exits:"]
     [:dd (string/join "; "
                       (for [link portal-links]
                         (let [portal (:portal/_links link)]
                           (str
                             (str/capitalize (name (:portal.link/direction link)))
                             " through "
                             (:portal/name portal)
                             (when (:portal/closed portal) " [closed]")
                             (when (:portal/locked portal) " [locked]")))))]
     [:dt "Actions:"]
     [:dd (into [:ul]
                (for [[action description] actions]
                  [:button
                   {:on-click (fn action-clicked [e]
                                (action))}
                   description]))]]))

(defn main-view [app-state]
  [:div
   [:h3 {:style {:text-align "center"}}
    "You are spock"]
   [room-view app-state]])
