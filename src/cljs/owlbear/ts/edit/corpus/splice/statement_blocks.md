# TypeScript Splice
## Lone Statement Block
```typescript
▌{
  a();
}
```
```typescript
▌
  a();

```

```typescript
{
  ▌a()
}
```
```typescript

  ▌a()

```

## Arrow Function Statement Block
```typescript
const foo = () => ▌{
  const sum = 1 + 1;
  return sum;
}
```
```typescript
▌
  const sum = 1 + 1;
  return sum;

```

## Async Statements
```typescript
▌{
  await foo();
}
```
```typescript
▌
  await foo();

```