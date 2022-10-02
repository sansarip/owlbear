# TypeScript Forward Move
## Declaration
```typescript
type foo = ▌null;

bar();
```
```typescript
type foo = null;

▌bar();
```

```typescript
type foo = ▌string;

bar();
```
```typescript
type foo = string;

▌bar();
```

```typescript
type foo = ▌{a: string};

bar();
```
```typescript
type foo = {a: string};

▌bar();
```

```typescript
▌type foo = {a: string};

bar();
```
```typescript
type foo = {a: string};

▌bar();
```

## Object Value
```typescript
type foo = {a: ▌number; b: string;}
```
```typescript
type foo = {a: number; ▌b: string;}
```

```typescript
type foo = {a: ▌b; c: d;}
```
```typescript
type foo = {a: b; ▌c: d;}
```

## Superfluous Whitespace
```typescript
type foo = {▌
  a: number;
  b: string;
} 
```
```typescript
type foo = {
  ▌a: number;
  b: string;
} 
```

```typescript
type foo = {

▌
  a: number;
  b: string;
} 
```
```typescript
type foo = {


  ▌a: number;
  b: string;
} 
```

```typescript
type foo = {
  a: number;
  ▌
  b: string;
} 
```
```typescript
type foo = {
  a: number;
  
  ▌b: string;
} 
```

```typescript
type foo = {
  a: number;
  b: 
  ▌
  string;
}
```
```typescript
type foo = {
  a: number;
  b: 
  
  ▌string;
}
```

## Type Key
```typescript
type foo = {▌a: number}
```
```typescript
type foo = {a: ▌number}
```

## Function Type
```typescript
type foo▌ = () => {}
```
```typescript
type foo = ▌() => {}
```

```typescript
type foo = ▌() => {}
```
```typescript
type foo = () => ▌{}
```

```typescript
type foo = () => ▌{}
bar();
```
```typescript
type foo = () => {}
▌bar();
```

## Union Type
```typescript
type foo▌ = "hello" | "world"
```
```typescript
type foo = ▌"hello" | "world"
```

```typescript
type foo = ▌"hello" | "world"
```
```typescript
type foo = "hello" | ▌"world"
```

```typescript
type foo = "hello" |▌ "world"
```
```typescript
type foo = "hello" | ▌"world"
```

## Intersection Type
```typescript
type foo▌ = "hello" & "world"
```
```typescript
type foo = ▌"hello" & "world"
```

```typescript
type foo = ▌"hello" & "world"
```
```typescript
type foo = "hello" & ▌"world"
```

```typescript
type foo = "hello" &▌ "world"
```
```typescript
type foo = "hello" & ▌"world"
```

# TypeScript Backward Move
## Declaration
```typescript
bar();
▌type foo = null;
```
```typescript
▌bar();
type foo = null;
```

```typescript
type foo = ▌string;
```
```typescript
type ▌foo = string;
```

```typescript
type foo = ▌{a: string};
```
```typescript
type ▌foo = {a: string};
```

## Object Value
```typescript
type foo = {a: ▌number; b: string;}
```
```typescript
type foo = {▌a: number; b: string;}
```

```typescript
type foo = {a: b; ▌c: d;}
```
```typescript
type foo = {a: ▌b; c: d;}
```

## Superfluous Whitespace
```typescript
type foo = {
  a: number;
  b: string;
▌
} 
```
```typescript
type foo = {
  a: number;
  ▌b: string;

} 
```

```typescript
type foo = {
  a: number;
  ▌
  b: string;
} 
```
```typescript
type foo = {
  ▌a: number;
  
  b: string;
} 
```

```typescript
type foo = {
  a: number;
  b: 
  ▌
  string;
}
```
```typescript
type foo = {
  a: number;
  ▌b: 
  
  string;
}
```

## Type Key
```typescript
type foo = {a: ▌number}
```
```typescript
type foo = {▌a: number}
```

## Function Type
```typescript
type foo = ▌() => {}
```
```typescript
type ▌foo = () => {}
```

```typescript
type foo = () => ▌{}
```
```typescript
type foo = ▌() => {}
```

## Union Type
```typescript
type foo = ▌"hello" | "world"
```
```typescript
type ▌foo = "hello" | "world"
```

```typescript
type foo = "hello" | ▌"world"
```
```typescript
type foo = ▌"hello" | "world"
```

