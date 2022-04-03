(ns owlbear.generators.utilities
  (:require [clojure.string :as str]
            [clojure.test.check.generators :as gen]
            [owlbear.utilities :as obu]))

(def large-pos-integer (gen/fmap obu/abs gen/large-integer))

(def small-pos-integer (gen/fmap obu/abs gen/small-integer))

(def small-pos-range
  "Generates a range of small positive integers"
  (gen/let [start (gen/fmap obu/abs gen/small-integer)
            addend (gen/fmap (comp inc obu/abs) gen/small-integer)]
    (range start (+ start addend))))

(def large-pos-range
  "Generates a range of large positive integers"
  (gen/let [start (gen/fmap obu/abs gen/large-integer)
            addend (gen/fmap (comp inc obu/abs) gen/large-integer)]
    (range start (+ start addend))))

(defn subvec*
  "Generates a subset vector of a given vector"
  [vec*]
  (let [max-index (dec (count vec*))]
    (gen/let [start-index (gen/choose 0 max-index)
              addend (gen/choose 0 (- max-index start-index))]
      (subvec vec* start-index (inc (+ start-index addend))))))

(defn vector-distinct-sorted
  "Generates a sorted vector of distinct values"
  ([g]
   (vector-distinct-sorted g {}))
  ([g opts]
   (let [opts* (merge {:max-tries 20} opts)] ; vector-distinct has a tendency to fail with only 10 default max tries
     (gen/fmap (comp vec sort) (gen/vector-distinct g opts*)))))

(defn vector-sorted
  "Generates a sorted vector"
  [g & opts]
  (gen/fmap (comp vec sort) (apply gen/vector g opts)))

(defn prefix-alpha
  "Prefixes the value generated by the given generator
   with an alpha character"
  [g]
  (gen/let [v g
            start-char gen/char-alpha]
    (str start-char v)))

(def string-alphanumeric-starts-with-alpha
  "Generate an alpha-numeric string that starts with an alpha character"
  (prefix-alpha gen/string-alphanumeric))

(defn string-from-elements
  "Generates a string from a given list of `elements`"
  [elements]
  (->> elements
       gen/elements
       gen/vector
       gen/not-empty
       (gen/fmap str/join)))

(defn string-from-generators
  "Generates a string from a given list of `elements`"
  [generators]
  (->> generators
       gen/one-of
       gen/vector
       gen/not-empty
       (gen/fmap str/join)))

(def hex-string
  (gen/fmap #(str "0x" (.toString % 16)) small-pos-integer))

(def primative
  (gen/one-of [hex-string
               gen/small-integer
               string-alphanumeric-starts-with-alpha]))

(defn s-expression
  ([root-gen child-gen]
   (s-expression root-gen child-gen {}))
  ([root-gen child-gen {:keys [vector-gen-args]
                        :or {vector-gen-args [2 4]}}]
   (gen/fmap (fn [[root children]]
               (concat (list root) children))
             (gen/tuple
              root-gen
              (apply gen/vector child-gen vector-gen-args)))))

(defn ast
  [& [root-gen child-gen {:keys [s-expression-opts]}]]
  {:pre [(gen/generator? root-gen) (gen/generator? child-gen)]}
  (gen/recursive-gen
   #(apply s-expression root-gen % s-expression-opts)
   (s-expression root-gen child-gen s-expression-opts)))

(def noop (gen/Generator. (fn [& _])))

(defn with-function-gen
  ([fn-g] 
   (with-function-gen fn-g nil))
  ([fn-g default-value]
   (gen/recursive-gen
    (fn [_] (fn-g))
    (gen/return default-value))))