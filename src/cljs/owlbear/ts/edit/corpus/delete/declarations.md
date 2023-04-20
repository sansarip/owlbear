# TypeScript Backward Delete
## Empty Functions
```ts
const foo = ()▌ => {};
```
```ts
const foo = (▌) => {};
```

```ts
const foo = (▌) => {};
```
```ts
const foo = ▌ => {};
```

```ts
const foo = () => {}▌;
```
```ts
const foo = () => {▌};
```

```ts
function foo()▌ {}
```
```ts
function foo(▌) {}
```

```ts
function foo(▌) {}
```
```ts
function foo▌ {}
```

```ts
function foo() {}▌
```
```ts
function foo() {▌}
```

```ts
function foo() {▌}
```
```ts
function foo() ▌
```

## Nonempty Functions
```ts
const foo = (a)▌ => {};
```
```ts
const foo = (a▌) => {};
```

```ts
const foo = () => {bar()}▌;
```
```ts
const foo = () => {bar()▌};
```

```ts
function foo(a)▌ {}
```
```ts
function foo(a▌) {}
```

```ts
function foo() {bar()}▌
```
```ts
function foo() {bar()▌}
```

## Empty Class Methods
```ts
class foo {
	bar()▌ {}
}
```
```ts
class foo {
	bar(▌) {}
}
```

```ts
class foo {
	bar(▌) {}
}
```
```ts
class foo {
	bar▌ {}
}
```

```ts
class foo {
	bar() {}▌
}
```
```ts
class foo {
	bar() {▌}
}
```

```ts
class foo {
	bar() {▌}
}
```
```ts
class foo {
	bar() ▌
}
```

## Nonempty Class Methods
```ts
class foo {
	bar(a)▌ {}
}
```
```ts
class foo {
	bar(a▌) {}
}
```

```ts
class foo {
	bar() {baz()}▌
}
```
```ts
class foo {
	bar() {baz()▌}
}
```

# TypeScript Forward Delete
## Empty Functions
```ts
const foo = ▌() => {};
```
```ts
const foo = (▌) => {};
```

```ts
const foo = (▌) => {};
```
```ts
const foo = ▌ => {};
```

```ts
const foo = () => ▌{};
```
```ts
const foo = () => {▌};
```

```ts
function foo▌() {}
```
```ts
function foo(▌) {}
```

```ts
function foo(▌) {}
```
```ts
function foo▌ {}
```

```ts
function foo() ▌{}
```
```ts
function foo() {▌}
```

```ts
function foo() {▌}
```
```ts
function foo() ▌
```

## Nonempty Functions
```ts
const foo = (a▌) => {};
```
```ts
const foo = (a)▌ => {};
```

```ts
const foo = () => {bar()▌};
```
```ts
const foo = () => {bar()}▌;
```

```ts
function foo(a▌) {}
```
```ts
function foo(a)▌ {}
```

```ts
function foo() {bar()▌}
```
```ts
function foo() {bar()}▌
```

## Empty Class Methods
```ts
class foo {
	bar▌() {}
}
```
```ts
class foo {
	bar(▌) {}
}
```

```ts
class foo {
	bar(▌) {}
}
```
```ts
class foo {
	bar▌ {}
}
```

```ts
class foo {
	bar() ▌{}
}
```
```ts
class foo {
	bar() {▌}
}
```

```ts
class foo {
	bar() {▌}
}
```
```ts
class foo {
	bar() ▌
}
```

## Nonempty Class Methods
```ts
class foo {
	bar▌(a) {}
}
```
```ts
class foo {
	bar(▌a) {}
}
```

```ts
class foo {
	bar(▌a) {}
}
```
```ts
class foo {
	bar(▌) {}
}
```

```ts
class foo {
	bar() {baz()▌}
}
```
```ts
class foo {
	bar() {baz()}▌
}
```