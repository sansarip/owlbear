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
{▌}
```

## Incomplete Pair
```typescript
{▌a:}
```
```typescript
{▌}
```

## Superfluous Commas
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