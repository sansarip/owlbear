# TypeScript Forward Barf
## Basic JSX
```tsx
<>
  <h1>Hello, World!▌</h1>
</>
```
```tsx
<>
  <h1>▌</h1>Hello, World!
</>
```

## Fragments
```tsx
<>
  <h1>Hello, World!</h1>
  <div></div>
▌</>
```
```tsx
<>
  <h1>Hello, World!</h1>
  ▌</><div></div>

```

## JSX Expressions
```tsx
<><div>{foo}▌</div></>
```
```tsx
<><div>▌</div>{foo}</>
```

## JSX Component Function
```tsx
const foo = () => {
    return (
        <>
          <h1>Hello, World!▌</h1>
          <h2>How's it going?</h2>
        </>
    );
}
```
```tsx
const foo = () => {
    return (
        <>
          <h1>▌</h1>Hello, World!
          <h2>How's it going?</h2>
        </>
    );
}
```