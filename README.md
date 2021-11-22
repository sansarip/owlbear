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
5. Select `:lib`
6. Open a new terminal and run `node -e "require('./out/cljs-lib/owlbear.js')"`
7. Voila! Your code will now be re-compiled after every change. Try evaluating some code!

[This video](https://i.gyazo.com/8ff378ec542fc9a76410fb5b936c2773.mp4) is a demonstration of the steps above.
