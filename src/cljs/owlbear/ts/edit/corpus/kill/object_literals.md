# TypeScript Kill
## Single Pair
```typescript
{a: ▌1}
```
```typescript
{a: ▌}
```

```typescript
{▌a: 1}
```
```typescript
{▌: 1}
```

## Incomplete Pair
```typescript
{▌a:}
```
```typescript
{▌}
```

## Superflous Commas
```typescript
{▌a: ,,,}
```
```typescript
{▌,,}
```

## Computed Property Names
```typescript
{▌[a]:}
```
```typescript
{▌}
```

```typescript
{[▌a]:}
```
```typescript
{[▌]:}
```