```typescript
type foo = "hello" | ▌ "world"
```
```typescript
type foo = ▌"hello" |  "world"
```

## Intersection Type
```typescript
type foo = ▌"hello" & "world"
```
```typescript
type ▌foo = "hello" & "world"
```

```typescript
type foo = "hello" & ▌"world"
```
```typescript
type foo = ▌"hello" & "world"
```

# TypeScript Downward Move
## Declaration
```typescript
▌type foo = null;
```
```typescript
type ▌foo = null;
```

```typescript
type foo = ▌{a: string};
```
```typescript
type foo = {▌a: string};
```

## Object Value
```typescript
type foo = {a: ▌{b: string}}
```
```typescript
type foo = {a: {▌b: string}}
```

## Superfluous Whitespace
```typescript
type foo = {▌
  a: number;
  b: string;
} 
```
```typescript
type foo = {
  ▌a: number;
  b: string;
} 
```

```typescript
type foo = {

▌
  a: number;
  b: string;
} 
```
```typescript
type foo = {


  ▌a: number;
  b: string;
} 
```

```typescript
type foo = {
  a: number;
  ▌
  b: string;
} 
```
```typescript
type foo = {
  ▌a: number;
  
  b: string;
} 
```

Newlines will render `b:` an incomplete signature and `string` a separate property signature
```typescript
type foo = {
  a: number;
  b: 
  ▌
  string;
}
```
```typescript
type foo = {
  ▌a: number;
  b: 
  
  string;
}
```

## Type Key
```typescript
type foo = {▌[string]: number}
```
```typescript
type foo = {[▌string]: number}
```

## Function Type
```typescript
type foo = ▌(a: string) => void
```
```typescript
type foo = (▌a: string) => void
```

```typescript
type foo = ▌() => void
```
```typescript
❎
```

```typescript
type foo = () => ▌{a: string}
```
```typescript
type foo = () => {▌a: string}
```

## Union Type
```typescript
type foo = ▌"hello" | "world"
```
```typescript
type foo = "▌hello" | "world"
```

```typescript
type foo = "hello" |▌ "world"
```
```typescript
type foo = ▌"hello" | "world"
```

## Intersection Type
```typescript
type foo = ▌"hello" & "world"
```
```typescript
type foo = "▌hello" & "world"
```

```typescript
type foo = "hello" &▌ "world"
```
```typescript
type foo = ▌"hello" & "world"
```

# TypeScript Upward Move
## Declaration
```typescript
type ▌foo = null;
```
```typescript
▌type foo = null;
```

```typescript
type foo = {▌a: string};
```
```typescript
type foo = ▌{a: string};
```

## Object Value
```typescript
type foo = {a: ▌{b: string}}
```
```typescript
type foo = {a▌: {b: string}}
```

## Superfluous Whitespace
```typescript
type foo = {▌
  a: number;
  b: string;
} 
```
```typescript
▌type foo = {
  a: number;
  b: string;
} 
```

```typescript
type foo = {

▌
  a: number;
  b: string;
} 
```
```typescript
▌type foo = {


  a: number;
  b: string;
} 
```

```typescript
type foo = {
  a: number;
  ▌
  b: string;
} 
```
```typescript
▌type foo = {
  a: number;
  
  b: string;
} 
```

```typescript
type foo = {
  a: number;
  b: 
  ▌
  string;
}
```
```typescript
▌type foo = {
  a: number;
  b: 
  
  string;
}
```

## Type Key
```typescript
type foo = {▌a: number}
```
```typescript
type foo = ▌{a: number}
```

## Function Type
```typescript
type foo = (▌a: string) => void
```
```typescript
type foo = ▌(a: string) => void
```

```typescript
type foo = ▌() => void
```
```typescript
▌type foo = () => void
```

```typescript
type foo = () => ▌{a: string}
```
```typescript
type foo = ▌() => {a: string}
```

## Union Type
```typescript
type foo = ▌"hello" | "world"
```
```typescript
▌type foo = "hello" | "world"
```

```typescript
type foo = "hello" |▌ "world"
```
```typescript
▌type foo = "hello" | "world"
```

## Intersection Type
```typescript
type foo = ▌"hello" & "world"
```
```typescript
▌type foo = "hello" & "world"
```

```typescript
type foo = "hello" &▌ "world"
```
```typescript
▌type foo = "hello" & "world"
```