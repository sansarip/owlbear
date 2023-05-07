# TypeScript Splice
## Elements
```tsx
<>
  ▌<h1>Hello, World!</h1>
</>
```
```tsx
<>
  ▌Hello, World!
</>
```

```tsx
▌<>
  <h1>Hello, World!</h1>
</>
```
```tsx
▌
  <h1>Hello, World!</h1>

```

```tsx
<>
  <h1>Hello, World!</h1>
</▌>
```
```tsx

  <h1>Hello, World!</h1>
▌
```

```tsx
<div>
  <section>
    <h1>My First Heading</h1>

    <▌p>My first paragraph.</p>
  </section>
</div>
```
```tsx
<div>
  <section>
    <h1>My First Heading</h1>

    ▌My first paragraph.
  </section>
</div>
```

## Text
```tsx
<>
  ▌<h1>Hello, World!</h1>
</>
```
```tsx
<>
  ▌Hello, World!
</>
```

```tsx
<>
  <h1>Hello, ▌World!</h1>
</>
```
```tsx
<>
  Hello, ▌World!
</>
```

## Fragments
```tsx
▌<div>
  <>Hello, World</>
</div>
```
```tsx
▌
  <>Hello, World</>

```

## JSX Expressions
```tsx
<>▌<div>{foo}</div></>
```
```tsx
<>▌{foo}</>
```

```tsx
<><div>▌{foo}</div></>
```
```tsx
<><div>▌foo</div></>
```

## JSX Component Function
```tsx
const foo = () => {
    return (
        <>
          ▌<div>
            <h1>Hello, World!</h1>
            <h2>How's it going?</h2>
          </div>
        </>
    );
}
```
```tsx
const foo = () => {
    return (
        <>
          ▌
            <h1>Hello, World!</h1>
            <h2>How's it going?</h2>
          
        </>
    );
}
```
