(ns zest.docs.registry
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs.core.async :as async]))

(defn get-so-root []
  (let [electron (.require js/window "electron")
        path (.require js/window "path")]
    (.join path
           (.getDataPath (.-app (.-remote electron)))
           "so")))

(defn get-devdocs-root []
  (let [electron (.require js/window "electron")
        path (.require js/window "path")]
    (.join path
           (.getDataPath (.-app (.-remote electron)))
           "devdocs")))

(defn get-available-devdocs [cb]
  (let [electron (.require js.window "electron")
        fs (.require js/window "fs")
        mkdirp (.require js/window "mkdirp")
        path (.require js/window "path")
        request (.require js/window "request")
        devdocs-json (.join path
                            (.getDataPath (.-app (.-remote electron)))
                            "devdocs.json")

        fetch
        (fn []
          (request "https://devdocs.io/docs.json"
                   (fn [error response body]
                     (.writeFileSync fs devdocs-json body)
                     (cb (.parse js/JSON body)))))]

    (.sync mkdirp (get-devdocs-root))
    (if (and (.existsSync fs devdocs-json)
             (.isFile (.statSync fs devdocs-json)))
      (try
        (cb (.parse js/JSON (.readFileSync fs devdocs-json)))
        (catch js/Error e
          (.log js/console (str "Error reading devdocs.json: " e "; fetching"))
          (fetch)))
      (do
        (.log js/console "devdocs.json missing - fetching")
        (fetch)))))


(defn get-installed-devdocs []
  (let
    [fs (.require js/window "fs")
     mkdirp (.require js/window "mkdirp")]
    (.sync mkdirp (get-devdocs-root))
    (.readdirSync fs (get-devdocs-root))))

(def installed-devdocs-atom (reagent/atom
                              (zest.docs.registry/get-installed-devdocs)))

(def installed-devdocs-chan (async/chan))

(defn update-installed-devdocs []
  (let [installed (get-installed-devdocs)]
    (go (async/>! installed-devdocs-chan installed))
    (reset! installed-devdocs-atom installed)))