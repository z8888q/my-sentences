(ns myapp.core
  (:require [compojure.core :refer :all]
            [org.httpkit.server :refer [run-server]]
            [compojure.route :as route]
            [myapp.views :as views]
            ;[myapp.posts :as posts]
            [myapp.sentences :as sentences]
            [ring.util.response :as resp]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [clj-json.core :as json]
            [ring.middleware.basic-authentication :refer :all])
  (:gen-class))

(defn authenticated? [name pass]
  (and (= name "user")
       (= pass "pass")))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json" "Access-Control-Allow-Origin" "*"}
   :body (json/generate-string data)})

(defroutes public-routes
  (GET "/" [] (views/main-page))
  (POST "/save_sentence" [& json]
   (do
     (sentences/create_by_json json)
     (json-response {:hello "world"})))
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
