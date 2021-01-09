(ns cheffy.recipe.routes
  (:require [cheffy.recipe.handlers :as recipe]))

(defn routes [env]
  (let [db (:jdbc-url env)]
    ["/recipes" {:swagger {:tags ["recipes"]}}
     ["/" {:get {:handler (recipe/list-recipes db)
                 :summary "List all recipes"}}]
     ["/:recipe-id" {:get (recipe/retrieve-recipe db)
                     :summary "Retrieve a recipe"}]]))