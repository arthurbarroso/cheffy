(ns cheffy.recipe.db
  (:require [next.jdbc.sql :as sql]
            [next.jdbc :as jdbc]))

(defn find-all-recipes [db uid]
  (with-open [conn (jdbc/get-connection db)]
    (let [public (sql/find-by-keys conn :recipe {:public true})]
      (if uid
        (let [drafts (sql/find-by-keys conn :recipe {:public false :uid uid})]
          {:public public
           :drafts drafts}))
      {:public public})))

(defn find-recipe-by-id [db recipe-id]
  (with-open [conn (jdbc/get-connection db)]
    (let [recipe (sql/get-by-id conn :recipe recipe-id :recipe_id {})
          steps (sql/find-by-keys conn :step {:recipe_id recipe-id})
          ingredients (sql/find-by-keys conn :ingredient {:recipe_id recipe-id})]
      (when (seq recipe)
        (assoc recipe
          :recipe/ingredients ingredients
          :recipe/steps steps)
        ))))