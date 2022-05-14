# TypeScript Forward Slurp
## Basic JSX
```tsx
<>
  <h1>Hello▌, </h1>
  World!
</>
```
```tsx
<>
  <h1>Hello▌, 
  World!
</h1></>
```

```tsx
<>
  <h1>Hello, </h1▌>
  World!
</>
```
```tsx
<>
  <h1>Hello, 
  World!
</h1▌></>
```

## JSX Expressions
```tsx
<><div>▌</div>{foo}</>
```
```tsx
<><div>{foo}▌</div></>
```

## JSX component function
```tsx
const foo = () => {
    return (
        <>
          <h1>Hello,▌ </h1>
          World!  
        </>
    )
}
```
```tsx
const foo = () => {
    return (
        <>
          <h1>Hello,▌ 
          World!  
        </h1></>
    )
}
```