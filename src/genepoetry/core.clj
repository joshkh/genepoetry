(ns genepoetry.core
  (:use [clj-wordnet.core])
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv])
  (:gen-class))

(def wordnet (make-dictionary "data/dict/"))

(def dog (first (wordnet "NLP")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn read-gene-data []
  (with-open [in-file (io/reader "resources/genedata.tsv")]
    (doall
      (csv/read-csv in-file :separator \tab))))

(def genes (read-gene-data))

(defn isword? [gene]
  (some? (first (wordnet gene))))

(defn unnamed? [val]
  (= "unnamed" val))

(defn doit []
  (remove empty? (reduce (fn [m next]
            (conj m (filter isword? next))) [] genes)))

; (println dog)

(println (doit))

; (explain)


;   (defn read-gene-data []
;     (with-open [in-file (io/reader "resources/genes-synonyms.tsv")]
;       (doall
;         (csv/read-csv in-file :separator \tab))))
;
;   (defn read-dictionary []
;     (with-open [in-file (io/reader "resources/sowpods.txt")]
;       (doall
;         (csv/read-csv in-file))))
;
;   (def genes (vec (flatten (map clojure.string/lower-case (read-gene-data)))))
;   (def dictionary (vec (flatten (read-dictionary))))
;
;   (defn cross []
;     (println "type" (take 5 genes))
;     (println "type2" (take 5 dictionary))
;     (println "diff" (count (nth (diff genes dictionary) 2)))
;     )
;
;
; (cross)
