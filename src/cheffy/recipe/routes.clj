(ns cheffy.recipe.routes
  (:require [cheffy.recipe.handlers :as recipe]
            [cheffy.responses :as responses]))

(defn routes [env]
  (let [db (:jdbc-url env)]
    ["/recipes" {:swagger {:tags ["recipes"]}}
     ["/" {:get {:handler (recipe/list-recipes db)
                 :responses {200 {:body responses/recipes}}
                 :summary "List all recipes"}}]
     ["/:recipe-id" {:get {:handler (recipe/retrieve-recipe db)
                           :summary "Retrieve a recipe"
                           :responses {200 {:body responses/recipe}}
                           :parameters {:path {:recipe-id string?}}}}]]))