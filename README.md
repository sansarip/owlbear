<a href="https://gitmoji.dev">
  <img src="https://img.shields.io/badge/gitmoji-%20😜%20😍-FFDD67.svg?style=flat-square" alt="Gitmoji">
</a>
<img src="https://user-images.githubusercontent.com/12676521/164160624-a6dc70ed-e35f-4570-890e-391248f922c4.svg" alt="Owlbear Logo" title="Owlbear" align="right" width="250px" />

# Owlbear (👷 WIP)

A Visual Studio Code extension to support paredit-like features for HTML and TS!

## Contributing

For info related to developing on Owlbear code, see below! 👇🧐

**Don't forget to `npm install` 💡**

### Clojure

Much of this extension is written in ClojureScript code. This section will detail how to get a running ClojureScript REPL in which said Cljs code can be loaded and played with.

Make sure you're using VS Code and have the [Calva](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva) extension installed.

To start a ClojureScript REPL:

1. `cmd + shift + p`
2. search for `Calva: Start a Project REPL and Connect...`
3. Select the aforementioned command
4. Select `shadow-cljs`
5. Select `:lib` checkbox [and the `:test` checkbox if you want the tests to autorun on save] and then click OK
6. Once the CLJS files have finished compiling, open a new terminal and run `npm run cljs-repl`
7. Voila! Your code will now be re-compiled after every change. Try evaluating some code! 
    1. You can hop into any Owlbear namespace and the use `cmd + shift + p` to find and select the `Calva: Load Current File and...` command

[This video](https://i.gyazo.com/bc970dbe5a01ac2ac9e4c113d97826dd.mp4) is a demonstration of the steps above.

### Running the Extension

To run the extension in a sandbox VS Code window:

1. Run `npm run compile-cljs`
2. Run `npm run compile`
3. Press the green play button in your VS Code editor! This should open up a new sandbox VS Code window.
4. I'd recommend opening the [playground](https://github.com/sansarip/owlbear/tree/main/playground) folder in the sandbox VS Code window, as the playground folder will have plenty of sample files to play around in!
5. Look for owlbear commands with `cmd + shift + p`

[Here's a video](https://i.gyazo.com/f7026c97810db2a62e0b469343cd2f7f.mp4) demonstrating the steps above!
