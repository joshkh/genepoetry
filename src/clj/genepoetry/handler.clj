(ns genepoetry.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [file-response]]
            [genepoetry.dictionary :as dictionary]))

(defroutes handler
  (GET "/" [] (file-response "index.html" {:root "resources/public"}))
  (GET "/api/magnets" [] {:body (dictionary/grab-bag)}))

(def app
  (-> (handler/site handler)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))
