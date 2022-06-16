# TypeScript Kill

## Text
```tsx
<>
  <h1>▌Hello, World!</h1>
</>
```
```tsx
<>
  <h1>▌</h1>
</>
```

```tsx
<>
  <h1>Hello, ▌World!</h1>
</>
```
```tsx
<>
  <h1>▌</h1>
</>
```

## Fragments
```tsx
<div>
  ▌<>Hello, World</>
</div>
```
```tsx
<div>
  ▌
</div>
```

## JSX Expressions
```tsx
<><div>▌{foo}</div></>
```
```tsx
<><div>▌</div></>
```

```tsx
<><div>{▌foo}</div></>
```
```tsx
<><div>{▌}</div></>
```

## JSX Component Function
```tsx
const foo = () => {
    return (
        <>
          ▌<h1>Hello, World!</h1>
          <h2>How's it going?</h2>
        </>
    );
}
```
```tsx
const foo = () => {
    return (
        <>
          ▌
          <h2>How's it going?</h2>
        </>
    );
}
```