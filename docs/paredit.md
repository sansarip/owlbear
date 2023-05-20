---
title: A Visual Guide to Paredit
layout: default
nav_order: 1
---

{: .no_toc}
# A Visual Guide to Paredit

{: .no_toc }
## What is Paredit?

ParEdit is a portmanteau of "parenthesis" and "editing". Paredit [originated in the Lisp community](https://www.emacswiki.org/emacs/ParEdit), but it's not limited to Lisps. It's a powerful way of structurally editing code that is often overlooked by mainstream editors.

{: .no_toc }
## Commands

1. TOC
{:toc}

{: .no_toc }
### Editing

The most commonly used commands are <a href="#slurp-forward">Slurp Forward</a> and <a href="#barf-forward">Barf Forward</a>
–used for editing code. If you are new to Paredit, consider starting with them!

{: .invisible}
#### Slurp Forward

<dl class="table-wrapper">
  <a href="#slurp-forward"><h4>🧃⏩ Slurp Forward</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>ctrl+alt+right</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Move the closing node forward, away from the cursor, past the following node, if any.</span> 
    <details open>
      <summary>📼</summary>
      <video class="example" src="assets/examples/tsx-slurp-forward-20230507-00.mp4" controls="controls" autoplay loop muted/>
    </details>
  </dd>
</dl>

{: .invisible}
#### Barf Forward

<dl class="table-wrapper">
  <a href="#barf-forward"><h4>🤮⏩ Barf Forward</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>ctrl+alt+left</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Move the closing node backward, toward the cursor, past the preceding form.</span> 
    <details open>
      <summary>📼</summary>
      <video class="example" src="assets/examples/tsx-barf-forward-20230507-00.mp4" controls="controls" autoplay loop muted/>
    </details>
  </dd>
</dl>

{: .invisible}
#### Splice

<dl class="table-wrapper">
  <a href="#splice"><h4>🦠 Splice</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>ctrl+alt+left</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Remove the enclosing nodes.</span> 
    <details open>
      <summary>📼</summary>
      <video class="example" src="assets/examples/tsx-splice-20230509-00.mp4" controls="controls" autoplay loop muted/>
    </details>
  </dd>
</dl>

{: .invisible}
#### Raise

<dl class="table-wrapper">
  <a href="#raise"><h4>🪜 Raise</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>ctrl+alt+p ctrl+alt+r</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Replace the enclosing node with the current node.</span> 
    <details open>
      <summary>📼</summary>
      <video class="example" src="assets/examples/tsx-raise-20230507-00.mp4" controls="controls" autoplay loop muted/>
    </details>
  </dd>
</dl>

{: .invisible}
#### Kill

<dl class="table-wrapper">
  <a href="#kill"><h4>🗡 Kill</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>ctrl+k</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Delete the current node.</span> 
    <details open>
      <summary>📼</summary>
      <video class="example" src="assets/examples/tsx-kill-20230507-00.mp4" controls="controls" autoplay loop muted/>
    </details>
  </dd>
</dl>

{: .invisible}
#### Cut

<dl class="table-wrapper">
  <a href="#cut"><h4>✂️ Cut</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>ctrl+alt+x</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Delete and copy the deleted node into the clipboard.</span> 
    <details open>
      <summary>📼</summary>
      <video class="example" src="assets/examples/tsx-kill-20230507-00.mp4" controls="controls" autoplay loop muted/>
    </details>
  </dd>
</dl>

{: .invisible}
#### Copy

<dl class="table-wrapper">
  <a href="#raise"><h4>🪞 Copy</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>ctrl+alt+c</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Copy the current node into the clipboard.</span>
  </dd>
</dl>

<p class="small-break"></p>

{: .note }

> By default, executing most edit commands will result in a formatting of the doc using whatever formatter is configured for the doc. To toggle this behavior off, execute the `Toggle Autoformat` command.

{: .no_toc }
### Strict Mode

This is a very experimental feature aiming to prevent unbalanced nodes by restricting conventional character deletion (see examples below).
By default this feature is not enabled, but it can be toggled on using the `Toggle Paredit Mode` command.

{: .invisible}
#### Delete Backward

<dl class="table-wrapper">
  <a href="#delete-backward"><h4>🔥⏪ Delete Backward</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>backspace</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Delete one character backwards, unless it will unbalance a form. Otherwise, moves past the character instead of deleting it. If the node is empty, then it is removed.</span> 
    <details open>
      <summary>📼</summary>
      <video class="example" src="assets/examples/tsx-delete-backward-20230508-00.mp4" controls="controls" autoplay loop muted/>
    </details>
  </dd>
</dl>

{: .invisible}
#### Delete Forward

<dl>
  <a href="#delete-forward"><h4>🔥⏩ Delete Forward</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>delete</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Delete one character forward, unless it will unbalance a form. Otherwise, move past the character instead of deleting it. If the node is empty, then remove it.</span> 
    <details open>
      <summary>📼</summary>
      <video class="example" src="assets/examples/tsx-delete-forward-20230508-00.mp4" controls="controls" autoplay loop muted/>
    </details>
  </dd>
</dl>

{: .no_toc}
### Navigation

These commands are used to navigate the doc.

{: .invisible}
#### Move Cursor Forward

<dl>
  <a href="#move-cursor-forward"><h4>🚟⏩ Move Cursor Forward</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>alt+right</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Move the cursor forward to the next sibling node.</span> 
    <video class="example" src="assets/examples/tsx-move-forward-20230509-00.mp4" controls="controls" autoplay loop muted/>
  </dd>
</dl>

{: .invisible}
#### Move Cursor Backward

<dl>
  <a href="#move-cursor-backward"><h4>🚟⏪ Move Cursor Backward</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>alt+left</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Move the cursor backward to the previous sibling node.</span> 
    <video class="example" src="assets/examples/tsx-move-backward-20230509-00.mp4" controls="controls" autoplay loop muted/>
  </dd>
</dl>

{: .invisible}
#### Move Cursor Up

<dl>
  <a href="#move-cursor-up"><h4>🚡⏫ Move Cursor Up</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>ctrl+up</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Move the cursor up to the parent node.</span> 
    <video class="example" src="assets/examples/tsx-move-up-20230509-00.mp4" controls="controls" autoplay loop muted/>
  </dd>
</dl>

{: .invisible}
#### Move Cursor Down

<dl>
  <a href="#move-cursor-down"><h4>🚡⏬ Move Cursor Down</h4></a>
  <dt>Keybinding</dt>
  <dd><span><code>ctrl+down</code></span></dd>
  <dt>Description</dt>
  <dd class="description">
    <span>Move the cursor down to the first child node.</span> 
    <video class="example" src="assets/examples/tsx-move-down-20230509-00.mp4" controls="controls" autoplay loop muted/>
  </dd>
</dl>