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

## Multiple Arguments
```typescript
foo(a▌);
b
```
```typescript
foo(a,
b▌);
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