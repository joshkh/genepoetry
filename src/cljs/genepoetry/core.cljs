(ns genepoetry.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [genepoetry.handlers]
              [genepoetry.subs]
              [genepoetry.routes :as routes]
              [genepoetry.views :as views]
              [genepoetry.config :as config]))

(when config/debug?
  (println "dev mode"))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init [] 
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
