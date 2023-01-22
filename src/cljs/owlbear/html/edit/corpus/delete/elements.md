# HTML Backward Delete

## Empty Elements
```html
<h1>▌</h1>
```
```html
▌
```

```html
<h1></h1>▌
```
```html
▌
```

```html
<▌h1></h1>
```
```html
▌
```

```html
<h1></▌h1>
```
```html
▌
```

```html
<>▌</>
```
```html
▌
```

```html
<p>▌<p>Hello, world!</p>
```
```html
▌<p>Hello, world!</p>
```

```html
<>▌<p>Hello, world!</p>
```
```html
▌<p>Hello, world!</p>
```

## Tag Names
```html
<span>Hello there!</span▌>
```
```html
<span>Hello there!</spa▌>
```

```html
<span>Hello there!</s▌pan>
```
```html
<span>Hello there!</▌pan>
```

```html
<a>Hello there!</a▌>
```
```html
<a>Hello there!</▌>
```

```html
<a▌>Hello there!</a>
```
```html
<▌>Hello there!</a>
```

```html
<p▌>Hello, <p>World!</p>
```
```html
<▌>Hello, <p>World!</p>
```

## Nonempty Elements
```html
<h1>Hello, World</h1>▌
```
```html
<h1>Hello, World</h1▌>
```

```html
<h1>Hello, World</▌h1>
```
```html
<h1>Hello, World<▌/h1>
```

```html
<h1>▌Hello, World</h1>
```
```html
<h1▌>Hello, World</h1>
```

```html
<h1>Hello, World▌</h1>
```
```html
<h1>Hello, Worl▌</h1>
```

```html
<h1>a▌</h1>
```
```html
<h1>▌</h1>
```

```html
<p>▌Hello, <p>World!</p>
```
```html
<p▌>Hello, <p>World!</p>
```

```html
<>▌Hello, <p>World!</p>
```
```html
<▌>Hello, <p>World!</p>
```

## Attributes
```tsx
<p id="foo"></▌p>
```
```tsx
<p id="foo"><▌/p>
```

```tsx
<p id="▌foo"></p>
```
```tsx
<p id=▌"foo"></p>
```

```tsx
<p id="▌"></p>
```
```tsx
<p id=▌></p>
```