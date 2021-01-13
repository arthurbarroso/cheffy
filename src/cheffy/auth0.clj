(ns cheffy.auth0
  (:require [clj-http.client :as http]
            [muuntaja.core :as m]))

(defn get-test-token
  []
  (->>
    {:content-type :json
     :cookie-policy :standard
     :body         (m/encode "application/json"
                             {:client_id "redacted"
                              :audience "https://reitit-cheffy.us.auth0.com/api/v2/"
                              :grant_type "password"
                              :username "redacted"
                              :password "redacted"
                              :scope "openid profile email"})}
    (http/post "https://reitit-cheffy.us.auth0.com/oauth/token") (m/decode-response-body)
    :access_token))

(comment
  (get-test-token))