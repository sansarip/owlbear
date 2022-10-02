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

## Superfluous Whitespace
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

# HTML Backward Move
## Root Level
```html
<header></header>
▌<body></body>
```
```html
▌<header></header>
<body></body>
```

```html
<header></header>
<body>▌</body>
```
```html
▌<header></header>
<body></body>
```

## Superfluous Whitespace
```html
<header></header>
<body></body>

▌
```
```html
<header></header>
▌<body></body>


```

```html
<header></header>
▌
<body></body>
```
```html
▌<header></header>

<body></body>
```

## Child Elements
```html
<body>
  <h1>Hello, World!</h1>
  ▌<p>The name is Zabip!</p>
</body>
```
```html
<body>
  ▌<h1>Hello, World!</h1>
  <p>The name is Zabip!</p>
</body>
```

```html
<body>
  <h1>Hello, World!</h1> ▌
  <p>The name is Zabip!</p>
</body>
```
```html
<body>
  ▌<h1>Hello, World!</h1> 
  <p>The name is Zabip!</p>
</body>
```

# HTML Downward Move
## No Children
```typescript
<h1>▌</h1>
```
```typescript
❎
```

## Superfluous Whitespace
```html
▌

<header></header>
<body></body>
```
```html
❎
```

## Child Elements
```html
<▌body>
  <h1>Hello, World!</h1>
</body>
```
```html
<body>
  ▌<h1>Hello, World!</h1>
</body>
```

```html
<body>
  ▌<h1>Hello, World!</h1>
</body>
```
```html
<body>
  <h1>▌Hello, World!</h1>
</body>
```

# HTML Upward Move
## No Parent
```typescript
▌<h1></h1>
```
```typescript
❎
```

## Superfluous Whitespace
```html
▌

<header></header>
<body></body>
```
```html
❎
```

```html
<header>
  <body>
  ▌
  </body>
</header>
```
```html
▌<header>
  <body>
  
  </body>
</header>
```

## Child Elements
```html
<body>
  ▌<h1>Hello, World!</h1>
</body>
```
```html
▌<body>
  <h1>Hello, World!</h1>
</body>
```