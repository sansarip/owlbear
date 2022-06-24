# HTML Forward Move
## Superficial Whitespace
```html
<div>
  ▌
  Hello, World!
  <p>The name is Zabip!</p>
</div>
```
```html
<div>
  
  ▌Hello, World!
  <p>The name is Zabip!</p>
</div>
```

## Text to Element
```html
<div>
  ▌Hello, World!
  <p>The Name is Zabip!</p>
</div>
```
```html
<div>
  Hello, World!
  ▌<p>The Name is Zabip!</p>
</div>
```

## Element to Text
```html
<div>
  ▌<h1>Hello, World!</h1>
  The name is Zabip!
</div>
```
```html
<div>
  <h1>Hello, World!</h1>
  ▌The name is Zabip!
</div>
```