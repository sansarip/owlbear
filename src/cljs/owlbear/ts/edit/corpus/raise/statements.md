# TypeScript Raise
## Call Expressions
```typescript
foo().▌bar();
```
```typescript
▌bar();
```

```typescript
foo().bar(▌baz());
```
```typescript
▌baz();
```

```typescript
foo.bar(▌a).qux(b);
```
```typescript
▌a.qux(b);
```

```typescript
foo.bar(a).qux(▌b);
```
```typescript
▌b;
```

```typescript
const a = foo.bar(a).qux(▌b);
```
```typescript
const a = ▌b;
```

```typescript
const a = ▌foo();
```
```typescript
▌foo()
```

## Async Expressions
```typescript
const foo = ▌await bar();
```
```typescript
▌await bar()
```

```typescript
const foo = await ▌bar();
```
```typescript
const foo = ▌bar();
```