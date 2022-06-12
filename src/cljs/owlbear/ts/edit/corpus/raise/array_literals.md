# TypeScript Raise
##  Single Item
```typescript
[▌1]
```
```typescript
▌1
```

##  Multiple Items
```typescript
[1, ▌2, 3]
```
```typescript
▌2
```

## Superfluous Commas
```typescript
[▌1,,,]
```
```typescript
▌1
```

## Nested Arrays
```typescript
[▌[1], 2]
```
```typescript
▌[1]
```