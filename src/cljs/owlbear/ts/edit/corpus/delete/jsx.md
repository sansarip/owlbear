# TypeScript Backward Delete

##  Empty Elements
```tsx
const foo = <h1>▌</h1>
```
```tsx
const foo = <h1▌></h1>
```

```tsx
const foo = <h1></h1>▌
```
```tsx
const foo = <h1></h1▌>
```

```tsx
const foo = <▌h1></h1>
```
```tsx
const foo = ▌
```

```tsx
const foo = <h1></▌h1>
```
```tsx
const foo = <h1><▌/h1>
```

```tsx
const foo = <▌a/>
```
```tsx
const foo = ▌
```

```tsx
const foo = <a/>▌
```
```tsx
const foo = <a/▌>
```

```tsx
const foo = <a/▌>
```
```tsx
const foo = <a▌/>
```

```tsx
const foo = <>▌</>
```
```tsx
const foo = <▌></>
```

```tsx
const foo = <></>▌
```
```tsx
const foo = <></▌>
```

```tsx
const foo = <▌></>
```
```tsx
const foo = ▌
```

```tsx
const foo = <></▌>
```
```tsx
const foo = <><▌/>
```

## Nested Elements
```tsx
const foo = <h1><▌p></p></h1>
```
```tsx
const foo = <h1>▌</h1>
```

## Tag Names
```tsx
const foo = <a▌></a>
```
```tsx
const foo = <▌></a>
```

```tsx
const foo = <a></a▌>
```
```tsx
const foo = <a></▌>
```

## Nonempty Elements
```tsx
const foo = <h1>Hello, World</h1>▌
```
```tsx
const foo = <h1>Hello, World</h1▌>
```

```tsx
const foo = <h1>Hello, World</▌h1>
```
```tsx
const foo = <h1>Hello, World<▌/h1>
```

```tsx
const foo = <h1>▌Hello, World</h1>
```
```tsx
const foo = <h1▌>Hello, World</h1>
```

```tsx
const foo = <h1>Hello, World▌</h1>
```
```tsx
const foo = <h1>Hello, Worl▌</h1>
```

```tsx
const foo = <h1>a▌</h1>
```
```tsx
const foo = <h1>▌</h1>
```

```tsx
const foo = <a href="google.com"/>▌
```
```tsx
const foo = <a href="google.com"/▌>
```

```tsx
const foo = <a href="google.com"/▌>
```
```tsx
const foo = <a href="google.com"▌/>
```

## Attributes
```tsx
const foo = <p id="foo"></▌p>
```
```tsx
const foo = <p id="foo"><▌/p>
```

```tsx
const foo = <p id="▌foo"></p>
```
```tsx
const foo = <p id=▌"foo"></p>
```

```tsx
const foo = <p id="foo▌"></p>
```
```tsx
const foo = <p id="fo▌"></p>
```

```tsx
const foo = <p id="▌"></p>
```
```tsx
const foo = <p id=▌></p>
```

# TypeScript Forward Delete

##  Empty Elements
```tsx
const foo = <h1>▌</h1>
```
```tsx
const foo = <h1><▌/h1>
```

```tsx
const foo = ▌<h1></h1>
```
```tsx
const foo = <▌h1></h1>
```

```tsx
const foo = <h1></h1▌>
```
```tsx
const foo = ▌
```

```tsx
const foo = <h1>▌</h1>
```
```tsx
const foo = <h1><▌/h1>
```

```tsx
const foo = <a▌/>
```
```tsx
const foo = ▌
```

```tsx
const foo = ▌<a/>
```
```tsx
const foo = <▌a/>
```


```tsx
const foo = <>▌</>
```
```tsx
const foo = <><▌/>
```

```tsx
const foo = ▌<></>
```
```tsx
const foo = <▌></>
```

```tsx
const foo = <></▌>
```
```tsx
const foo = ▌
```

```tsx
const foo = <><▌/>
```
```tsx
const foo = ▌
```

## Nested Elements
```tsx
const foo = <h1><p></p▌></h1>
```
```tsx
const foo = <h1>▌</h1>
```

## Tag Names
```tsx
const foo = <▌a></a>
```
```tsx
const foo = <▌></a>
```

```tsx
const foo = <a></▌a>
```
```tsx
const foo = <a></▌>
```

## Nonempty Elements
```tsx
const foo = ▌<h1>Hello, World</h1>
```
```tsx
const foo = <▌h1>Hello, World</h1>
```

```tsx
const foo = <h1>Hello, World▌</h1>
```
```tsx
const foo = <h1>Hello, World<▌/h1>
```

```tsx
const foo = <h1▌>Hello, World</h1>
```
```tsx
const foo = <h1>▌Hello, World</h1>
```

```tsx
const foo = <h1>Hello, Worl▌d</h1>
```
```tsx
const foo = <h1>Hello, Worl▌</h1>
```

```tsx
const foo = <h1>▌a</h1>
```
```tsx
const foo = <h1>▌</h1>
```

```tsx
const foo = <a href="google.com"▌/>
```
```tsx
const foo = <a href="google.com"/▌>
```

```tsx
const foo = <a href="google.com"/▌>
```
```tsx
const foo = <a href="google.com"/>▌
```

```tsx
const foo = <a href="google.com▌"/>
```
```tsx
const foo = <a href="google.com"▌/>
```

## Attributes
```tsx
const foo = <p id="foo"></p▌>
```
```tsx
const foo = <p id="foo"></p>▌
```

```tsx
const foo = <p id="foo▌"></p>
```
```tsx
const foo = <p id="foo"▌></p>
```

```tsx
const foo = <p id="fo▌o"></p>
```
```tsx
const foo = <p id="fo▌"></p>
```

```tsx
const foo = <p id="▌"></p>
```
```tsx
const foo = <p id=▌></p>
```