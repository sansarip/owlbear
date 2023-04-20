#_{:clj-kondo/ignore [:unused-namespace]}
(ns owlbear.corpus
  "The place where the CLJS tests for the project's corpus files are manufactured 🏭"
  (:require #?@(:cljs [[directory-tree :as dirt]
                       [fs]
                       [owlbear.parse :as obp]
                       [owlbear.parse.rules :as obpr]]
                :clj [[clojure.edn :as edn]
                      [clojure.java.shell :refer [sh]]])
            [clojure.string :as str]
            [clojure.test :refer [deftest do-report testing]]
            [owlbear.config :as cfg]
            [owlbear.utilities :as obu]))

(defn with-tsx [sym]
  (fn [& args]
    (concat (list sym) args '(nil :tsx))))

(def corpus-title-to-function-map
  "Map of corpus h1-header to higher-order functions that return 
   s-expressions of the Owlbear functions to be tested; 
   update this whenever a new test-suite (h1-header) is added"
  {"HTML Backward Delete" (partial list 'owlbear.html.edit.delete/backward-delete)
   "HTML Backward Move" (partial list 'owlbear.html.edit.move/backward-move)
   "HTML Downward Move" (partial list 'owlbear.html.edit.move/downward-move)
   "HTML Forward Delete" (partial list 'owlbear.html.edit.delete/forward-delete)
   "HTML Forward Move" (partial list 'owlbear.html.edit.move/forward-move)
   "HTML Upward Move" (partial list 'owlbear.html.edit.move/upward-move)
   "TypeScript Backward Delete" (with-tsx 'owlbear.ts.edit.delete/backward-delete)
   "TypeScript Backward Move" (with-tsx 'owlbear.ts.edit.move/backward-move)
   "TypeScript Downward Move" (with-tsx 'owlbear.ts.edit.move/downward-move)
   "TypeScript Forward Slurp" (with-tsx 'owlbear.ts.edit.slurp/forward-slurp)
   "TypeScript Forward Barf" (with-tsx 'owlbear.ts.edit.barf/forward-barf)
   "TypeScript Forward Delete" (with-tsx 'owlbear.ts.edit.delete/forward-delete)
   "TypeScript Forward Move" (with-tsx 'owlbear.ts.edit.move/forward-move)
   "TypeScript Kill" (with-tsx 'owlbear.ts.edit.kill/kill)
   "TypeScript Raise" (with-tsx 'owlbear.ts.edit.raise/raise)
   "TypeScript Upward Move" (with-tsx 'owlbear.ts.edit.move/upward-move)})

(defn src->cursor-ctx
  "Given a source string, `src`, 
   returns a map of the source string without the cursor, 
   `:src-without-cursor-symbol`, and the cursor offset, 
   `:cursor-offset`
   
   Optionally accepts a string representing the cursor, 
   `cursor-str`, defaults to \"▌\""
  ([src]
   (src->cursor-ctx src "▌"))
  ([src cursor-str]
   (let [cursor-start-offset (str/index-of src cursor-str)
         cursor-length (count cursor-str)
         cursor-stop-offset (+ cursor-start-offset cursor-length)]
     {:src-without-cursor (obu/str-remove src cursor-start-offset cursor-stop-offset)
      :offset cursor-start-offset})))

(defn assertion->is-exp
  "Given a map containing an `input` and `output`, 
   and a function that returns an s-expression containing the test function, `test-s-exp`, 
   returns a correspinding test assertion s-expression"
  [{:keys [input output]} test-s-exp]
  (let [{:keys [src-without-cursor offset]} (src->cursor-ctx input)
        result-s-exp (test-s-exp src-without-cursor offset)
        print-input-info-s-exp `(do (println "   input: " (pr-str ~input))
                                    (println "  offset: " ~offset))
        insert-cursor (fn [r]
                        `(let [{:keys [~'src ~'offset]} ~r]
                           (obu/str-insert (str (when (map? ~r)
                                                  (if (contains? ~r :src)
                                                    ~'src
                                                    ~src-without-cursor)))
                                           "▌"
                                           ~'offset)))
        pass-s-exp `(do-report {:type :pass})
        negative-test? (= output "❎")]
    (if negative-test?
      `(let [~'result ~result-s-exp]
         (if (nil? ~'result)
           ~pass-s-exp
           (do (do-report {:type :fail
                           :expected nil
                           :actual ~(insert-cursor 'result)})
               ~print-input-info-s-exp)))
      `(let [{:keys [~'src ~'offset] :as ~'result} ~result-s-exp
             ~'actual ~(insert-cursor 'result)
             ~'difference-str ~`(obu/str-diff ~output ~'actual)]
         (if (empty? ~'difference-str)
           ~pass-s-exp
           (do (do-report {:type :fail
                           :expected ~output
                           :actual ~'actual})
               (println "    diff: " ~'difference-str)
               ~print-input-info-s-exp))))))

(defn section->testing-exp
  "Given a map containing a `description` and `assertions`, 
   and a function that returns an s-expression containing the test function, `test-s-exp`, 
   returns a corresponding `testing` s-expression"
  [{:keys [description assertions]} test-s-exp]
  (concat (list `testing description)
          (map #(assertion->is-exp % test-s-exp) assertions)))

(defn combine-sections
  "Given a map containing a `title` and `sections`, 
   returns a map with all of the sections concatenated 
   as one collection"
  [[{:keys [title]} :as same-tests]]
  {:title title
   :sections (mapcat :sections same-tests)})

(defn test->deftest-exp
  "Given a map containing a `title` and `sections`, 
   returns a corresponding `deftest` s-expression"
  [{:keys [title sections]}]
  (let [test-name (-> title
                      obu/string->snake-case
                      (str "-test")
                      symbol)
        test-s-exp (corpus-title-to-function-map title)]
    (concat `(deftest ~test-name)
            (map #(section->testing-exp % test-s-exp) sections))))

#?(:clj
   (defn parse-corpus-files
     []
     #_{:clj-kondo/ignore [:unresolved-symbol]}
     (some->> cfg/source-paths
              (apply sh "node" cfg/corpus-parser-script-path)
              :out
              edn/read-string)))

#?(:clj
   (defmacro deftests
     "Parses all corpus files and emits the corresponding test code"
     []
     (some->> (parse-corpus-files)
              (group-by :title)
              (into [] (map (comp combine-sections second)))
              (map test->deftest-exp)
              (concat '(do)))))

#?(:clj
   (defmacro watch-corpus-files
     "Emits code that informs shadow-cljs to watch the corpus files for changes"
     []
     (let [corpus-file-paths (some->> (parse-corpus-files)
                                      (group-by :file-path)
                                      keys)]
       `(do ~@(map (comp #(list 'shadow.resource/inline %)
                         #(str/replace-first % "src/cljs/owlbear/" "./"))
                   corpus-file-paths)))))

#?(:cljs
   (defn header-node
     "Given a `node`, 
      returns the node if it is a header node
      
      Optionally accepts acceptable header `levels`"
     ([node] (header-node node #{1 2}))
     ([node levels]
      {:pre [(not-empty levels)]}
      (when (and (= "atx_heading" (obu/noget+ node :?type))
                 ((set (map #(str "atx_h" % "_marker") levels))
                  (obu/noget+ node :?children.?0.?type)))
        node))))

#?(:cljs
   (defn header-node-content
     "Given a header `node`, 
     returns the textual contents of the header"
     [node]
     (some-> (header-node node)
             (obu/noget+ :?children.?1.?children.?0.?text)
             (str/trim))))

#?(:cljs
   (defn fenced-code-content
     "Given a `node`, 
      returns the node if it is a fenced-code node"
     [node]
     (obu/noget+ node :?children.?1.?text)))

#?(:cljs
   (defn test-node
     "Given a `node`, 
      returns the node if it is vital to the generation of test code"
     [node]
     (when (#{"atx_heading" "fenced_code_block"} (obu/noget+ node :?type))
       node)))

#?(:cljs
   (defn corpus-file-paths
     "Given a collection of directory paths, `dir-paths`, 
      recursively crawls the given paths, 
      and returns all corpus files in those paths"
     [dir-paths]
     {:pre [(not-empty dir-paths)]}
     #_{:clj-kondo/ignore [:unresolved-symbol]}
     (some->> dir-paths
              (map (comp obpr/node->descendants #(dirt %)))
              flatten
              (filter (comp #(str/ends-with? % ".md") #(obu/noget+ % :?name)))
              (map #(obu/noget+ % :?path)))))

#?(:cljs
   (defn parse-corpus-files
     "Given a collection of directory paths, `dir-paths`, 
      recursively crawls the given paths, 
      parses all corpus files, 
      and returns the test-useful, parsed contents of the corpus files
      
      e.g. 
      ```clojure
      {:title \"TypeScript Forward Slurp\"
       :file-path \"src/cljs/owlbear/ts/edit/slurp_test.md\"
       :sections '({:description \"Simple JSX\"
                    :assertions '({:input \"...\"
                                   :output \"...\"})})}
      ```"
     [dir-paths]
     {:pre [(not-empty dir-paths)]}
     (let [test-nodes->assertions (fn [nodes]
                                    (map (fn [[k v]]
                                           {:input (fenced-code-content k)
                                            :output (fenced-code-content v)})
                                         (partition 2 nodes)))
           test-nodes->sections (fn [nodes]
                                  (->> nodes
                                       (partition-by #(header-node % [2]))
                                       (partition 2)
                                       (map (fn [[[k] v]]
                                              {:description (header-node-content k)
                                               :assertions (test-nodes->assertions v)}))))
           test-nodes->test-blocks (fn [tuples]
                                     (map (fn [[nodes file-path]]
                                            (->> nodes
                                                 (partition-by #(header-node % [1]))
                                                 (partition 2)
                                                 (map (fn [[[k] v]]
                                                        {:title (header-node-content k)
                                                         :file-path file-path
                                                         :sections (test-nodes->sections v)}))))
                                          tuples))]
       (some->> (corpus-file-paths dir-paths)
                (map #(some-> %
                              (fs/readFileSync "utf8")
                              (obp/src->tree! :markdown)
                              (obu/noget+ :?rootNode)
                              obpr/node->descendants
                              (->> (filter test-node))
                              (vector %)))
                test-nodes->test-blocks
                flatten))))

#?(:cljs
   (defn parse-corpus-files-cli
     "Command-line-facing function for parsing the corpus files in the given directory paths"
     [& dir-paths]
     (.then (obp/load-language-wasm! :markdown "resources/tree-sitter-markdown.wasm")
            #(-> dir-paths
                 parse-corpus-files
                 pr-str
                 println))))