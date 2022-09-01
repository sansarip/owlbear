# TypeScript Forward Move
## Object Level
```typescript
const foo = ▌{a: 1};
bar();
```
```typescript
const foo = {a: 1};
▌bar();
```

```typescript
const foo = {a: 1▌};
bar();
```
```typescript
const foo = {a: 1};
▌bar();
```

## Object Key
```typescript
const foo = {▌a: 1}
```
```typescript
const foo = {a: ▌1}
```

## Object Value
```typescript
const foo = {a: ▌1, b: 2}
```
```typescript
const foo = {a: 1, ▌b: 2}
```

```typescript
const foo = {a: bar▌(), b: 2}
```
```typescript
const foo = {a: bar(), ▌b: 2}
```

## Computed Property
```typescript
const foo = {▌[a]: 1, b: 2}
```
```typescript
const foo = {[a]: ▌1, b: 2}
```

## Shorthand
```typescript
const foo = {▌a, b: 2}
```
```typescript
const foo = {a, ▌b: 2}
```

## Superfluous Whitespace
```typescript
const foo = {
  ▌
  a: 1,
  b: 2
}
```
```typescript
const foo = {
  
  ▌a: 1,
  b: 2
}
```

```typescript
const foo = {▌ 
  a: 1,
  b: 2
}
```
```typescript
const foo = { 
  ▌a: 1,
  b: 2
}
```

```typescript
const foo = {
  a: 1,
  ▌
  b: 2
}
```
```typescript
const foo = {
  a: 1,
  
  ▌b: 2
}
```

```typescript
const foo = {
  a: 1,
  b: 
  ▌
  2
}
```
```typescript
const foo = {
  a: 1,
  b: 
  
  ▌2
}
```

# TypeScript Backward Move
## Object Level
```typescript
const foo = {a: 1};
▌bar();
```
```typescript
▌const foo = {a: 1};
bar();
```

```typescript
const foo = ▌{a: 1};
```
```typescript
const ▌foo = {a: 1};
```

## Object Key
```typescript
const foo = {▌a: 1}
```
```typescript
const ▌foo = {a: 1}
```

## Object Value
```typescript
const foo = {a: ▌1, b: 2}
```
```typescript
const foo = {▌a: 1, b: 2}
```

## Computed Property
```typescript
const foo = {▌[a]: 1, b: 2}
```
```typescript
const ▌foo = {[a]: 1, b: 2}
```

## Shorthand
```typescript
const foo = {▌a, b: 2}
```
```typescript
const ▌foo = {a, b: 2}
```

## Superfluous Whitespace
```typescript
const foo = {
  a: 1,
  b: 2
  ▌
}
```
```typescript
const foo = {
  a: 1,
  ▌b: 2
  
}
```

```typescript
const foo = { 
  a: 1,
  b: 2
▌
}
```
```typescript
const foo = { 
  a: 1,
  ▌b: 2

}
```

```typescript
const foo = {
  a: 1,
  ▌
  b: 2
}
```
```typescript
const foo = {
  ▌a: 1,
  
  b: 2
}
```

```typescript
const foo = {
  a: 1,
  b: 
  ▌
  2
}
```
```typescript
const foo = {
  a: 1,
  ▌b: 
  
  2
}
```