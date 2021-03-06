(ns user
  (:require [integrant.repl :as ig-repl]
            [integrant.core :as ig]
            [integrant.repl.state :as state]
            [cheffy.server]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]))

(ig-repl/set-prep!
  (fn []
    (-> "resources/config.edn" slurp ig/read-string)))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app
  (-> state/system :cheffy/app))
(def db
  (-> state/system :db/postgres))

(comment
  (app {:request-method :get
        :uri "/swagger.json"})
  (app {:request-method :get
        :uri "/v1/recipes"})
  (-> (app {:request-method :get
            :uri "/v1/recipes"}) :body (slurp))
  (-> (app {:request-method :post
            :uri "/v1/recipes"
            :body-params {:name "my recipe"
                          :prep-time 49
                          :img "image-url"}})
      :body
      (slurp))
  (->  (app {:request-method :put
             :uri "/v1/recipes/a3dde84c-4a33-45aa-b0f3-4bf9ac997680"
             :body-params {:name "chiclete"
                           :prep-time 49
                           :public false
                           :img "url"}}) :body (slurp))
  (app {:request-method :put
        :uri "/v1/recipes/a3dde84c-4a33-45aa-b0f3-4bf9ac997680"
        :body-params {:name "chiclete"
                      :prep-time 49
                      :public false
                      :img "url"}})
  (jdbc/execute! db ["SELECT * FROM recipe WHERE public = true"])
  (sql/find-by-keys db :recipe {:public false})
  (jdbc/execute! db ["SELECT * FROM account"])
  (go)
  (halt)
  (reset)
  (reset))

