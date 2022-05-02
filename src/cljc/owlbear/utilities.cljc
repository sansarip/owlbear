(ns owlbear.utilities
  "Universal utilities"
  (:require #?@(:cljs [[Diff]
                       [oops.core :refer [ocall oget+]]])
            [clojure.string :as str]))

(defn str-insert
  "Insert c in string s at the given offset"
  [s c offset]
  (str (subs s 0 offset) c (subs s offset)))

(defn str-remove
  "Remove the string in between the given start and end offsets"
  ([s start-offset]
   (str-remove s start-offset (inc start-offset)))
  ([s start-offset end-offset]
   (str (subs s 0 start-offset) (subs s end-offset))))

(defn call-fn
  "Given that the argument, `f`, is a function, 
   returns `(f)`, else returns `f`"
  [f]
  (cond-> f
    (fn? f) (#(%))))

(defn string->snake-case
  "Converts a string to lower-case, snake-case 
   e.g. \"Hello World\" 👉 \"hello-world\""
  [s]
  (str/replace (str/lower-case s) " " "-"))

(defmacro &testing
  "Same as a normal testing block, 
   but it fails if one assertion fails, 
   essentially wrapping the given assertions 
   in an and form
   
   Meant for usage from cljs"
  [s & assertions]
  ;; Using clojure.test results in warning 👉 clojure.test/*testing-contexts* not declared ^:dynamic
  `(~'cljs.test/testing ~s (boolean (and ~@assertions))))

#?(:cljs
   (defn abs
     "js/Math.abs"
     ([num]
      (js/Math.abs num))))

#?(:cljs
   (defn noget+
     "Attempts oops.core/oget+, 
      defaults the obj if the obj is falsey"
     [obj args]
     #_{:clj-kondo/ignore [:unresolved-symbol]}
     (oget+ (or obj #js {}) args)))

#?(:cljs
   (defn str-diff
     "Given two strings, `str-1` and `str-2`, 
      returns the commonalities/differences of `str-2` from `str-1` 
   
      White text indicates commonalities, 
      green text indicates missing but expected text, 
      and red text indicates added but unexpected text"
     [str-1 str-2]
     #_{:clj-kondo/ignore [:unresolved-symbol]}
     (let [diff (ocall Diff :diffChars str-1 str-2)]
       (if (> (noget+ diff :length) 1)
         (some-> diff
                 (ocall :map
                        (fn [part]
                          (let [color-code (cond (noget+ part :?added) "\u001b[31m"
                                                 (noget+ part :?removed) "\u001b[32m"
                                                 :else "\u001b[37m")
                                reset-color-code "\u001b[0m"]
                            (-> part
                                (noget+ :?value)
                                (as-> $ (str color-code $ reset-color-code))))))
                 (ocall :join ""))
         ""))))