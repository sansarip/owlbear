# TypeScript Forward Slurp
## Empty Comment
```typescript
/*▌*/
const foo = {};
```
```typescript
/*
const foo = {};▌*/
```

## Comment Meets Comment
```typescript
/*▌*/
/**/
```
```typescript
/*
\/**\/▌*/
```

```typescript
/*▌*/
/*\/**\/*/
```
```typescript
/*
\/*\/**\/*\/▌*/
```

```typescript
/*▌*/
const foo = () => {
  /* Hello, World! */
  // Buh!
  return 1 + 1;
}
```
```typescript
/*
const foo = () => {
  \/* Hello, World! *\/
  // Buh!
  return 1 + 1;
}▌*/
```