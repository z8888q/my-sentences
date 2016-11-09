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
    ["SELECT * from sentences ORDER BY updated_at DESC"]))

(defn get-sentences [id]
  (first (jdbc/query db-spec
    ["SELECT * from sentences WHERE id = ?" id])))

(defn now []
  (str (java.sql.Timestamp. (System/currentTimeMillis))))

(defn create [params]
  (jdbc/insert! db-spec :sentences (merge params {:created_at (now) :updated_at (now)})))

(defn create_by_json [json]
  (jdbc/insert! db-spec :sentences (merge {:sentence (if (not (get json "text"))
                                                       (get json "url")
                                                       (get json "text"))
                                           :is_url (if (get json "isUrl") 1 0)
                                           :created_at (now)
                                           :updated_at (now)})))

(defn save [id params]
  (jdbc/update! db-spec :sentences (merge params {:updated_at (now)}) ["id = ?" id]))

(defn delete [id]
  (jdbc/delete! db-spec :sentences ["id = ?" id]))
