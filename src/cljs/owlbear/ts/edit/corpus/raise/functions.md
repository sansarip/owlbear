# TypeScript Raise
## Declarations
```ts
function foo() {
	▌bar();
}
```
```ts
▌bar();
```

## Nameless
```ts
function () {
  ▌bar();
}
```
```ts
▌bar();
```

## Nested
```ts
function foo () {
	return function () {
		▌bar();
	}
}
```
```ts
function foo () {
	return ▌bar();
}
```