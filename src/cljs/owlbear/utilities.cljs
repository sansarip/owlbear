(ns owlbear.utilities)

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