# TypeScript Kill
##  Single Item
```typescript
[▌1]
```
```typescript
[▌]
```

##  Multiple Items
```typescript
[1, ▌2, 3]
```
```typescript
[1, ▌ 3]
```

## Superfluous Commas
```typescript
[▌1,,,]
```
```typescript
[▌,,]
```

## Nested Arrays
```typescript
[▌[1], 2]
```
```typescript
[▌ 2]
```