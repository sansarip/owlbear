<a href="https://gitmoji.dev">
  <img src="https://img.shields.io/badge/gitmoji-%20😜%20😍-FFDD67.svg?style=flat-square" alt="Gitmoji">
</a>
<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/sansarip/owlbear/main/assets/images/owlbear-logo-warm.png">
  <img src="https://raw.githubusercontent.com/sansarip/owlbear/main/assets/images/owlbear-logo-cool.png" alt="Owlbear Logo" title="Owlbear" width="250px" align="right">
</picture>

# Owlbear (👷 WIP)

A Visual Studio Code extension to support paredit-like features for HTML and TS!

## Installing the extension

Until official releases are created for this project, there should be a `.vsix` file in the root project folder that you can use to install Owlbear manually.

*OR*

You can build the `.vsix` file yourself! 👇

```sh
npm i
npm run compile-vsce
```

To install the `.vsix` file, simply open your VS Code's extensions view `(cmd + shift + x)` and then click the ellipsis menu at the top right of the view.

## Contributing

For info related to developing on Owlbear code, see below! 👇🧐

**Don't forget to `npm install` 💡**

### ClojureScript

Much of this extension is written in ClojureScript code. This section will detail how to get a running ClojureScript REPL in which said Cljs code can be loaded and played with.

Make sure you're using VS Code and have the [Calva](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva) extension installed.

To start a ClojureScript REPL:

1. `cmd + shift + p`
2. search for `Calva: Start a Project REPL and Connect...`
3. Select the aforementioned command
4. Select `shadow-cljs`
5. Select `:lib/dev` checkbox [and the `:test` checkbox if you want the tests to autorun on save] and then click OK
6. In the next prompt, select `:lib/dev`
7. Once the CLJS files have finished compiling, open a new terminal and run `npm run connect-repl`
8. Voila! Your code will now be re-compiled after every change. Try evaluating some code! 
    1. You can hop into any Owlbear namespace and the use `cmd + shift + p` to find and select the `Calva: Load Current File and...` command

[This video](https://i.gyazo.com/82a3343520005dbc1127be565b2c42d6.mp4) is a demonstration of the steps above.

### Running the Extension

To run the extension in a sandbox VS Code window:

1. Run `npm run compile`
2. Press the green play button in your VS Code editor! This should open up a new sandbox VS Code window.
3. I'd recommend opening the [playground](https://github.com/sansarip/owlbear/tree/main/playground) folder in the sandbox VS Code window, as the playground folder will have plenty of sample files to play around in!
4. Look for owlbear commands with `cmd + shift + p`

[Here's a video](https://i.gyazo.com/f7026c97810db2a62e0b469343cd2f7f.mp4) demonstrating the steps above!
