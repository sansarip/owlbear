# TypeScript Forward Slurp
## Empty Arguments
```typescript
foo(▌);
a
```
```typescript
foo(
a▌);
```

## In Declaration
```typescript
const foo = bar(▌);
a
```
```typescript
const foo = bar(
a▌);
```

## Multiple Arguments
```typescript
foo(a▌);
b
```
```typescript
foo(a,
b▌);
```

## Statement Blocks
```typescript
foo(() => {▌}, bar(), baz())
```
```typescript
foo(() => { bar()▌}, baz())
```

## Nested Args
```typescript
foo(bar(▌), baz(), qux())
```
```typescript
foo(bar( baz()▌), qux())
```

## Statements
```typescript
foo(▌);
bar();
```
```typescript
foo(
bar()▌);
```

## Superfluous Commas
```typescript
foo(a▌,,,);
b
```
```typescript
foo(a,
b▌,,,);
```

## In Array
```typescript
[foo(a▌), b, c]
```
```typescript
[foo(a, b▌), c]
```

```typescript
[foo(▌), a, b]
```
```typescript
[foo( a▌), b]
```