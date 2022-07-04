# TypeScript Raise
## Single Argument
```typescript
foo(▌a);
```
```typescript
▌a;
```

## Multiple Arguments
```typescript
foo(a, 1, ▌b);
```
```typescript
▌b;
```

```typescript
foo(a, 1, ▌bar());
```
```typescript
▌bar();
```

## Nested Arguments
```typescript
foo(bar(▌baz()))
```
```typescript
foo(▌baz())
```

## Chained Arguments
```TypeScript
foo().bar(▌baz())
```
```TypeScript
▌baz()
```

## Superfluous Commas
```typescript
foo(a, ▌b,,,)
```
```typescript
▌b
```