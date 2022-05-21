# TypeScript Forward Slurp
## Empty Type/Interface
```typescript
type foo = {▌}
a
```
```typescript
type foo = {
a▌:;}
```

```typescript
interface foo {▌}
a
```
```typescript
interface foo {
a▌:;}
```

## Type/Interface with Pair
```typescript
type foo = {a: string;▌}
b
```
```typescript
type foo = {a: string;
b:▌;}
```

```typescript
type foo = {a: string▌}
b
```
```typescript
type foo = {a: string;
b▌:;}
```

```typescript
interface foo {a: string;▌}
b
```
```typescript
interface foo {a: string;
b:▌;}
```

## Type/Interface with Incomplete Pair
```typescript
type foo = {a:▌}
string
```
```typescript
type foo = {a:
string▌;}
```

```typescript
type foo = {a:▌;}
string
```
```typescript
type foo = {a:
string▌;}
```

```typescript
interface foo {a:▌}
string
```
```typescript
interface foo {a:
string▌;}
```

## Type/Interface with Generics
```typescript
type foo<S> = {a:▌}
string
```
```typescript
type foo<S> = {a:
string▌;}
```

```typescript
interface foo<S> {a:▌}
string
```
```typescript
interface foo<S> {a:
string▌;}
```