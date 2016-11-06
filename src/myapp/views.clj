(ns myapp.views
  (:use
    [hiccup.form]
    [hiccup.page :only (html5 include-css include-js)])
  (:require [hiccup.core :refer (html)]
            ;[myapp.posts :as posts]
            [myapp.sentences :as sentences]
            ))

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
  (layout "My Sentences"
    [:h1 "My MySentences"]
    [:p "Welcome to my page"]
    [:a {:href "/admin"} "Enter"]))

(def showDateFormat
  (java.text.SimpleDateFormat. "yyyy年MM月dd日 HH时mm分"))

(defn formatDate [date]
  (.format showDateFormat date))

; Post is a map corresponding to a record from the database
(defn sentence-summary [sentence_item]
  (let [id (:id sentence_item)
        sentence (:sentence sentence_item)
        updated_at (:updated_at sentence_item)]
    [:section.panel.panel-default
     [:div.panel-body
      [:small.text-muted.right (formatDate updated_at)]
      [:div
       [:h4 sentence]]
      [:section.actions.right
       [:a.edit-posts "Edit"] " / "
       [:a {:href (str "/admin/" id "/delete")} "Delete"]]
      [:div.edit-box
       (form-to [:post (str "/admin/" id "/save")]
                [:div.form-group
                 (label "sentence" "Sentence")
                 (text-area {:class "form-control edit-body" :rows 3} "sentence" (:sentence sentence_item))]
                (submit-button {:class "btn btn-default"} "Save"))
       ]
      ]
      ]))

(defn admin-blog-page []
  (layout "My Sentences - Administer"
    ;[:h1 "Administer Blog"]
    [:span.right-top.label.label-info " By XQ "]
    [:div.panel
     [:div.panel-body
      [:h2 "All my sentences"]
      [:a.btn.btn-primary.right.add-sentence-btn "Add"]
      (form-to {:class "form-inline edit-box row"} [:post "/admin/create"]
               [:div.input-group
                (text-field {:class "form-control" :placeholder "Type Sentence"} "sentence")
                [:span.input-group-btn
                 (submit-button {:class "btn btn-primary"} "Save")]])
      ]]
    (map #(sentence-summary %) (sentences/all))))

(defn add-sentence []
  (layout "My Sentences - Add Sentence"
    (list
      [:h2 "Add Sentence"]
      (form-to [:post "/admin/create"]
        (label "sentence" "Sentence") [:br]
        (text-area {:rows 20} "sentence") [:br]
        (submit-button "Save")))))

(defn edit-sentence [id]
  (layout "My Sentence - Edit Sentence"
  (list
    (let [sentence (sentences/get-sentences id)]
    [:h2 (str "Edit Sentence " id)]
    (form-to [:post "save"]
      (label "sentence" "Sentence") [:br]
      (text-area {:rows 20} "body" (:sentence sentence)) [:br]
      (submit-button "Save"))))))
