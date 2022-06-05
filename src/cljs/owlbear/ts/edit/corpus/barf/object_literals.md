# TypeScript Forward Barf
## Object with Pair
```typescript
{a: 1▌}
```
```typescript
{a: ▌}1
```

## Object with Incomplete Pair
```typescript
{a:▌}
```
```typescript
{▌}a
```

## Object with Incomplete & Complete Pairs
```typescript
{a: 1, b:▌}
```
```typescript
{a: 1 ▌}b
```

## Superfluous Commas
```typescript
{a: 1,▌}
```
```typescript
{a: ,▌}1
```

```typescript
{a: ,,,▌}
```
```typescript
{▌}a 
```

## Computed Property Names
```typescript
{[a]:▌}
```
```typescript
{▌}a
```

## In Arrays
```typescript
[{a: 1▌}, 2]
```
```typescript
[{a: ▌},1, 2]
```

```typescript
[{a: ▌},1, 2]
```
```typescript
[{▌},a ,1, 2]
```
