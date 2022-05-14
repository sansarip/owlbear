# TypeScript Forward Slurp
## Basic Arrays
```typescript
[▌]
1
```
```typescript
[
1▌]
```

```typescript
[1, ▌2]
3
```
```typescript
[1, ▌2,
3]
```

## Statement
```typescript
[1, 2▌]
foo();
```
```typescript
[1, 2,
foo()▌]
```

```typescript
const foo = [1, 2▌];
3
```
```typescript
const foo = [1, 2,
3▌];
```

## Nested Arrays
```typescript
[[1, 2, 3▌], 4]
```
```typescript
[[1, 2, 3, 4▌]]
```

```typescript
[[1, 2, 3▌], [4, 5, 6]]
```
```typescript
[[1, 2, 3, [4, 5, 6]▌]]
```

## JSX in Arrays
```tsx
[[<div></div>▌], <></>]
```
```tsx
[[<div></div>, <></>▌]]
```