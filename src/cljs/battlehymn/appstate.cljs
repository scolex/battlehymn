(ns battlehymn.appstate)

(defonce appstate
  (atom {
          :conf {:tile-size     40
                 :board-sides   5
                 :vanguard-size 10
                 }

          :ui   {:battlefield {:selected-tile nil}
                 :vanguard    {:select-tile nil}
                 }
          :game {
                  :round         1
                  :active-player :p1
                  :phase    :phase-1
                  :player        {:p1
                                   {:victory-points 0
                                    :command-points 0}
                                  :p2
                                   {:victory-points 0
                                    :command-points 0}
                                  }
                  :board         {
                                   :strongholds {:-3-0 "p2"
                                                 :-3-3 "p2"
                                                 :3-0  "p1"
                                                 :3--3 "p1"}

                                   :battlefield {:-1-4 {:player "p1"
                                                        :unit   :dragon}
                                                 :-4-3 {:player "p2"
                                                        :unit   :wolf}
                                                 }

                                   :p1          {:reinforcements []
                                                 :captures       []
                                                 :vanguard       [:dragon :wolf]
                                                 }

                                   :p2          {:reinforcements []
                                                 :captures       []
                                                 :vanguard       []
                                                 }
                                   }
                  }}))
