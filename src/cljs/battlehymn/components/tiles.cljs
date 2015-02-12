(ns battlehymn.components.tiles
  (:require [om.core :as om :include-macros true]
            [battlehymn.hexagonal :as hex]
            [battlehymn.units :as units]
            [cljs.core.async :refer [put! chan <! alts!]]))


(defn tile-selected [pos selected-pos]
  (if (= pos selected-pos)
    "tile tile-empty tile-selected"
    "tile tile-empty"))


(defn power-symbol [size cx cy angle power className]
  (let [d (/ size 6)
        l (/ size 9)
        offset (/ (hex/width size) 2)]
    [:g {:class     className
         :transform (str "translate(" 0 "," (- offset) ") " "rotate(" angle ",0," offset ")")}
     [:polygon {:points [0 0 l d (- l) d] :fill "black"}]
     [:circle {:cx 0 :cy (* 1.7 d) :r d :fill "white"}]
     [:text {
              :x (- (/ size 15))
              :y (/ size 2.5)} power]]))


(defn base [app _]
  (reify
    om/IRenderState
    (render-state [this {:keys [chan x y size pos className]}]
      (let [selected-pos (get-in app [:ui :battlefield :selected-tile])
            class-name (tile-selected selected-pos pos)]
        (html [:g {:class-name class-name
                   :transform  (str "translate(" x "," y ")")
                   :on-click  {(fn [_] (put! chan pos))}}
               [:polygon {:points (hex/coords-str size)}]])))))


(defn blank [app chan x y size pos]
  (om/build base app {:init-state {:chan      chan
                                   :x         x
                                   :y         y
                                   :size      size
                                   :pos       pos
                                   :className "kokos"}}))


(defn stronghold [app chan size x y player]
  (om/build base app {:init-state {:chan      chan
                                   :x         x
                                   :y         y
                                   :size      size
                                   :pos       "nic"
                                   :className (str "tile tile-stronghold-" player)}}))

(defn unit [size x y spec]
  (let [player (:player spec)
        powers (units/get-unit-powers (:unit spec))]
    [:g {:class     (str "tile tile-unit-" player)
         :transform (str "translate(" x "," y ")")}
     [:polygon {:points (hex/coords-str size)}]
     (for [i (range 6)]
       (when-let [{attack :a} (powers i)]
         (power-symbol size y x (+ 30 (* i 60)) attack "power-symbol")))]))
