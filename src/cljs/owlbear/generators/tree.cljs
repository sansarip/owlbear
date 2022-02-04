(ns owlbear.generators.tree
  (:require [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]
            [clojure.walk :as w]
            [oops.core :refer [oget oset!]]
            [owlbear.generators.utilities :as obgu]))

(defn partition*
  "Generates a partitioned value from the given generator"
  [g]
  (gen/let [partitionable g
            num-partitions (gen/choose 1 (count partitionable))]
    (if (= (count partitionable) 1)
      partitionable
      (partition num-partitions partitionable))))

(def pos-int-tuple-tree
  "Generates a tree, recursively, with whole number tuples as leaf nodes
   
   e.g. 
   ```clojure
   '(([0 1] [2 3]) ([6 11] [21 25]) ([45 999] [1421 2283]) ([6566 7748] [17369 21903])) 
   ```"
  (->> (obgu/vector-distinct-sorted obgu/large-pos-integer {:min-elements 2})
       (gen/fmap #(map vec (partition 2 %)))
       (gen/recursive-gen partition*)))

(def root-node
  "Generates a root node (tree), containing child nodes with proper start and stop indices. 
   Generated nested parent nodes will not contain recursive references to children.
   
   e.g.
   ```clojure
   #js {:startIndex 0
        :endIndex 10
        :children '(#js {:startIndex 0
                         :endIndex 5
                         :parent #js {:startIndex 0
                                      :endIndex 10}}
                    #js {:startIndex 5
                         :endIndex 10
                         :parent #js {:startIndex 0
                                      :endIndex 10}})}
   ```"
  (gen/fmap
   (fn [t]
     (w/postwalk
      (fn [n]
        (cond
          (s/valid? (s/tuple int? int?) n) (let [[start stop] n]
                                             #js {:startIndex start
                                                  :endIndex stop})
          (seq? n) (let [[first-child :as children] n
                         start (oget first-child :?startIndex)
                         stop (oget (last children) :?endIndex)
                         parent #js {:startIndex start
                                     :endIndex stop}]
                     (doseq [child children]
                       (oset! child :!parent parent))
                     #js {:startIndex start
                          :endIndex stop
                          :children children})
          :else n))
      t))
   pos-int-tuple-tree))

