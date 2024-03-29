---
title: Caveats
layout: default
nav_order: 3
---

# Caveats

Owlbear's implementation of Paredit focuses specifically on tagged entities like JSX and HTML elements. And while it tries to support the surrounding languages, there are many instances where Paredit will not work as expected. The following is a list of known caveats.

* Owlbear will not work on the contents of `script` and `style` tags. This is a limitation of the underlying [HTML Tree-sitter grammar](https://github.com/sansarip/tree-sitter-html) that Owlbear relies on.
* HTML template syntax is not supported e.g. [Liquid](https://shopify.github.io/liquid/).
* Owlbear will often not work as expected if there are syntax errors in the code. This is especially noticeable when using Owlbear on TypeScript/JavaScript code (outside of JSX elements). Improvements can be made on this front by "loosening" the rules of the underlying [TS/JS grammars](https://github.com/sansarip/tree-sitter-javascript), but it will probably never be perfect.