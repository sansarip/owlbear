# HTML Forward Move
## Root Level
```html
▌<!-- Beep, boop! -->
<body></body>
```
```html
<!-- Beep, boop! -->
▌<body></body>
```

## Superficial Whitespace
```html
▌

<!-- Beep, boop! -->
```
```html


▌<!-- Beep, boop! -->
```

```html
<!-- Zip, zoop! -->
▌

<!-- Beep, boop! -->
```
```html
<!-- Zip, zoop! -->


▌<!-- Beep, boop! -->
```

## Child Comments
```html
<body>
  ▌<!-- Beep, boop! -->
  <h1>Hello, World!</h1>
</body>
```
```html
<body>
  <!-- Beep, boop! -->
  ▌<h1>Hello, World!</h1>
</body>
```

```html
<body>
  ▌
  <!-- Beep, boop! -->
</body>
```
```html
<body>
  
  ▌<!-- Beep, boop! -->
</body>
```
