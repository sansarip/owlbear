# TypeScript Forward Barf
## Basic Arguments
```typescript
foo(a▌);
```
```typescript
foo(▌);a
```

## Multiple Arguments
```typescript
foo(a, 1, b▌);
```
```typescript
foo(a, 1 ▌);b
```

```typescript
foo(a, 1, bar()▌);
```
```typescript
foo(a, 1 ▌);bar()
```

## Superfluous Commas
```typescript
foo(a, b,▌)
```
```typescript
foo(a ,▌)b
```

```typescript
foo(a, b,,,▌)
```
```typescript
foo(a ,,,▌)b
```

```typescript
foo(a,,,▌)
```
```typescript
foo(▌)a
```

## In Array
```typescript
[foo(a▌)]
```
```typescript
[foo(▌),a]
```

```typescript
[foo(a▌), b]
```
```typescript
[foo(▌),a, b]
```