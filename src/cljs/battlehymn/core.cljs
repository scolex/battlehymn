(ns battlehymn.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [battlehymn.appstate :as appstate]
            [battlehymn.components.vanguard :as vanguard]
            [battlehymn.components.battlefield :as b]))

(enable-console-print!)

(defn main []
  #_(om/root b/battlefield-component appstate/appstate
           {:target (.getElementById js/document "battlefield")})

  (om/root vanguard/vanguard-component appstate/appstate
           {:target (.getElementById js/document "vanguard")}))
