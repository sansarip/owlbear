# TypeScript Forward Move
## 1-3 Items
```typescript
[▌1, 2, 3]
```
```typescript
[1, ▌2, 3]
```

```typescript
[1, ▌2, 3]
```
```typescript
[1, 2, ▌3]
```

## Array Level
```typescript
▌[1, 2, 3]
foo();
```
```typescript
[1, 2, 3]
▌foo();
```


## Separators
```typescript
[1▌, 2, 3]
```
```typescript
[1, ▌2, 3]
```

## Superfluous Whitespace
```typescript
[▌
 1, 2, 3]
```
```typescript
[
 ▌1, 2, 3]
```

```typescript
[
 1,
 ▌ 
 2, 3]
```
```typescript
[
 1,
  
 ▌2, 3]
```

## Superfluous Commas
```typescript
[1, ▌2,,,]
```
```typescript
❎
```

# TypeScript Backward Move
## 1-3 Items
```typescript
[1, ▌2, 3]
```
```typescript
[▌1, 2, 3]
```

```typescript
[1, 2, ▌3]
```
```typescript
[1, ▌2, 3]
```

## Array Level
```typescript
foo();
▌[1, 2, 3]
```
```typescript
▌foo();
[1, 2, 3]
```


## Separators
```typescript
[1, 2▌, 3]
```
```typescript
[1, ▌2, 3]
```

## Superfluous Whitespace
```typescript
[1, 2, 3
▌
]
```
```typescript
[1, 2, ▌3

]
```

```typescript
[
 1,
 ▌ 
 2, 3]
```
```typescript
[
 ▌1,
  
 2, 3]
```

## Superfluous Commas
```typescript
[1, 2,,,▌ ]
```
```typescript
[1, ▌2,,, ]
```

```typescript
[,,,▌1]
```
```typescript
❎
```

# TypeScript Downward Move
## 1-3 Items
```typescript
▌[1, 2, 3]
```
```typescript
[▌1, 2, 3]
```

## No Items
```typescript
▌[]
```
```typescript
❎
```

## Separators
```typescript
[1▌, 2, 3]
```
```typescript
[▌1, 2, 3]
```

## Superfluous Whitespace
```typescript
[▌
 1, 2, 3]
```
```typescript
[
 ▌1, 2, 3]
```

```typescript
[
 1,
 ▌ 
 2, 3]
```
```typescript
[
 ▌1,
  
 2, 3]
```

## Superfluous Commas
```typescript
[1, 2,,▌,]
```
```typescript
[▌1, 2,,,]
```
