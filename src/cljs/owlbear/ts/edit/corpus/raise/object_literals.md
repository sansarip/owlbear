# TypeScript Raise
## Single Pair
```typescript
{a: ▌1}
```
```typescript
▌1
```

```typescript
{▌a: 1}
```
```typescript
▌a
```

## Incomplete Pair
```typescript
{▌a:}
```
```typescript
▌a
```

## Superfluous Commas
```typescript
{▌a: ,,,}
```
```typescript
▌a
```

## Computed Property Names
```typescript
{▌[a]:}
```
```typescript
▌[a]
```

```typescript
{[▌a]:}
```
```typescript
▌a
```