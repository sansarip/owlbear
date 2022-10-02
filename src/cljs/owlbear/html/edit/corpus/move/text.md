# HTML Forward Move
## Superfluous Whitespace
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

# HTML Backward Move
## Superfluous Whitespace
```html
<div>
  Hello, World!
  <p>The name is Zabip!</p>
  ▌
</div>
```
```html
<div>
  Hello, World!
  ▌<p>The name is Zabip!</p>
  
</div>
```

## Text to Element
```html
<div>
  <p>The Name is Zabip!</p>
  ▌Hello, World!
</div>
```
```html
<div>
  ▌<p>The Name is Zabip!</p>
  Hello, World!
</div>
```

## Element to Text
```html
<div>
  The name is Zabip!
  ▌<h1>Hello, World!</h1>
</div>
```
```html
<div>
  ▌The name is Zabip!
  <h1>Hello, World!</h1>
</div>
```

# HTML Downward Move
## No Children
```html
<div>
  ▌The name is Zabip!
</div>
```
```html
❎
```

# HTML Upward Move
## No Children
```html
<div>
  ▌The name is Zabip!
</div>
```
```html
▌<div>
  The name is Zabip!
</div>
```