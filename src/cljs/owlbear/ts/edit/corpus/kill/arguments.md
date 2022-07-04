# TypeScript Kill
## Single Argument
```typescript
foo(▌a);
```
```typescript
foo(▌);
```

## Multiple Arguments
```typescript
foo(a, 1, ▌b);
```
```typescript
foo(a, 1▌);
```

```typescript
foo(a, 1, ▌bar());
```
```typescript
foo(a, 1▌);
```

```typescript
foo(a, ▌1, bar());
```
```typescript
foo(a, ▌ bar());
```

## Nested Arguments
```typescript
foo(bar(▌baz()))
```
```typescript
foo(bar(▌))
```

## Chained Arguments
```TypeScript
foo().bar(▌baz())
```
```TypeScript
foo().bar(▌)
```

## Superfluous Commas
```typescript
foo(a, ▌b,,,)
```
```typescript
foo(a, ▌,,)
```