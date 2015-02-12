(ns battlehymn.hexagonal
  (:require [clojure.string :as s]))

(defn height [size]
  (* 2 size))

(defn width [size]
  (* (/ (Math/sqrt 3) 2) (height size)))

(defn mesh-height [tile-size nsides]
  (let [h (height tile-size)]
    (+ h
       (* (/ 3 4) h (- (* 2 nsides) 2)))))

(defn mesh-width [tile-size nsides]
  (* (width tile-size)
     (- (* 2 nsides) 1)))

(defn coords [size]
  (for [i (range 0 6)
        :let [angle (* (/ (* 2 Math/PI) 6) (+ i 0.5))
              x (* size (Math/cos angle))
              y (* size (Math/sin angle))]]
    [x y]))

(defn coords-str [size]
  (s/join " "
          (map #(s/join "," %) (coords size))))


