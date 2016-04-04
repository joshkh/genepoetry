(ns genepoetry.views
    (:require [re-frame.core :as re-frame]
              [reagent.core :as reagent]))


;; home

(defn home-panel []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [:div (str "Hello from " @name ". This is the Home Page.")
       [:div [:a {:href "#/about"} "go to About Page"]]])))


;; about

(defn about-panel []
  (fn []
    [:div "This is the About Page."
     [:div [:a {:href "#/"} "go to Home Page"]]]))

;; magnet panel

(defn fridge-frame []
  (fn []
    [:div.fridge-frame-outer
     [:div.fridge-frame]]))

(defn magnet []
  (reagent/create-class
    {:reagent-render      (fn [m]
                            [:div.magnet (:genetic-word m)])
     :component-did-mount (fn [e]
                            (-> (js/$ (reagent/dom-node e))
                                (.css "transform" (str "rotate(" (- (rand-int 5) (rand-int 10)) "deg)"))
                                .draggable))}))

(defn magnet-panel []
  (let [magnets (re-frame/subscribe [:magnets])]
    (fn []
      [:div
       [fridge-frame]
       (for [m @magnets]
         ^{:key (:identifier m)} [magnet m])])))


;; main

(defmulti panels identity)
(defmethod panels :home-panel [] [magnet-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      (panels @active-panel))))
