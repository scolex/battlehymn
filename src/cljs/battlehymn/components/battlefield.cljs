(ns battlehymn.components.battlefield
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :refer [put! chan <! alts!]]
            [battlehymn.components.tiles :as tiles]
            [battlehymn.units :as units]
            [battlehymn.hexagonal :as hex]))

(defn board-get [i j struct]
  ((keyword (str i "-" j)) struct))


(defn- background [height width]
  [:rect
   {:x      0
    :y      0
    :width  width
    :height height
    :class  "battlefield-background"}])


(defn- hexgrid [app chan size sides]
  (let [height (hex/height size)
        width (hex/width size)
        vert (* (/ 3 4) height)
        strongholds (get-in app [:game :board :strongholds])
        battlefield (get-in app [:game :board :battlefield])]
    (for [i (range (+ (- sides) 1) sides)
          j (range (+ (- sides) 1) sides)]
      (when (and (> (+ j i) (- sides)) (< (+ j i) sides))
        (let [x (+ (* j width) (* i (/ width 2)))
              y (+ (* i vert))
              content (board-get i j battlefield)]
          (cond content
                (tiles/unit size x y content)
                (board-get i j strongholds)
                (tiles/stronghold app chan size x y (board-get i j strongholds))
                :else (tiles/blank
                        app
                        chan
                        x
                        y
                        size
                        (str i "-" j))))))))


(defn- battlefield-component [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:chan (chan)})

    om/IWillMount
    (will-mount [_]
      (let [bfchan (om/get-state owner :chan)]
        (go (while true
              (let [[v ch] (alts! [bfchan])]
                (when (= ch bfchan)
                  (do
                    (om/update! app [:ui :battlefield :selected-tile] v))))))))

    om/IRenderState
    (render-state [this {:keys [chan]}]
      (html
        (let [tile-size (get-in app [:conf :tile-size])
              board-sides (get-in app [:conf :board-sides])
              board-height (hex/mesh-height tile-size board-sides)
              board-width (hex/mesh-width tile-size board-sides)
              translate (str "translate(" (/ board-width 2) "," (/ board-height 2) ")")]
          [:svg {:version "1.1" :height board-height :width board-width}
           (background board-height board-width)
           [:g {:transform translate}
            (hexgrid app chan tile-size board-sides)]])))))
