(ns battlehymn.components.vanguard
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [cljs.core.async :refer [put! chan <! alts!]]
            [battlehymn.components.tiles :as tiles]
            [battlehymn.units :as units]
            [battlehymn.hexagonal :as hex]
            [sablono.core :as html :refer-macros [html]]
            [om-tools.dom :as dom :include-macros true]))

(defn background [height width]
  [:rect
   {:x      0
    :y      0
    :width  width
    :height height
    :class  "vanguard-background"}])

(defn vanguard [app chan]
  (comment (len [tile-size (get-in app [:conf :tile-size])
         positions (get-in app [:conf :vanguard-positions])
         strongholds (get-in app [:game :board :strongholds])
         battlefield (get-in app [:game :board :battlefield])]
        )))

(defn vanguard-component [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:chan (chan)})

    om/IWillMount
    (will-mount [_]
      (let [vgchan (om/get-state owner :chan)]
        (go (while true
              (let [[v ch] (alts! [vgchan])]
                (when (= ch vgchan)
                  (do
                    (om/update! app [:ui :vanguard :selected-tile] v))))))))

    om/IRenderState
    (render-state [this {:keys [chan]}]
      (let [selected-pos (get-in app [:ui :vanguard :selected-tile])]
            (html [:div {:on-click (fn [_] (put! chan "selected"))
                         :class-name selected-pos} "ahoj"])

            #_(html
               (let [tile-size (get-in app [:conf :tile-size])
                     positions (get-in app [:conf :vanguard-positions])
                     height (hex/height tile-size)
                     width (* tile-width positions)]
                 [:svg {:version "1.1" :height height :width width}
                  (background height width)
                  (vanguard app chan)]))))))
