(ns electron.core
 (:require [electron.state :refer [main-window]]
           [electron.ffmpeg]
           [electron.menu :refer [init-menu]]
           [electron.init :refer [prepare-preview-dir]]))


(def electron       (js/require "electron"))
(def app            (.-app electron))
(def browser-window (.-BrowserWindow electron))
(def crash-reporter (.-crashReporter electron))


(defn init-browser []
 (reset! main-window (browser-window.
                       (clj->js {:width 800
                                 :height 600})))
  ; Path is relative to the compiled js file (main.js in our case)
 (.loadURL @main-window (str "file://" js/__dirname "/public/index.html"))
 (.on @main-window "closed" #(reset! main-window nil))
 (init-menu)
 (prepare-preview-dir))


; CrashReporter can just be omitted
(.start crash-reporter
        (clj->js
          {:companyName "MyAwesomeCompany"
           :productName "MyAwesomeApp"
           :submitURL "https://example.com/submit-url"
           :autoSubmit false}))

(.on app "window-all-closed" #(when-not (= js/process.platform "darwin")
                                (.quit app)))
(.on app "ready" init-browser)
