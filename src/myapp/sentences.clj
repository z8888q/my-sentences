(ns myapp.sentences
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec {:classname "com.mysql.jdbc.Driver"
               :subprotocol "mysql"
               :subname "//localhost:3306/myblog"
               :user "root"
               :password "123123"
               :zeroDateTimeBehavior "convertToNull"})

(defn all []
  (jdbc/query db-spec
    ["SELECT * from sentences"]))

(defn get-sentences [id]
  (first (jdbc/query db-spec
    ["SELECT * from sentences WHERE id = ?" id])))

(def now
  (str (java.sql.Timestamp. (System/currentTimeMillis))))

(defn create [params]
  (jdbc/insert! db-spec :sentences (merge params {:created_at now :updated_at now})))

(defn save [id params]
  (jdbc/update! db-spec :sentences (merge params {:updated_at now}) ["id = ?" id]))

(defn delete [id]
  (jdbc/delete! db-spec :sentences ["id = ?" id]))
