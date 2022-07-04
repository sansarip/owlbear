# TypeScript Forward Move
## Chained Arguments
```typescript
foo().bar(▌a, b, c)
```
```typescript
foo().bar(a, ▌b, c)
```

```typescript
foo(▌1).bar(a, b, c)
```
```typescript
❎
```

## Default Arguments
```typescript
function foo (a = ▌bar(), b, c) {}
```
```typescript
function foo (a = bar(), ▌b, c) {}
```

## Multiple Arguments
```typescript
foo(▌a, b, c);
```
```typescript
foo(a, ▌b, c);
```

```typescript
foo(a, b, ▌c);
bar();
```
```typescript
❎
```

## Nested Arguments
```typescript
foo(bar(▌baz(), a, b));
```
```typescript
foo(bar(baz(), ▌a, b));
```

## Parameters
```typescript
function foo (▌a, b, c) {}
```
```typescript
function foo (a, ▌b, c) {}
```

## Superfluous Argument Whitespace
```typescript
foo(a, ▌ b, c);
```
```typescript
foo(a,  ▌b, c);
```

## Superfluous Commas
```typescript
foo(a, ▌b,,,)
```
```typescript
❎
```

## Superfluous Default Arg Whitespace
```typescript
function foo (a = ▌ 1, b, c) {}
```
```typescript
function foo (a =  ▌1, b, c) {}
```

## Superfluous Parameter Whitespace
```typescript
function foo (a, ▌ b, c) {}
```
```typescript
function foo (a,  ▌b, c) {}
```

## Typed Parameters
```typescript
function foo (a: ▌string, b, c) {}
```
```typescript
function foo (a: string, ▌b, c) {}
```
