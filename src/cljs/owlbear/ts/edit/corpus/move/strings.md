# TypeScript Forward Move
## Escape Sequences
```typescript
"▌\"\""
```
```typescript
"\"▌\""
```

```typescript
'▌\'\''
```
```typescript
'\'▌\''
```

```typescript
`▌\`\``
```
```typescript
`\`▌\``
```

## Last Fragment
```typescript
"Hello, ▌World!"
foo();
```
```typescript
❎
```

## Multiple Fragments
```typescript
"▌a b c"
```
```typescript
"a ▌b c"
```

```typescript
'▌a b c'
```
```typescript
'a ▌b c'
```

```typescript
`▌a b c`
```
```typescript
`a ▌b c`
```

## Template String Substitution
```typescript
`Hello, ▌${name}!`
```
```typescript
`Hello, ${name}▌!`
```

```typescript
`${`▌Hello, World!`}`
```
```typescript
`${`Hello, ▌World!`}`
```

```typescript
▌`Hello, World!`
${}
```
```typescript
`Hello, World!`
▌${}
```