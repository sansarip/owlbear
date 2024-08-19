# TypeScript Forward Slurp
## Empty Object
Have to define an empty object as part of an expression here, because `{}` by itself is parsed as a statement block.
```typescript
const foo = {▌}
"a"
```
```typescript
const foo = {
"a"▌:}
```

## Object with Incomplete Pair
```typescript
{a:▌}
1
```
```typescript
{a:
1▌}
```

## Object with Pair
```typescript
{a: 1▌}
"b"
```
```typescript
{a: 1,
"b"▌:}
```

## Object with Incomplete & Complete Pairs
```typescript
{a: 1, b:▌}
2
```
```typescript
{a: 1, b:
2▌}
```

## Superfluous Commas
```typescript
{a: 1,▌}
"b"
```
```typescript
{a: 1,
"b":▌,}
```

```typescript
{a: 1, b:▌,}
2
```
```typescript
{a: 1, b:
2▌,}
```

## Statements
```typescript
{a: 1, b:▌}
foo();
```
```typescript
{a: 1, b:
foo()▌}
```

```typescript
const foo = {▌};
"a"
```
```typescript
const foo = {
"a"▌:};
```

## Computed Property Names
```typescript
const foo = {▌}
a
```
```typescript
const foo = {
[a▌]:}
```

```typescript
const foo = {▌}
foo();
```
```typescript
const foo = {
[foo()▌]:}
```

```typescript
{a: 1▌}
b
```
```typescript
{a: 1,
[b▌]:}
```

## In Arrays
```typescript
[{▌}, a, b, c]
```
```typescript
[{ [a▌]:}, b, c]
```

```typescript
[{[a]:▌}, b, c]
```
```typescript
[{[a]: b▌}, c]
```

```typescript
[{[a]: b▌}, c]
```
```typescript
[{[a]: b, [c▌]:}]
```

## In Objects
```typescript
{a: ▌{}, b: c}
```
```typescript
{a: ▌{ b: c}}
```

```typescript
{a: ▌{b: c}, c: d}
```
```typescript
{a: ▌{b: c, c: d}}
```

```typescript
{a: {b: c▌}, c: d}
```
```typescript
{a: {b: c, c: d▌}}
```

```typescript
{a: ▌{b: c, c: d}, b}
```
```typescript
{a: ▌{b: c, c: d, [b]:}}
```

```typescript
{a: {b: ▌{}, c: d}}
```
```typescript
{a: {b: ▌{ c: d}}}
```

```typescript
const foo = {
  a: {
    ▌b: number
  },
  c: string
};
```
```typescript
const foo = {
  a: {
    ▌b: number
  ,
  c: string}
};
```
