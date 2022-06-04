# TypeScript Forward Barf
## Basic Strings
```typescript
"a▌"
```
```typescript
"▌"a
```

```typescript
'a▌'
```
```typescript
'▌'a
```

```typescript
`a▌`
```
```typescript
`▌`a
```

## String Fragments
```typescript
"Hello, world!▌"
```
```typescript
"Hello, ▌"world!
```

## Strings Within Strings
```typescript
"\"\"▌"
```
```typescript
"\"▌""
```

```typescript
'\'\'▌'
```
```typescript
'\'▌''
```

```typescript
`\`\`▌`
```
```typescript
`\`▌``
```

## Template String Substitution
```typescript
`Hello, ${name▌}`
```
```typescript
`Hello, ${▌}name`
```

```typescript
`${`Hello, World!`▌}`
```
```typescript
`${▌}\`Hello, World!\``
```

```typescript
`${`Hello, ${`convoluted World`}`▌}`
```
```typescript
`${▌}\`Hello, ${`convoluted World`}\``
```

```typescript
`▌Hello, ${name}`
```
```typescript
`▌Hello, `${name}
```