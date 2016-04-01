(ns genepoetry.dictionary
  (:use [clj-wordnet.core])
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.data.csv :as csv]))


(def parsed (read-string (slurp "data.edn")))

(def dictionary {:noun (vec (filter #(some? (some #{:noun} (:pos %))) parsed))
                 :adjective (vec (filter #(some? (some #{:adjective} (:pos %))) parsed))
                 :verb (vec (filter #(some? (some #{:verb} (:pos %))) parsed))
                 :adverb (vec (filter #(some? (some #{:adverb} (:pos %))) parsed))})

(def defaults {:noun 15
               :adjective 15
               :verb 15
               :adverb 15})

(defn grab-bag []
  (let [classes [:noun :adjective :verb :adverb]]
    (reduce (fn [bag class]
              (assoc bag class
                     (map (class dictionary)
                          (take (class defaults)
                                (shuffle (range (count (class dictionary))))))))
            {}
            classes)))
