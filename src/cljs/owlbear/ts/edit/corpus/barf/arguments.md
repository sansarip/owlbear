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

## Arrays
```typescript
foo([a, b, c▌]);
```
```typescript
foo([a, b ▌],c);
```

```typescript
foo([a, b, c▌], d);
```
```typescript
foo([a, b ▌],c, d);
```

## Nested Arguments
```typescript
foo(bar(baz()▌))
```
```typescript
foo(bar(▌),baz())
```

```typescript
foo(bar(baz()▌), qux())
```
```typescript
foo(bar(▌),baz(), qux())
```

## Object Literals
```typescript
foo({a: b▌});
```
```typescript
foo({a: ▌},b);
```

## Statement Blocks
```typescript
foo(() => {bar()▌})
```
```typescript
foo(() => {▌},bar())
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