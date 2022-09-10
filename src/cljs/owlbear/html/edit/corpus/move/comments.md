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

## Superfluous Whitespace
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

# HTML Backward Move
## Root Level
```html
<!-- Beep, boop! -->
▌<body></body>
```
```html
▌<!-- Beep, boop! -->
<body></body>
```

## Superfluous Whitespace
```html
<!-- Beep, boop! -->

▌
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
▌<!-- Zip, zoop! -->


<!-- Beep, boop! -->
```

## Child Comments
```html
<body>
  <!-- Beep, boop! -->
  ▌<h1>Hello, World!</h1>
</body>
```
```html
<body>
  ▌<!-- Beep, boop! -->
  <h1>Hello, World!</h1>
</body>
```

```html
<body>
  <!-- Beep, boop! -->
  ▌
</body>
```
```html
<body>
  ▌<!-- Beep, boop! -->
  
</body>
```

# HTML Downward Move
## Root Level
```html
▌<!-- Hello, World! -->
```
```html
<!-- ▌Hello, World! -->
```

## Superfluous Whitespace
```html
▌

<!-- Beep, boop! -->
```
```html
❎
```

```html
<!-- ▌Zip, zoop! -->
```
```html
❎
```
