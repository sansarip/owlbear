# TypeScript Kill
## Multiple Fragment
```typescript
"▌a b c"
```
```typescript
"▌ b c"
```

```typescript
'▌a b c'
```
```typescript
'▌ b c'
```

```typescript
`▌a b c`
```
```typescript
`▌ b c`
```

## Nested Strings
```typescript
"▌\"\""
```
```typescript
"▌\""
```

```typescript
'▌\'\''
```
```typescript
'▌\''
```

```typescript
`▌\`\``
```
```typescript
`▌\``
```

## Template String Substitution
```typescript
`Hello, ${▌name}`
```
```typescript
`Hello, ${▌}`
```

```typescript
`${▌`Hello, World!`}`
```
```typescript
`${▌}`
```

```typescript
`${▌`Hello, ${`convoluted World`}`}`
```
```typescript
`${▌}`
```

```typescript
`Hello, ▌${name}`
```
```typescript
`Hello, ▌`
```