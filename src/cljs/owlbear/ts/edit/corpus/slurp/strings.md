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

## Strings Meet Strings
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
${▌} 
foo
```
```typescript
${ 
foo▌}
```