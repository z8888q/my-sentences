(ns myapp.views
  (:use
    [hiccup.form]
    [hiccup.page :only (html5 include-css include-js)])
  (:require [hiccup.core :refer (html)]
            [myapp.posts :as posts]))

(defn layout [title & content]
  (html5
    [:head [:title title]
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     (include-css "css/bootstrap-3.3.7.min.css")
     (include-css "css/bootstrap-theme-3.3.7.min.css")
     (include-css "css/mystyle.css")
     (include-js "js/jquery-3.1.0.min.js")
     (include-js "js/bootstrap-3.3.7.min.js")
     (include-js "js/my.js")
     ]
    [:body
     [:div.container content]]))

(defn main-page []
  (layout "My Blog"
    [:h1 "My Blog"]
    [:p "Welcome to my page"]
    [:a {:href "/admin"} "Enter"]))

(def showDateFormat
  (java.text.SimpleDateFormat. "yyyy年MM月dd日 HH时mm分"))

(defn formatDate [date]
  (.format showDateFormat date))

; Post is a map corresponding to a record from the database
(defn post-summary [post]
  (let [id (:id post)
        title (:title post)
        body (:body post)
        updated_at (:updated_at post)]
    [:section.panel.panel-default
     [:div.panel-body
      [:small.text-muted.right (formatDate updated_at)]
      [:div
       [:h3 title]
       [:h4 body]]
      [:section.actions.right
       [:a.edit-posts "Edit"] " / "
       [:a {:href (str "/admin/" id "/delete")} "Delete"]]
      [:div.edit-box
       (form-to [:post (str "/admin/" id "/save")]
                [:div.form-group
                 (label "title" "Title")
                 (text-field {:class "form-control edit-title"} "title" (:title post))]
                [:div.form-group
                 (label "body" "Body")
                 (text-area {:class "form-control edit-body" :rows 3} "body" (:body post))]
                (submit-button {:class "btn btn-default"} "Save"))
       ]
      ]
      ]))

(defn admin-blog-page []
  (layout "My Blog - Administer Blog"
    ;[:h1 "Administer Blog"]
    [:span.right-top.label.label-info " By XQ "]
    [:div.panel
     [:div.panel-body
      [:h2 "All my posts"]
      [:a.btn.btn-primary.right {:href "/admin/add"} "Add"]]]
    (map #(post-summary %) (posts/all))))

(defn add-post []
  (layout "My Blog - Add Post"
    (list
      [:h2 "Add Post"]
      (form-to [:post "/admin/create"]
        (label "title" "Title")
        (text-field "title") [:br]
        (label "body" "Body") [:br]
        (text-area {:rows 20} "body") [:br]
        (submit-button "Save")))))

(defn edit-post [id]
  (layout "My Blog - Edit Post"
  (list
    (let [post (posts/get-posts id)]
    [:h2 (str "Edit Post " id)]
    (form-to [:post "save"]
      (label "title" "Title")
      (text-field "title" (:title post)) [:br]
      (label "body" "Body") [:br]
      (text-area {:rows 20} "body" (:body post)) [:br]
      (submit-button "Save"))))))
