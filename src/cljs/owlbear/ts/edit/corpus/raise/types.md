# TypeScript Raise
## Declaration
```typescript
type foo = ▌string;
```
```typescript
▌string
```

```typescript
type foo = ▌bar;
```
```typescript
▌bar
```

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