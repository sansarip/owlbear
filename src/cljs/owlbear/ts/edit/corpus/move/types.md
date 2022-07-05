# TypeScript Forward Move
## Declaration
```typescript
type foo = ▌null;

bar();
```
```typescript
type foo = null;

▌bar();
```

```typescript
type foo = ▌string;

bar();
```
```typescript
type foo = string;

▌bar();
```

```typescript
type foo = ▌{a: string};

bar();
```
```typescript
type foo = {a: string};

▌bar();
```

```typescript
▌type foo = {a: string};

bar();
```
```typescript
type foo = {a: string};

▌bar();
```

## Object Value
```typescript
type foo = {a: ▌number; b: string;}
```
```typescript
type foo = {a: number; ▌b: string;}
```

```typescript
type foo = {a: ▌b; c: d;}
```
```typescript
type foo = {a: b; ▌c: d;}
```

## Superfluous Whitespace
```typescript
type foo = {▌
  a: number;
  b: string;
} 
```
```typescript
type foo = {
  ▌a: number;
  b: string;
} 
```

```typescript
type foo = {

▌
  a: number;
  b: string;
} 
```
```typescript
type foo = {


  ▌a: number;
  b: string;
} 
```

```typescript
type foo = {
  a: number;
  ▌
  b: string;
} 
```
```typescript
type foo = {
  a: number;
  
  ▌b: string;
} 
```

```typescript
type foo = {
  a: number;
  b: 
  ▌
  string;
}
```
```typescript
type foo = {
  a: number;
  b: 
  
  ▌string;
}
```

## Type Key
```typescript
type foo = {▌a: number}
```
```typescript
type foo = {a: ▌number}
```