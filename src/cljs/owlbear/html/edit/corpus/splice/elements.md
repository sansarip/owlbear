# HTML Splice

## Empty Elements
```html
▌<body></body>
```
```html
❎
```

```html
<body>▌</body>
```
```html
❎
```

```html
<body></body>▌
```
```html
❎
```

## Nonempty Elements
```html
<body>
	▌<section>
	  <h1>Hello, World!</h1>
	  <h2>What a time to be a bean!</h2>
	</section>
</body>
```
```html
<body>
	▌<h1>Hello, World!</h1>
	  <h2>What a time to be a bean!</h2>
</body>
```

```html
<body>
	<section>
	  <h1>Hello, World!</h1>
	  <h2>What a time to be a bean!</h2>
	</section▌>
</body>
```
```html
<body>
	<h1>Hello, World!</h1>
	  <h2>What a time to be a bean!</h2>▌
</body>
```

## Root Level
```html
▌<body>Mamma mia!</body>
```
```html
▌Mamma mia!
```

## Nested
```html
<body>
  <div>▌</div>
</body>
```
```html
<div>▌</div>
```