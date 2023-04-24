# TypeScript Kill
## Call Expressions
```typescript
foo().▌bar();
```
```typescript
foo()▌;
```

```typescript
fo▌o().bar();
```
```typescript
▌
```

```typescript
foo()▌.bar();
```
```typescript
▌
```

```typescript
foo().bar(▌baz());
```
```typescript
foo().bar(▌);
```

```typescript
foo.bar(▌a).qux(b);
```
```typescript
foo.bar(▌).qux(b);
```

```typescript
foo.bar(a).qux(▌b);
```
```typescript
foo.bar(a).qux(▌);
```

```typescript
const a = foo.bar(a).qux(▌b);
```
```typescript
const a = foo.bar(a).qux(▌);
```

## Binary Expressions
```typescript
const foo = ▌1 + 1;
```
```typescript
const foo = ▌;
```

```typescript
▌1 + 1;
```
```typescript
▌
```

```typescript
1 + ▌1;
```
```typescript
1 + ▌;
```

## Imports
```tsx
import {▌a, b, c} from '/foo'
```
```tsx
import {▌ b, c} from '/foo'
```

## Exports
```tsx
export {▌a, b, c} from '/foo'
```
```tsx
export {▌ b, c} from '/foo'
```
