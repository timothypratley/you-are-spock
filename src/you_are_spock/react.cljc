(ns you-are-spock.react)


(defn- ->deps
  "Returns a deps array containing a single hash, suitable for specifying hook dependencies.
  Note that you can pass in a vector to conveniently watch multiple values."
  [x]
  #?(:clj [(hash x)]
     :cljs #js [(hash x)]))

(defn noop [])

(defn use-state
  "Returns the initial value, and a function to update it.
  On subsequent renders, will return the current state and updater.
  Analogous to Reagent track in that it looks like immediate code, but the state is not recreated.
  Calling the update function will cause a re-render to occur.
  You may call the update function with a value (similar to reset!) or a function (similar to swap!).
  If you update to the same value as the current state,
  render would be called but bail out without rendering the children or firing effects instead.
  React may still need to render that specific component again before bailing out."
  ([]
   #?(:clj [nil noop]
      :cljs (js/React.useState)))
  ([initial-state]
   #?(:clj (if (fn? initial-state)
             [(initial-state) noop]
             [initial-state noop])
      :cljs (js/React.useState initial-state))))

(defn use-state*
  ([]
   (let [[state set-state] (js/React.useState)]
     [state (fn set-state-if-changed [x-or-f]
              (let [x (if (fn? x-or-f)
                        (x-or-f state)
                        x-or-f)]
                (if (= x state)
                  state
                  (set-state x))))])))
;; does set state return a value?


;;  Can deps be external to the component? no render?
(defn use-effect
  "Hook that accepts a construction function, which can return a destructor function.
  This allows you to do setup/teardown based on a component being mounted/unmounted.
  You can also also supply dependencies to cause setup/teardown to occur in response to data changing.
  Supply empty dependencies to limit behavior to mount/unmount.

  Accepts a function f that contains imperative, possibly effectful code and dependencies `deps`.
  f will be called after every completed render when the deps data changes.
  f should return a cleanup callback or nil.
  When returning a callback, that callback will be called after unmount or deps change."
  [f deps]
  #?(:cljs (js/React.useEffect
             (fn []
               (let [cleanup (f deps)]
                 ;; do not treat collections keywords etc as a cleanup function
                 (if (fn? cleanup)
                   cleanup
                   js/undefined)))
             (->deps deps))))

