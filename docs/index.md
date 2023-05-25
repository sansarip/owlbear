---
title: Getting started
layout: default
nav_order: 0
---

# 👋 Welcome

Owlbear is a free VS Code extension aiming to bring the [paredit structural editing](https://calva.io/paredit/) paradigm, commonly enjoyed when working with Lisps, to the mainstream!

Tired of grappling with JSX/HTML tags? Take a look at the video below! Owlbear makes it easier to edit JSX and HTML elements. See the [visual guide](paredit.html) for more details on the tooling Owlbear provides.

<div class="filter-shadow" style="position: relative; padding-bottom: 62.5%; height: 0; border-radius: 6px"><iframe src="https://www.loom.com/embed/b71e758ed9494546aa172acb08f95b87" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen style="position: absolute; top: 0; left: 0; width: 100%; height: 100%;"></iframe></div>

## Install Owlbear

Install the extension by searching for `Owlbear` in the [Visual Studio Code Extension Pane](https://code.visualstudio.com/docs/editor/extension-marketplace), then click **install**.

It's also recommended that you install the [Auto Rename Tag](https://marketplace.visualstudio.com/items?itemName=formulahendry.auto-rename-tag) extension as it pairs nicely with Owlbear.

You can also try Owlbear without installing it, in Gitpod!

<a target="_blank" href="https://gitpod.io/#https://github.com/sansarip/owlbear">
  <img class="filter-shadow" src="assets/images/gitpod-button.svg"/>
</a>

## How does it work?

Owlbear uses a mixture of ClojureScipt, TypeScript, and WebAssembly (thanks to [Tree-sitter](https://tree-sitter.github.io/tree-sitter/)) to parse and edit code. Everything Owlbear does is client-side! None of your data leaves your machine when using Owlbear, and Owlbear works totally fine offline. All of Owlbear's code is available on [Github](https://github.com/sansarip/owlbear).

<br>

{: .note }
> This extension was heavily inspired by [Calva](https://calva.io/) –a powerful VS Code extension for working with Clojure and ClojureScript ️❤️