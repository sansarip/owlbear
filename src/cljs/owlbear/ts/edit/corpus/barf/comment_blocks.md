# TypeScript Forward Barf
## Basic Comment
```typescript
/*foo▌*/
```
```typescript
/*▌*/foo
```

## Comment Within Comment
```typescript
/*\/**\/▌*/
```
```typescript
/*▌*//**/
```

```typescript
/*\/*\/**\/*\/▌*/
```
```typescript
/*▌*//*\/**\/*/
```

```typescript
/*
const foo = () => {
  \/* Hello, World! *\/
  // Buh!
  return 1 + 1;
}
▌*/
```
```typescript
/*▌*/
const foo = () => {
  /* Hello, World! */
  // Buh!
  return 1 + 1;
}

```