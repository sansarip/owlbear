(ns owlbear.corpus-test
  "Defines all of the corpus tests"
  (:require [clojure.test]
            [owlbear.corpus :refer-macros [deftests watch-corpus-files]]
            [owlbear.html.edit.move]
            [owlbear.html.edit.delete]
            [owlbear.html.edit.splice]
            [owlbear.ts.edit.barf]
            [owlbear.ts.edit.delete]
            [owlbear.ts.edit.kill]
            [owlbear.ts.edit.move]
            [owlbear.ts.edit.raise]
            [owlbear.ts.edit.slurp]
            [owlbear.ts.edit.splice]
            [owlbear.utilities]
            [shadow.resource]))

(deftests)
(watch-corpus-files)