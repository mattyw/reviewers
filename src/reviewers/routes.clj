(ns reviewers.routes
 (:use compojure.core
       reviewers.core
       [hiccup.middleware :only (wrap-base-url)])
 (:require [compojure.route :as route]
           [compojure.handler :as handler]
           ))

(defroutes site-routes
  (GET "/" [] (format-list (start-today (list-reviewers 50))))
  (GET "/today" [] (format-list (today-reviewers))))
  ;(GET "/:year/:month/:day" [year month day] (format-list (get-reviewers-str year month day))))

(def app
  (-> (handler/site site-routes)
      (wrap-base-url)))
