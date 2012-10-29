(ns gratefulplace.controllers.comments
  (:require [ring.util.response :as res]
            [net.cgrand.enlive-html :as h]
            [gratefulplace.models.comment :as comment]
            [gratefulplace.views.comments :as view]
            [cemerick.friend :as friend])

  (:use [gratefulplace.controllers.common :only (if-valid)]
        gratefulplace.controllers.common.content))

(def validations
  [[:content
    ["Whoops! You forgot to write anything"
     #(> (count %) 4)]]])

(defn create!
  [params]
  (let [comment (comment/create! (assoc params
                                   :user_id
                                   (:id (friend/current-authentication))))]
    (res/redirect (str "/posts/" (:post_id params) "#comment-" (:id comment)))))

(defn edit
  [id]
  (view/edit (comment/by-id id)))

(def update (update-fn comment/by-id comment/update!))