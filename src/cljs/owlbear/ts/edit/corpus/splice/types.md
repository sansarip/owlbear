# TypeScript Splice
## Single Pair
```typescript
type foo = ▌{a: string;}
```
```typescript
▌a: string;
```

```typescript
type foo = {▌a: string}
```
```typescript
▌a: string
```

## Single Incomplete Pair
```typescript
type foo = {▌a:}
```
```typescript
▌a:
```

```typescript
type foo = {▌a:;}
```
```typescript
▌a:;
```

```typescript
interface foo ▌{a:}
```
```typescript
▌a:
```

## Nested Object Type
```typescript
type foo = {a: ▌{b: string}};
```
```typescript
type foo = {a: ▌b: string};
```

```typescript
type foo = {a: ▌{b: string;};};
```
```typescript
type foo = {a: ▌b: string;;};
```
