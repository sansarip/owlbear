# Owlbear (👷 WIP)

A Visual Studio Code extension to support paredit-like features for HTML/JSX

## Dev

### Clojure

Some parts of this extension will involve ClojureScript code. This section will detail how to get a running ClojureScript REPL in which said Cljs code can be loaded and played with.

Make sure you're using VS Code and have the [Calva](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva) extension installed.

To start a ClojureScript REPL:

1. `cmd + shift + p`
2. search for `Calva: Start a Project REPL and Connect...`
3. Select the aforementioned command
4. Select `shadow-cljs`
5. Select `:lib` checkbox then OK
6. Open a new terminal and run `npm run cljs-repl`
7. Voila! Your code will now be re-compiled after every change. Try evaluating some code!

[This video](https://i.gyazo.com/8ff378ec542fc9a76410fb5b936c2773.mp4) is a demonstration of the steps above.

### Antlr4

If you need to generate new lexer/parser files, then you can do so easily with the [Antlr4 VS Code extension](https://marketplace.visualstudio.com/items?itemName=mike-lischke.vscode-antlr4).

You'll also need to edit your VS Code `settings.json` file with the proper Antlr4 extension settings e.g.

```json
  "antlr4": {
    "generation": {
      "mode": "external",
      "language": "JavaScript",
      "listeners": false,
      "visitors": false,
      "importDir": "<absolute path to owlbear>/owlbear/antlr4",
      "outputDir": "./module/src/gen"
    }
```

The above config will look for grammar files in th `antlr4` dir and generate lexer/parser JS files in the `antlr4/module` dir. The aformentioned module dir is responsible for the [owlbear-grammar NPM module](https://www.npmjs.com/package/owlbear-grammar) that the Owlbear extension consumes.
