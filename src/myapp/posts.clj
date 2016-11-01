(ns myapp.posts
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec {:classname "com.mysql.jdbc.Driver"
               :subprotocol "mysql"
               :subname "//localhost:3306/myblog"
               :user "root"
               :password "123123"
               :zeroDateTimeBehavior "convertToNull"})

(defn all []
  (jdbc/query db-spec
    ["SELECT * from posts"]))

(defn get-posts [id]
  (first (jdbc/query db-spec
    ["SELECT * from posts WHERE id = ?" id])))

(def now
  (str (java.sql.Timestamp. (System/currentTimeMillis))))

(defn create [params]
  (jdbc/insert! db-spec :posts (merge params {:created_at now :updated_at now})))

(defn save [id params]
  (jdbc/update! db-spec :posts (merge params {:updated_at now}) ["id = ?" id]))

(defn delete [id]
  (jdbc/delete! db-spec :posts ["id = ?" id]))
