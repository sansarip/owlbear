# TypeScript Kill
## Declaration
```typescript
type foo = ▌string;
```
```typescript
type foo = ▌;
```

```typescript
type foo = ▌bar;
```
```typescript
type foo = ▌;
```

## Single Pair
```typescript
type foo = {a: ▌string;}
```
```typescript
type foo = {a: ▌;}
```

```typescript
type foo = {▌a: string}
```
```typescript
type foo = {▌: string}
```

```typescript
interface foo {a: ▌string;}
```
```typescript
interface foo {a: ▌;}
```

## Single Incomplete Pair
```typescript
type foo = {▌a:}
```
```typescript
type foo = {▌}
```

```typescript
type foo = {▌a:;}
```
```typescript
type foo = {▌}
```

```typescript
interface foo {▌a:}
```
```typescript
interface foo {▌}
```

## Nested Object Type
```typescript
type foo = {a: {b: ▌string}};
```
```typescript
type foo = {a: {b: ▌}};
```

```typescript
type foo = {a: {b: ▌string;};};
```
```typescript
type foo = {a: {b: ▌;};};
```

```typescript
type foo = {a: {▌b: string}};
```
```typescript
type foo = {a: {▌: string}};
```

## Function Type
```typescript
type foo = ▌() => {}
```
```typescript
type foo = ▌
```

## Union Type
```typescript
type foo = ▌"hello" | "world"
```
```typescript
type foo = ▌
```

## Intersection Type
```typescript
type foo = ▌"hello" & "world"
```
```typescript
type foo = ▌
```