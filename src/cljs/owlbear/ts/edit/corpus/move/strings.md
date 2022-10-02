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
"Hello, World!"
▌foo();
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

# TypeScript Backward Move
## Escape Sequences
```typescript
"\"▌\""
```
```typescript
"▌\"\""
```

```typescript
'\'▌\''
```
```typescript
'▌\'\''
```

```typescript
`\`▌\``
```
```typescript
`▌\`\``
```

## First Fragment
```typescript
foo();
"▌Hello, World!"
```
```typescript
▌foo();
"Hello, World!"
```

## Multiple Fragments
```typescript
"a ▌b c"
```
```typescript
"▌a b c"
```

```typescript
'a ▌b c'
```
```typescript
'▌a b c'
```

```typescript
`a ▌b c`
```
```typescript
`▌a b c`
```

## Template String Substitution
```typescript
`Hello, ${▌name}!`
```
```typescript
`▌Hello, ${name}!`
```

```typescript
`${`Hello, ▌World!`}`
```
```typescript
`${`▌Hello, World!`}`
```

```typescript
`Hello, World!`
▌${}
```
```typescript
▌`Hello, World!`
${}
```

# TypeScript Downward Move
## Escape Sequences
```typescript
"▌\"\""
```
```typescript
❎
```

```typescript
'▌\'\''
```
```typescript
❎
```

```typescript
`▌\`\``
```
```typescript
❎
```

##  Single Fragment
```typescript
▌"Hello"
```
```typescript
"▌Hello"
```

## Multiple Fragments
```typescript
▌"a b c"
```
```typescript
"▌a b c"
```

```typescript
▌'a b c'
```
```typescript
'▌a b c'
```

```typescript
▌`a b c`
```
```typescript
`▌a b c`
```

## Whitespace
```typescript
"a ▌ b"
```
```typescript
"▌a  b"
```

## Template String Substitution
```typescript
`Hello, ▌${name}!`
```
```typescript
`Hello, ${▌name}!`
```

```typescript
`${▌`Hello, World!`}`
```
```typescript
`${`▌Hello, World!`}`
```

# TypeScript Upward Move
## Escape Sequences
```typescript
"▌\"\""
```
```typescript
▌"\"\""
```

```typescript
'▌\'\''
```
```typescript
▌'\'\''
```

```typescript
`▌\`\``
```
```typescript
▌`\`\``
```

##  Single Fragment
```typescript
"▌Hello"
```
```typescript
▌"Hello"
```

## Multiple Fragments
```typescript
"▌a b c"
```
```typescript
▌"a b c"
```

```typescript
'▌a b c'
```
```typescript
▌'a b c'
```

```typescript
`▌a b c`
```
```typescript
▌`a b c`
```

## Whitespace
```typescript
"a ▌ b"
```
```typescript
❎
```

## Template String Substitution
```typescript
`Hello, ▌${name}!`
```
```typescript
▌`Hello, ${name}!`
```

```typescript
`${▌`Hello, World!`}`
```
```typescript
`▌${`Hello, World!`}`
```