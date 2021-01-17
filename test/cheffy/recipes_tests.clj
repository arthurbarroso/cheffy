(ns cheffy.recipes-tests
  (:require [clojure.test :refer :all]
            [cheffy.server :refer :all]
            [cheffy.test-system :as ts]))

(use-fixtures :once ts/token-fixture)

(def recipe-id (atom nil))

(def step-id (atom nil))

(def ingredient-id (atom nil))

(def recipe
  {:img       "https://images.unsplash.com/photo-1547516508-4c1f9c7c4ec3?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=3318&q=80"
   :prep-time 30
   :name      "My Test Recipe"})

(def step
  {:description "cool step :)"
  :sort 1})

(def ingredient
  {:sort 1
   :amount 2
   :measure "30 grams"
   :name "flour"})

(def update-recipe
  (assoc recipe :public true))

(deftest recipes-tests
  (testing "List recipes"
    (testing "with auth -- public and drafts"
      (let [{:keys [status body]} (ts/test-endpoint :get "/v1/recipes" {:auth true})]
        (is (= 200 status))
        (is (vector? (:public body)))
        (is (vector? (:drafts body)))))
    (testing "without auth -- public"
      (let [{:keys [status body]} (ts/test-endpoint :get "/v1/recipes" {:auth false})]
        (is (= 200 status))
        (is (vector? (:public body)))
        (is (nil? (:drafts body)))))
    ))

(deftest recipes-tests-2
  (testing "Create recipe"
    (let [{:keys [status body]} (ts/test-endpoint :post "/v1/recipes" {:auth true :body recipe})]
      (reset! recipe-id (:recipe-id body))
      (is (= 201 status))))

  (testing "Update recipe"
    (let [{:keys [status]} (ts/test-endpoint :put (str "/v1/recipes/" @recipe-id) {:auth true :body update-recipe})]
      (is (= status 204))))

  (testing "Favorite recipe"
    (let [{:keys [status]} (ts/test-endpoint :post (str "/v1/recipes/" @recipe-id "/favorite") {:auth true :body recipe})]
      (is (= 204 status))))

  (testing "Unfavorite recipe"
    (let [{:keys [status]} (ts/test-endpoint :delete (str "/v1/recipes/" @recipe-id "/favorite") {:auth true :body recipe})]
      (is (= 204 status))))

  (testing "Create step"
    (let [{:keys [status body]} (ts/test-endpoint :post (str "/v1/recipes/" @recipe-id "/steps")
                                                  {:auth true :body step})]
      (reset! step-id (:step-id body))
      (is (= 201 status))))

  (testing "Update step"
    (let [{:keys [status]} (ts/test-endpoint :put (str "/v1/recipes/" @recipe-id "/steps")
                                             {:auth true :body
                                                          {:step-id @step-id
                                                          :sort 2
                                                          :description "updated step"}})]
      (is (= status 204))))

  (testing "Delete step"
    (let [{:keys [status]} (ts/test-endpoint :delete (str "/v1/recipes/" @recipe-id "/steps")
                                             {:auth true :body {:step-id @step-id}})]
      (is (= status 204))))



  (testing "Create ingredient"
    (let [{:keys [status body]} (ts/test-endpoint :post (str "/v1/recipes/" @recipe-id "/ingredients")
                                                  {:auth true :body ingredient})]
      (reset! ingredient-id (:ingredient-id body))
      (is (= 201 status))))

  (testing "Update ingredient"
    (let [{:keys [status]} (ts/test-endpoint :put (str "/v1/recipes/" @recipe-id "/ingredients")
                                             {:auth true :body
                                                    {:ingredient-id @ingredient-id
                                                     :sort 2
                                                     :amount 3
                                                     :measure "30 grams"
                                                     :name "updated ingredient"
                                                     }})]
      (is (= status 204))))

  (testing "Delete ingredient"
    (let [{:keys [status]} (ts/test-endpoint :delete (str "/v1/recipes/" @recipe-id "/ingredients")
                                             {:auth true :body {:ingredient-id @ingredient-id}})]
      (is (= status 204))))


  (testing "Delete recipe"
    (let [{:keys [status]} (ts/test-endpoint :delete (str "/v1/recipes/" @recipe-id) {:auth true :body recipe})]
      (is (= 204 status))))

  )





(comment
  (ts/test-endpoint :post "/v1/recipes" {:auth true
                                         :body recipe})
  (ts/test-endpoint :post "/v1/recipes/6bf9207c-38d2-4176-9c6d-f7a268deba75/favorite" {:auth true})
  (ts/test-endpoint :delete "/v1/recipes/6bf9207c-38d2-4176-9c6d-f7a268deba75/favorite" {:auth true})
  (ts/test-endpoint :put "/v1/recipes/79fc1d97-2dca-4698-a23a-09ace63b976e" {:auth true
                                                                             :body update-recipe})
  (ts/test-endpoint :delete "/v1/recipes/0bfed7c3-566e-4772-88db-ed4bd61deb81" {:auth true}))