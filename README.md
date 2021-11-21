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

The below GIF is a demonstration of the steps above.

[![Image from Gyazo](https://i.gyazo.com/e5a8d87e1ba238912ab7dfdb21525814.gif)](https://gyazo.com/e5a8d87e1ba238912ab7dfdb21525814)
