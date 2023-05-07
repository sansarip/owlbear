# TypeScript Splice
## Single Argument
```typescript
foo▌(a);
```
```typescript
▌a
```

## Multiple Arguments
```typescript
foo(a, 1, ▌b);
```
```typescript
a, 1, ▌b
```

## Nested Arguments
```typescript
foo(bar▌(baz()))
```
```typescript
foo(▌baz())
```

## Superfluous Commas
```typescript
foo▌(a, b,,,)
```
```typescript
▌a, b,,,
```