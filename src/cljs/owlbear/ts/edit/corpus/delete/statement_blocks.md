# TypeScript Backward Delete
## Empty Statement Blocks
```ts
{}▌
```
```ts
{▌}
```

```ts
{▌}
```
```ts
▌
```

```ts
function foo() {▌}
```
```ts
function foo() ▌
```

```ts
while (true) {▌}
```
```ts
while (true) ▌
```

```ts
for (i of l) {▌}
```
```ts
for (i of l) ▌
```

```ts
switch (a) {▌}
```
```ts
switch (a) ▌
```

## Nonempty Statement Blocks
```ts
{
  console.log("Hello, World!")
}▌
```
```ts
{
  console.log("Hello, World!")
▌}
```

```ts
{▌
  console.log("Hello, World!")
}
```
```ts
▌{
  console.log("Hello, World!")
}
```

```ts
{
  const foo = 1▌;
}
```
```ts
{
  const foo = ▌;
}
```

```ts
function foo() {
	return "Hello, World!"
}▌
```
```ts
function foo() {
	return "Hello, World!"
▌}
```

```ts
while (true) {
	console.log(new Date() + "")
}▌
```
```ts
while (true) {
	console.log(new Date() + "")
▌}
```

```ts
for (i of l) {
	console.log(i)
}▌
```
```ts
for (i of l) {
	console.log(i)
▌}
```

```ts
switch (a) {
	case "a":
		return 1;
}▌
```
```ts
switch (a) {
	case "a":
		return 1;
▌}
```
