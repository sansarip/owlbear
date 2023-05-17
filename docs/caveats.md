---
title: Caveats
layout: default
nav_order: 3
---

# Caveats

Owlbear's implementation of Paredit focuses specifically on tagged entities like JSX and HTML elements. And, while it tries to support the surrounding languages, there are many instances where Paredit will not work as expected. These are not bugs but a limitation of the Paredit implementation. The following is a list of known caveats.

* Paredit will not work as expected inside of HTML script tags or style tags. In other words, paredit will work just fine on the script/style tags themselves, but not on their contents. This is a limitation of the underlying [HTML Tree-sitter grammar](https://github.com/sansarip/tree-sitter-html/tree/38606d4a279e7f019994d20963ed89aa9952368a) that Owlbear relies on.
* Paredit in JS/TS/HTML is not as robust as paredit in Lisps. This due to the intrinsic syntactic complexity of said languages —compared to syntactically simple Lisps. In other words, it's much easier to get into a syntactically erroneous state in said languages than in Lisps, and Owlbear will often not work as expected if there are syntax errors in the code.