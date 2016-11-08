(defproject myapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [http-kit "2.2.0"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-devel "1.5.0"]
                 [ring/ring-json "0.4.0"]
                 [clj-json "0.5.3"]
                 [hiccup "1.0.5"]
                 ;; https://mvnrepository.com/artifact/mysql/mysql-connector-java
                 ;[mysql/mysql-connector-java "5.1.6"]
                 ;[korma "0.4.3"]
                 [org.clojure/java.jdbc "0.6.2-alpha2"]
                 [mysql/mysql-connector-java "5.1.25"]
                 [ring-basic-authentication "1.0.5"]]
  :aot  [myapp.core]
  :main myapp.core)
