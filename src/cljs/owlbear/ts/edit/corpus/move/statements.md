# TypeScript Forward Move
## Async Expressions
```typescript
const foo = ▌await bar();
baz();
```
```typescript
const foo = await bar();
▌baz();
```

## Binary Expressions
```typescript
▌1 + 1;
2;
```
```typescript
1 + 1;
▌2;
```

## Call Expressions
```typescript
▌foo();
bar();
```
```typescript
foo();
▌bar();
```

## Lexical Declarations
```typescript
▌const foo = 1 + 1;
bar();
```
```typescript
const foo = 1 + 1;
▌bar();
```

```typescript
const ▌foo = 1 + 1;
```
```typescript
const foo = ▌1 + 1;
```

```typescript
const foo = ▌1 + 1;
bar();
```
```typescript
const foo = 1 + 1;
▌bar();
```

## Function Declarations
```typescript
▌function foo () {}
bar();
```
```typescript
function foo () {}
▌bar();
```

```typescript
function ▌foo () {}
```
```typescript
function foo ▌() {}
```

```typescript
function foo ▌() {}
```
```typescript
function foo () ▌{}
```

```typescript
function foo () ▌{}
bar();
```
```typescript
function foo () {}
▌bar();
```

## Member Expressions
```typescript
foo().▌bar();
baz();
```
```typescript
foo().bar();
▌baz();
```