# TypeScript Forward Slurp
## Empty String
```typescript
"▌"
a
```
```typescript
"
a▌"
```

```typescript
'▌'
a
```
```typescript
'
a▌'
```

```typescript
`▌`
a
```
```typescript
`
a▌`
```

## Nested Strings
```typescript
"▌"
""
```
```typescript
"
\"\"▌"
```

```typescript
"▌"
1 " + " 1 " = " 2
```
```typescript
"
1 \" + \" 1 \" = \" 2▌"
```

```typescript
"▌"
"\"\""
```
```typescript
"
\"\"\"\"▌"
```

```typescript
"▌"
''
```
```typescript
"
''▌"
```

```typescript
'▌'
''
```
```typescript
'
\'\'▌'
```

```typescript
`▌`
``
```
```typescript
`
\`\`▌`
```

```typescript
`▌`
`${foo()}`
```
```typescript
`
\`${foo()}\`▌`
```

## Template String Substitution
```typescript
`${▌} foo`
```
```typescript
`${ foo▌}`
```

```typescript
`▌`
${}
```
```typescript
`
${}▌`
```

```typescript
`▌`
${`Hello!`}
```
```typescript
`
${`Hello!`}▌`
```

```typescript
${▌} 
foo
```
```typescript
${ 
foo▌}
```