---
title: Getting started
layout: default
nav_order: 0
---

# 👋 Welcome

Owlbear is a free VS Code extension aiming to bring the [paredit-like structural editing](https://calva.io/paredit/) paradigm, commonly enjoyed when working with Lisps, to the mainstream! In other words, Owlbear mainly makes it easier to edit JSX and HTML elements, but it also supports the greater TypeScript and JavaScript languages.

Try Owlbear without installing it in Gitpod!

<a target="_blank" href="https://gitpod.io/#https://github.com/sansarip/owlbear">
  <img src="assets/images/gitpod-button.svg"/>
</a>

## Installation

Install the extension by searching for `Owlbear` in the [Visual Studio Code Extension Pane](https://code.visualstudio.com/docs/editor/extension-marketplace), then click **install**.

It's also recommended that you install the [Auto Rename Tag](https://marketplace.visualstudio.com/items?itemName=formulahendry.auto-rename-tag) extension as it *pairs* nicely with Owlbear.

## How does it work?

Owlbear uses a mixture of ClojureScipt, TypeScript, and WebAssembly (thanks to [Tree-sitter](https://tree-sitter.github.io/tree-sitter/)) to parse and edit code. Everything Owlbear does is client-side, meaning none of your data leaves your machine when using Owlbear, and Owlbear works totally fine offline. All of the code is available on [Github](https://github.com/sansarip/owlbear).

<br>

{: .note }
> This extension was heavily inspired by [Calva](https://calva.io/) –a powerful VS Code extension for working with Clojure and ClojureScript ️❤️!