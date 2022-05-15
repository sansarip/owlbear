# TypeScript Forward Slurp
## Empty Block
```typescript
{▌}
a
```
```typescript
{
a▌}
```

```typescript
function foo {▌}
a
```
```typescript
function foo {
a▌}
```

```typescript
const foo = () => {▌};
a
```
```typescript
const foo = () => {
a▌};
```

```typescript
while (true) {▌}
a
```
```typescript
while (true) {
a▌}
```

```typescript
for (i of l) {▌}
a
```
```typescript
for (i of l) {
a▌}
```

## Variable Declaration
```typescript
{▌}
var foo = 2;
```
```typescript
{
var foo = 2;▌}
```