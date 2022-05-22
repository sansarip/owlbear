(ns owlbear.corpus-test
  "Defines all of the corpus tests"
  (:require [clojure.test]
            [owlbear.corpus :refer-macros [deftests watch-corpus-files]]
            [owlbear.ts.edit.barf]
            [owlbear.ts.edit.slurp]
            [owlbear.utilities]
            [shadow.resource]))

(deftests)
(watch-corpus-files)