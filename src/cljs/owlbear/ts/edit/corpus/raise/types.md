# TypeScript Raise
## Single Pair
```typescript
type foo = {a: ▌string;}
```
```typescript
type foo = ▌string
```

```typescript
type foo = {▌a: string}
```
```typescript
type foo = ▌a
```

```typescript
interface foo {a: ▌string;}
```
```typescript
▌string
```

## Single Incomplete Pair
```typescript
type foo = {▌a:}
```
```typescript
type foo = ▌a
```

```typescript
type foo = {▌a:;}
```
```typescript
type foo = ▌a
```

```typescript
interface foo {▌a:}
```
```typescript
▌a
```

## Nested Object Type
```typescript
type foo = {a: {b: ▌string}};
```
```typescript
type foo = {a: ▌string};
```

```typescript
type foo = {a: {b: ▌string;};};
```
```typescript
type foo = {a: ▌string;};
```

```typescript
type foo = {a: {▌b: string}};
```
```typescript
type foo = {a: ▌b};
```