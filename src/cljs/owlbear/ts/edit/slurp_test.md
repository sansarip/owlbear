# Forward Slurp
## Simple JSX
```tsx
<>
  <h1>Hello, 📍</h1>
  World!
</>
```
```tsx
<>
  <h1>Hello, 📍
  World!</h1>
</>
```
---
```tsx
<>
  <h1>Hello, </h1>📍
  World!
</>
```
```tsx
<>
  <h1>Hello, 
  World!</h1>📍
</>
```
## JSX component function
```tsx
const foo = () => {
    return (
        <>
          <h1>Hello, 📍</h1>
          World!  
        </>
    )
}
```
```tsx
const foo = () => {
    return (
        <>
          <h1>Hello, 📍
          World!</h1>  
        </>
    )
}
```