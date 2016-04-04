(ns genepoetry.dictionary
  (:use [clj-wordnet.core])
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.data.csv :as csv]))


(def parsed (filter (fn [n] (not= "unnamed" (:genetic-word n))) (read-string (slurp "data.edn"))))

(def keepers ["ed" "es" "s" "y" "ly" "1" "2" "4" "8"])

(defn numeric? [s]
  (if-let [s (seq s)]
    (let [s (if (= (first s) \-) (next s) s)
          s (drop-while #(Character/isDigit %) s)
          s (if (= (first s) \.) (next s) s)
          s (drop-while #(Character/isDigit %) s)]
      (empty? s))))

(defn single-letters []
  (filter (fn [e] (= 1 (count (:genetic-word e)))) parsed))

(defn helpers []
  (filter (fn [e]
            (some true? (map #(= % (clojure.string/lower-case (:genetic-word e))) keepers))) parsed))

(def dictionary {:noun (vec (filter #(some? (some #{:noun} (:pos %))) parsed))
                 :adjective (vec (filter #(some? (some #{:adjective} (:pos %))) parsed))
                 :verb (vec (filter #(some? (some #{:verb} (:pos %))) parsed))
                 :adverb (vec (filter #(some? (some #{:adverb} (:pos %))) parsed))
                 :single-letters (vec (single-letters))
                 :helpers (vec (distinct (helpers)))})

(def defaults {:noun 15
               :adjective 15
               :verb 15
               :adverb 15
               :single-letters 10
               :helpers (count (:helpers dictionary))})

(defn grab-bag-sorted []
  (let [classes [:noun :adjective :verb :adverb]]
    (reduce (fn [bag class]
              (assoc bag class
                     (mapcat (class dictionary)
                          (take (class defaults)
                                (shuffle (range (count (class dictionary))))))))
            {}
            classes)))



(defn grab-bag []
  (let [classes [:noun :adjective :verb :adverb :single-letters :helpers]]
    (reduce (fn [bag class]
              (into bag (map (class dictionary)
                             (take (class defaults)
                                   (shuffle (range (count (class dictionary))))))))
            '()
            classes)))