(ns myapp.core
  (:require [compojure.core :refer :all]
            [org.httpkit.server :refer [run-server]]
            [compojure.route :as route]
            [myapp.views :as views]
            ;[myapp.posts :as posts]
            [myapp.sentences :as sentences]
            [ring.util.response :as resp]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.basic-authentication :refer :all])
  (:gen-class))

(defn authenticated? [name pass]
  (and (= name "user")
       (= pass "pass")))

(defroutes public-routes
  (GET "/" [] (views/main-page))
  (route/resources "/"))

(defroutes protected-routes
  (GET "/admin" [] (views/admin-blog-page))
  (GET "/admin/add" [] (views/add-sentence))
  (POST "/admin/create" [& params]
        (do
          (sentences/create params)
          (resp/redirect "/admin")))
  (GET "/admin/:id/edit" [id] (views/edit-sentence id))
  (POST "/admin/:id/save" [& params]
    (do
      (sentences/save (:id params) params)
        (resp/redirect "/admin")))
  (GET "/admin/:id/delete" [id]
    (do (sentences/delete id)
      (resp/redirect "/admin"))))

(defroutes myapp
  public-routes
  (wrap-basic-authentication protected-routes authenticated?)
  (route/not-found "Not Found"))

(def reloadable-app
  (-> myapp
    (wrap-params)
    (wrap-reload)))

(defn -main [& args]
  (run-server reloadable-app {:port 5000})
  (println "Start Http-Kit Server On Port 5000..."))
