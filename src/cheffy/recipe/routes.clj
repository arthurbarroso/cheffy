(ns cheffy.recipe.routes
  (:require [cheffy.recipe.handlers :as recipe]
            [cheffy.responses :as responses]
            [cheffy.middleware :as mw]))

(defn routes [env]
  (let [db (:jdbc-url env)]
    ["/recipes" {:swagger {:tags ["recipes"]}
                 :middleware [[mw/wrap-auth0]]}
     [""
      {:get  {:handler   (recipe/list-recipes db)
              :responses {200 {:body responses/recipes}}
              :summary   "List all recipes"}
       :post {:handler    (recipe/create-recipe! db)
              :parameters {:body {:name      string?
                                  :prep-time number?
                                  :img       string?}}
              :responses  {201 {:body {:recipe-id string?}}}
              :summary    "Create recipe"}}]
     ["/:recipe-id" {:get {:handler (recipe/retrieve-recipe db)
                           :summary "Retrieve a recipe"
                           :responses {200 {:body responses/recipe}}
                           :parameters {:path {:recipe-id string?}}}
                     :put {:handler (recipe/update-recipe! db)
                           :parameters {:path {:recipe-id string?}
                                        :body {:name string? :prep-time int? :public boolean? :img string?}}
                           :summary "Update a recipe"
                           :responses {204 {:body nil?}}}
                     :delete {:handler (recipe/delete-recipe! db)
                              :parameters {:path {:recipe-id string?}
                                           :responses {204 {:body nil}}
                                           :summary "Delete recipe"}}}]]))