(ns owlbear.corpus-test
  "Defines all of the corpus tests"
  (:require [clojure.test]
            [owlbear.corpus :refer-macros [deftests watch-corpus-files]]
            [owlbear.html.edit.move]
            [owlbear.ts.edit.barf]
            [owlbear.ts.edit.kill]
            [owlbear.ts.edit.raise]
            [owlbear.ts.edit.slurp]
            [owlbear.utilities]
            [shadow.resource]))

(deftests)
(watch-corpus-files)