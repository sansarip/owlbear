# TypeScript Backward Delete
## Empty Arguments
```typescript
foo(▌);
```
```typescript
foo▌;
```

```typescript
foo(bar(▌))
```
```typescript
foo(bar▌)
```

```typescript
foo().bar(▌)
```
```typescript
foo().bar▌
```

## Nonempty Arguments
```typescript
foo(a▌);
```
```typescript
foo(▌);
```

```typescript
foo(a, b▌);
```
```typescript
foo(a, ▌);
```

# TypeScript Forward Delete
## Empty Arguments
```typescript
foo(▌);
```
```typescript
foo▌;
```

```typescript
foo(bar(▌))
```
```typescript
foo(bar▌)
```

```typescript
foo().bar(▌)
```
```typescript
foo().bar▌
```

## Nonempty Arguments
```typescript
foo(▌a);
```
```typescript
foo(▌);
```

```typescript
foo(a, ▌b);
```
```typescript
foo(a, ▌);
```