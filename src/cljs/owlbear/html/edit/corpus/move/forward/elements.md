# HTML Forward Move
## Root Level
```html
▌<header></header>
<body></body>
```
```html
<header></header>
▌<body></body>
```

```html
<header>▌</header>
<body></body>
```
```html
<header></header>
▌<body></body>
```

## Superficial Whitespace
```html
▌

<header></header>
<body></body>
```
```html


▌<header></header>
<body></body>
```

```html
<header></header>
▌
<body></body>
```
```html
<header></header>

▌<body></body>
```

## Child Elements
```html
<body>
  ▌<h1>Hello, World!</h1>
  <p>The name is Zabip!</p>
</body>
```
```html
<body>
  <h1>Hello, World!</h1>
  ▌<p>The name is Zabip!</p>
</body>
```

```html
<body>
 ▌ <h1>Hello, World!</h1>
  <p>The name is Zabip!</p>
</body>
```
```html
<body>
  ▌<h1>Hello, World!</h1>
  <p>The name is Zabip!</p>
</body>
```