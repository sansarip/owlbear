# TypeScript Backward Delete
## Empty Object Type
```ts
type foo = {}▌;
```
```ts
type foo = ▌;
```

## Nonempty Object Type
```ts
type foo = {a: string}▌;
```
```ts
type foo = {a: string▌};
```

```ts
type foo = {a: }▌;
```
```ts
type foo = {a: ▌};
```

```ts
type foo = {a: string▌}
```
```ts
type foo = {a: strin▌}
```