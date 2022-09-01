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
```
```typescript
1 + ▌1;
```

```typescript
1 + ▌1;
bar();
```
```typescript
1 + 1;
▌bar();
```

## Call Expressions
```typescript
▌foo();
```
```typescript
foo▌();
```

```typescript
foo▌();
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
```
```typescript
const foo = 1 + ▌1;
```

```typescript
const foo = 1 + ▌1;
bar();
```
```typescript
const foo = 1 + 1;
▌bar();
```

## Arrow Functions
```typescript
▌() => {}
```
```typescript
() => ▌{}
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

## Generator Function Declarations
```typescript
▌function* foo () {}
bar();
```
```typescript
function* foo () {}
▌bar();
```

```typescript
function* foo () {
  ▌yield 1 + 1;
  bar();
}
```
```typescript
function* foo () {
  yield 1 + 1;
  ▌bar();
}
```

## Member Expressions
```typescript
foo().bar▌();
baz();
```
```typescript
foo().bar();
▌baz();
```

# TypeScript Backward Move
## Arrow Functions
```typescript
foo();
▌() => {}
```
```typescript
▌foo();
() => {}
```

## Async Expressions
```typescript
const foo = ▌await bar();
```
```typescript
const ▌foo = await bar();
```

## Binary Expressions
```typescript
1 + ▌1;
```
```typescript
▌1 + 1;
```

## Call Expressions
```typescript
foo();
▌bar();
```
```typescript
▌foo();
bar();
```

## Lexical Declarations
```typescript
bar();
▌const foo = 1 + 1;
```
```typescript
▌bar();
const foo = 1 + 1;
```

```typescript
const foo = ▌1 + 1;
```
```typescript
const ▌foo = 1 + 1;
```

## Function Declarations
```typescript
function foo () {}
▌bar();
```
```typescript
▌function foo () {}
bar();
```

```typescript
function foo ▌() {}
```
```typescript
function ▌foo () {}
```

```typescript
function foo () ▌{}
```
```typescript
function foo ▌() {}
```

```typescript
bar();
function ▌foo () {}
```
```typescript
▌bar();
function foo () {}
```

## Generator Function Declarations
```typescript
function* foo () {}
▌bar();
```
```typescript
▌function* foo () {}
bar();
```

```typescript
function* foo () {
  yield 1 + 1;
  ▌bar();
}
```
```typescript
function* foo () {
  ▌yield 1 + 1;
  bar();
}
```

## Member Expressions
```typescript
foo().bar().▌baz();
```
```typescript
▌foo().bar().baz();
```

```typescript
foo().▌bar().baz();
```
```typescript
▌foo().bar().baz();
```
