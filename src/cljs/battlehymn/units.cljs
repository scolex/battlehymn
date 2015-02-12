(ns battlehymn.units)

(def units
  {:wolf   {:powers {1 {:a 4} 2 {:a 7}}}
   :dragon {:powers {4 {:a 7}}}
   })

(defn get-unit-powers [unit]
  (get-in units [unit :powers]))
