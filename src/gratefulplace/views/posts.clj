(ns gratefulplace.views.posts
  (:require [net.cgrand.enlive-html :as h])
  (:use [gratefulplace.views.common :only [*template-dir* defpage]]))

(defn comments
  [post]
  (let [comment-count (get :comment-count post 0)]
    (if (zero? comment-count)
      "Comment"
      (str  comment-count " comments"))))

(defn timestamp->string
  [timestamp]
  (-> (java.text.SimpleDateFormat. "MMM dd, yyyy hh:mma")
      (.format timestamp)))

(defn post-path
  [post]
  (str "/posts/" (:id post)))

(defpage all "index.html"
  [posts]
  ;; don't show the second post as it's just an example
  [[:.post (h/nth-of-type 2)]] nil
  [:.post] (h/clone-for [post posts]
                        [:.author]   (h/content (:username   post))
                        [:.date]     (h/content (timestamp->string (:created_on post)))
                        [:.content]  (h/content (:content    post))
                        [:.comments] (h/do->
                                      (h/content (comments    post))
                                      (h/set-attr :href (post-path post)))))

(defpage show "posts/show.html"
  [post]
  [:.post :.author]  (h/content (:username post))
  [:.post :.date]    (h/content (timestamp->string (:created_on post)))
  [:.post :.content] (h/content (:content  post)))

(defpage show-new "posts/new.html"
  [])