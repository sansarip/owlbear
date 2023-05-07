# TypeScript Splice
## Single Pair
```typescript
▌{a: 1}
```
```typescript
▌a: 1
```

```typescript
{▌a: 1}
```
```typescript
▌a: 1
```

## Incomplete Pair
```typescript
{▌a:}
```
```typescript
▌a:
```

## Superfluous Commas
```typescript
{▌a: ,,,}
```
```typescript
▌a: ,,,
```

## Computed Property Names
```typescript
▌{[a]:}
```
```typescript
▌[a]:
```

## Nested
```tsx
{a: ▌{b: 1}}
```
```tsx
{a: ▌b: 1}
```