# TypeScript Forward Barf
## Basic Arrays
```typescript
[1▌]
```
```typescript
[▌]1
```

```typescript
[1, 2, 3▌]
```
```typescript
[1, 2 ▌]3
```

## Superfluous Commas
```typescript
[1,,,▌]
```
```typescript
[▌]1
```

## Nested Arrays
```typescript
[[1▌], 2]
```
```typescript
[[▌],1, 2]
```

## JSX in Arrays
```tsx
[<div></div>▌]
```
```tsx
[▌]<div></div>
```

