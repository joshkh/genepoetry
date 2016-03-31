(ns genepoetry.core
  (:use [clj-wordnet.core])
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.data.csv :as csv])
  (:gen-class))

(def wordnet (make-dictionary "data/dict/"))

(def run (take 10 (wordnet "gohsgousdogusdg")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn read-gene-data []
  (with-open [in-file (io/reader "resources/genedata.tsv")]
    (doall
      (csv/read-csv in-file :separator \tab))))

(def genes (vec (read-gene-data)))

(def parsed (read-string (slurp "data.edn")))

(def dictionary {:noun (vec (filter #(some? (some #{:noun} (:pos %))) parsed))
                  :adjective (vec (filter #(some? (some #{:adjective} (:pos %))) parsed))
                  :verb (vec (filter #(some? (some #{:verb} (:pos %))) parsed))
                  :adverb (vec (filter #(some? (some #{:adverb} (:pos %))) parsed))})

(def defaults {:noun 15
               :adjective 15
               :verb 15
               :adverb 15})



(defn unique-random-numbers [n]
  (let [a-set (set (take n (repeatedly #(rand-int n))))]
    (concat a-set (set/difference (set (take n (range)))
                                  a-set))))


(defn grab-bag []
  (let [classes [:noun :adjective :verb :adverb]]
    (reduce (fn [bag class]
              (assoc bag class
                     (map (class dictionary)
                          (take (class defaults)
                                (shuffle (range (count (class dictionary))))))))
            {}
            classes)))

(defn isword? [gene]
  (some? (first (wordnet gene))))

(defn unnamed? [val]
  (= "unnamed" val))


(defn lookup-gene [data]
  (remove nil? (map-indexed (fn [idx datum]
                              (let [words (take 10 (wordnet datum))]
                                (if-not (empty? words)
                                  (let [pos (distinct (map :pos words))]
                                    {:identifier (first data)
                                     :genetic-word datum
                                     :english-word (:lemma (first words))
                                     :pos pos
                                     :attribute (nth (first genes) idx)})))) data)))

; (defn tester []
;   (mapcat lookup-gene (take 10000 (rest genes))))

; (defn parse-genes []
;   (distinct (mapcat lookup-gene (rest genes))))

; (defn write-contents [contents]
;   (clojure.pprint/pprint (vec (remove empty? contents)) (clojure.java.io/writer "data.edn")))
