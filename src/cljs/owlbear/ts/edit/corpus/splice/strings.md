# TypeScript Splice
## Multiple Fragment
```typescript
▌"a b c"
```
```typescript
▌a b c
```

```typescript
▌'a b c'
```
```typescript
▌a b c
```

```typescript
▌`a b c`
```
```typescript
▌a b c
```

## Nested Strings
```typescript
▌"\"\""
```
```typescript
▌""
```

```typescript
▌'\'\''
```
```typescript
▌''
```

```typescript
▌`\`\``
```
```typescript
▌``
```

## Template String Substitution
```typescript
`Hello, $▌{name}`
```
```typescript
`Hello, ▌name`
```

```typescript
`$▌{`Hello, World!`}`
```
```typescript
`\▌`Hello, World!\``
```

```typescript
`$▌{`Hello, ${`convoluted World`}`}`
```
```typescript
`\▌`Hello, ${`convoluted World`}\``
```

```typescript
`Hello, ▌${name}`
```
```typescript
`Hello, ▌name`
```