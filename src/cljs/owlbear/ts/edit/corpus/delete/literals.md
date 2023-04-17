# TypeScript Backward Delete
## Empty Object
```ts
const foo = {}▌
```
```ts
const foo = {▌}
```

```ts
const foo = {▌}
```
```ts
const foo = ▌
```

```ts
const foo = {,▌}
```
```ts
const foo = {▌}
```

```ts
const foo = {,}▌
```
```ts
const foo = {,▌}
```

```ts
const foo = {▌,}
```
```ts
const foo = ▌
```

## Nonempty Object
```ts
const foo = {a: 1}▌
```
```ts
const foo = {a: 1▌}
```

```ts
const foo = {▌a: 1}
```
```ts
const foo = ▌{a: 1}
```

```ts
const foo = {a: 1▌}
```
```ts
const foo = {a: ▌}
```

## Empty Computed Property Names
```ts
{[]▌: 1}
```
```ts
{[▌]: 1}
```

```ts
{[▌]: 1}
```
```ts
{▌: 1}
```

## Nonempty Computed Property Names
```ts
{[a]▌: 1}
```
```ts
{[a▌]: 1}
```

```ts
{[a▌]: 1}
```
```ts
{[▌]: 1}
```

## Empty Arrays
```ts
[]▌
```
```ts
[▌]
```

```ts
[▌]
```
```ts
▌
```

```ts
[,▌]
```
```ts
[▌]
```

```ts
[▌,]
```
```ts
▌
```