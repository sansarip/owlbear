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

## Namespace Declaration
```ts
declare namespace Foo {▌}
interface bar {a: string};
```
```ts
declare namespace Foo {
interface bar {a: string}▌};
```

## Nested Object Types
```ts
type foo = {a: ▌{b: string;}; c: string;}
```
```ts
type foo = {a: ▌{b: string; c: string;};}
```

```ts
type foo = {a: ▌{}; b: string;}
```
```ts
type foo = {a: ▌{ b: string};}
```