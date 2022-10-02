# TypeScript Forward Move
## Root JSX
```tsx
▌

<h1>Hello, World!</h1>
```
```tsx


▌<h1>Hello, World!</h1>
```

## Sibling Elements
```tsx
const foo = (
  <section>
    ▌<h1>Hello, World!</h1>
    <h2>The name is Zabip!</h2>
  </section>
);
```
```tsx
const foo = (
  <section>
    <h1>Hello, World!</h1>
    ▌<h2>The name is Zabip!</h2>
  </section>
);
```

## Superfluous Whitespace
```tsx
const foo = (
  ▌
  <section>
    <h1>Hello, World!</h1>
  </section>
);
```
```tsx
const foo = (
  
  ▌<section>
    <h1>Hello, World!</h1>
  </section>
);
```

```tsx
const foo = (
  <section>
    ▌
    <h1>Hello, World!</h1>
  </section>
);
```
```tsx
const foo = (
  <section>
    
    ▌<h1>Hello, World!</h1>
  </section>
);
```

```tsx
const foo = (
  <>
    <section>
	    ▌
	    <h1>Hello, World!</h1>
	    <h2>The name is Zabip!</h2>
    </section>
    <div></div>
  </>
);
```
```tsx
const foo = (
  <>
    <section>
	    
	    ▌<h1>Hello, World!</h1>
	    <h2>The name is Zabip!</h2>
    </section>
    <div></div>
  </>
);
```

# TypeScript Backward Move
## Root JSX
```tsx
<h1>Hello, World!</h1>


▌
```
```tsx
▌<h1>Hello, World!</h1>



```

## Sibling Elements
```tsx
const foo = (
  <section>
    <h1>Hello, World!</h1>
    ▌<h2>The name is Zabip!</h2>
  </section>
);
```
```tsx
const foo = (
  <section>
    ▌<h1>Hello, World!</h1>
    <h2>The name is Zabip!</h2>
  </section>
);
```

## Superfluous Whitespace
```tsx
const foo = (
  <section>
    <h1>Hello, World!</h1>
  </section>
  ▌
);
```
```tsx
const foo = (
  ▌<section>
    <h1>Hello, World!</h1>
  </section>
  
);
```

```tsx
const foo = (
  <section>
    <h1>Hello, World!</h1>
    ▌
  </section>
);
```
```tsx
const foo = (
  <section>
    ▌<h1>Hello, World!</h1>
    
  </section>
);
```

```tsx
const foo = (
  <>
    <section>
	    <h1>Hello, World!</h1>
	    <h2>The name is Zabip!</h2>
	    ▌
    </section>
    <div></div>
  </>
);
```
```tsx
const foo = (
  <>
    <section>
	    <h1>Hello, World!</h1>
	    ▌<h2>The name is Zabip!</h2>
	    
    </section>
    <div></div>
  </>
);
```

# TypeScript Downward Move
## Root JSX
```tsx
▌

<h1>Hello, World!</h1>
```
```tsx
❎
```

## Element With Children
```tsx
const foo = (
  ▌<section>
    <h1>Hello, World!</h1>
  </section>
);
```
```tsx
const foo = (
  <section>
    ▌<h1>Hello, World!</h1>
  </section>
);
```

```tsx
const foo = (
  <section>
    ▌<h1>Hello, World!</h1>
  </section>
);
```
```tsx
const foo = (
  <section>
    <h1>▌Hello, World!</h1>
  </section>
);
```

## No Children
```typescript
const foo = (▌<div></div>)
```
```typescript
❎
```

## Superfluous Whitespace
```tsx
const foo = (
  ▌
  <section>
    <h1>Hello, World!</h1>
  </section>
);
```
```tsx
const foo = (
  
  ▌<section>
    <h1>Hello, World!</h1>
  </section>
);
```

```tsx
const foo = (
  <section>
    ▌
    <h1>Hello, World!</h1>
  </section>
);
```
```tsx
const foo = (
  <section>
    
    ▌<h1>Hello, World!</h1>
  </section>
);
```

```tsx
const foo = (
  <>
    <section>
	    ▌
	    <h1>Hello, World!</h1>
	    <h2>The name is Zabip!</h2>
    </section>
    <div></div>
  </>
);
```
```tsx
const foo = (
  <>
    <section>
	    
	    ▌<h1>Hello, World!</h1>
	    <h2>The name is Zabip!</h2>
    </section>
    <div></div>
  </>
);
```

# TypeScript Upward Move
## Root JSX
```tsx
▌

<h1>Hello, World!</h1>
```
```tsx
❎
```

## Element With Parent
```tsx
const foo = (
  <section>
    ▌<h1>Hello, World!</h1>
  </section>
);
```
```tsx
const foo = (
  ▌<section>
    <h1>Hello, World!</h1>
  </section>
);
```

```tsx
const foo = (
  <section>
    <h1>▌Hello, World!</h1>
  </section>
);
```
```tsx
const foo = (
  <section>
    ▌<h1>Hello, World!</h1>
  </section>
);
```

## Superfluous Whitespace
```tsx
const foo = (
  ▌
  <section>
    <h1>Hello, World!</h1>
  </section>
);
```
```tsx
▌const foo = (
  
  <section>
    <h1>Hello, World!</h1>
  </section>
);
```

```tsx
const foo = (
  <section>
    ▌
    <h1>Hello, World!</h1>
  </section>
);
```
```tsx
const foo = ▌(
  <section>
    
    <h1>Hello, World!</h1>
  </section>
);
```

```tsx
const foo = (
  <>
    <section>
	    ▌
	    <h1>Hello, World!</h1>
	    <h2>The name is Zabip!</h2>
    </section>
    <div></div>
  </>
);
```
```tsx
const foo = (
  ▌<>
    <section>
	    
	    <h1>Hello, World!</h1>
	    <h2>The name is Zabip!</h2>
    </section>
    <div></div>
  </>
);
```