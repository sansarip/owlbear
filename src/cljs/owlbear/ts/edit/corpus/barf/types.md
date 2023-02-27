# TypeScript Forward Barf
## Type/Interface with Pair
```typescript
type foo = {a: string;▌}
```
```typescript
type foo = {a: ;▌}string
```

```typescript
type foo = {a: string▌}
```
```typescript
type foo = {a: ▌}string
```

```typescript
interface foo {a: string;▌}
```
```typescript
interface foo {a: ;▌}string
```

## Type/Interface with Incomplete Pair
```typescript
type foo = {a:▌}
```
```typescript
type foo = {▌}a
```

```typescript
type foo = {a:▌;}
```
```typescript
type foo = {▌}a
```

```typescript
interface foo {a:▌}
```
```typescript
interface foo {▌}a
```


## Namespace Declaration
```ts
declare namespace Foo {
interface bar {a: string}▌};
```
```ts
declare namespace Foo {
▌}interface bar {a: string};
```