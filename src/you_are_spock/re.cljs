(ns you-are-spock.re
  (:require [datascript.core :as d]))

(defn- tx-relates-to-entity? [id {:keys [tx-data]}]
  (boolean
    (some
      (fn [[e a v]]
        (or
          (= e id)
          ;; TODO: v should only be checked if a is ref type in schema
          (= v id)))
      tx-data)))

(defn rentity
  "Creates a reactive entity effect.
  Sets up a listener and returns a cleanup function.
  Suitable for use with the React useEffect hook or Reagent with-let.
  conn is a DataScript connection.
  e is either an entity id, an identifier like [:entity/name \"Unique Name\"], or an entity/map containing :db/id.
  on-change will be called with the entity of the new db when any datoms related to the entity get transacted."
  [conn e on-change]
  (let [k (keyword (gensym "listener_"))
        id (if (map? e)
             (:db/id e)
             e)]
    ;; constructor
    (prn 'LISTEN id k)
    (d/listen! conn k
      (fn tx-listener [tx-report]
        (when (tx-relates-to-entity? id tx-report)
          (on-change (d/entity (:db-after tx-report) id)))))
    ;; destructor
    (fn cleanup-rentity []
      (prn 'UNLISTEN id k)
      (d/unlisten! conn k))))

(defn tx-contains-attribute? [attribute {:keys [tx-data]}]
  (boolean
    (some
      (fn [[e a v]]
        (= a attribute))
      tx-data)))

(defn tx-relates? [eids attribute {:keys [tx-data]}]
  (boolean
    (some
      (fn [[e a v]]
        (and (contains? eids e))
        (= a attribute))
      tx-data)))

(defn rdbfn
  "A reactive db function effect.
  Sets up a listener and returns a cleanup function.
  Suitable for use with the React useEffect hook or Reagent with-let.
  conn is a DataScript connection.
  f is any function that should be run in response to the relevant? function.
  f should accept a db as it's only argument and return anything.
  relevant? is a function that will receive a tx-report (containing datoms that changed in :tx-data).
  on-change will be called with a new result when the underlying data changes."
  [conn f relevant? on-change]
  (let [k (keyword (gensym "listener_"))
        current (atom (f @conn))]
    ;; constructor
    (d/listen! conn k
      (fn tx-listener [tx-report]
        (when (relevant? tx-report)
          (let [new (f (:db-after tx-report))]
            ;; TODO: do we have to check if it doesn't change?
            (when (not= new @current)
              (reset! current new)
              (on-change new))))))
    ;; destructor
    (fn cleanup-reffect []
      (d/unlisten! conn k))))

;; TODO: rdbdiff, updates state based on tx only (is this what posh does?)
