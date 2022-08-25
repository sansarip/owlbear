# TypeScript Forward Move
## Spread Syntax
```typescript
[▌...foo, bar]
```
```typescript
[...foo, ▌bar]
```

```typescript
[..▌.foo, bar]
```
```typescript
[...foo, ▌bar]
```

```typescript
[...▌foo, bar]
```
```typescript
[...foo, ▌bar]
```

```typescript
[...▌foo()]
```
```typescript
[...foo▌()]
```

```typescript
[...foo▌(), bar]
```
```typescript
[...foo(), ▌bar]
```

```typescript
[...▌foo.bar()]
```
```typescript
[...foo.▌bar()]
```

# TypeScript Backward Move
## Spread Syntax
```typescript
[...foo, ▌bar]
```
```typescript
[▌...foo, bar]
```

```typescript
[bar, ..▌.foo]
```
```typescript
[▌bar, ...foo]
```

```typescript
[bar, ...▌foo]
```
```typescript
[▌bar, ...foo]
```

```typescript
[...foo▌()]
```
```typescript
[...▌foo()]
```

```typescript
[...foo(), ▌bar]
```
```typescript
[▌...foo(), bar]
```

```typescript
[...foo.▌bar()]
```
```typescript
[...▌foo.bar()]
```
