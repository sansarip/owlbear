# TypeScript Raise
## Lone Statement Block
```typescript
{
  ▌a();
}
```
```typescript
▌a();
```

```typescript
{
  ▌a()
}
```
```typescript
▌a()
```

```typescript
{
  ▌1
}
```
```typescript
▌1
```

## Function Statement Block
```typescript
function foo () ▌{
  return null;
}
```
```typescript
▌{
  return null;
}
```

## While-Loop Statement Block
```typescript
while (true) ▌{
  console.log(new Date() + "")
}
```
```typescript
▌{
  console.log(new Date() + "")
}
```

```typescript
while (true) {
  ▌console.log(new Date() + "")
}
```
```typescript
▌console.log(new Date() + "")
```

## For-Loop Statement Block
```typescript
for (i of l) ▌{
  console.log(new Date() + "")
}
```
```typescript
▌{
  console.log(new Date() + "")
}
```

## Switch Statement Block
This *case* doesn't really make too much sense, but it works 🤷‍♂️
```typescript
switch (a) ▌{
  case "a":
    break;
}
```
```typescript
▌{
  case "a":
    break;
}
```

```typescript
switch (a) {
  case "a":
    ▌foo();
    break;
}
```
```typescript
▌foo();
```
