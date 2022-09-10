# TypeScript Forward Move
## Comment Blocks
```typescript
▌/* Ullamco laboris nisi ut aliquid ex ea commodi consequat. */
foo();
```
```typescript
/* Ullamco laboris nisi ut aliquid ex ea commodi consequat. */
▌foo();
```

```typescript
/*▌Ullamco laboris nisi ut aliquid ex ea commodi consequat.*/
foo();
```
```typescript
/*Ullamco laboris nisi ut aliquid ex ea commodi consequat.*/
▌foo();
```

## Comments
```typescript
▌// Quo usque tandem abutere, Catilina, patientia nostra?
foo();
```
```typescript
// Quo usque tandem abutere, Catilina, patientia nostra?
▌foo();
```

# TypeScript Backward Move
## Comment Blocks
```typescript
/* Ullamco laboris nisi ut aliquid ex ea commodi consequat. */
▌foo();
```
```typescript
▌/* Ullamco laboris nisi ut aliquid ex ea commodi consequat. */
foo();
```

```typescript
foo();
/*▌Ullamco laboris nisi ut aliquid ex ea commodi consequat.*/
```
```typescript
▌foo();
/*Ullamco laboris nisi ut aliquid ex ea commodi consequat.*/
```

## Comments
```typescript
foo();
▌// Quo usque tandem abutere, Catilina, patientia nostra?
```
```typescript
▌foo();
// Quo usque tandem abutere, Catilina, patientia nostra?
```

# TypeScript Downward Move
## Comment Blocks
```typescript
▌/* Ullamco laboris nisi ut aliquid ex ea commodi consequat. */
```
```typescript
/*▌ Ullamco laboris nisi ut aliquid ex ea commodi consequat. */
```

```typescript
/*▌Ullamco laboris nisi ut aliquid ex ea commodi consequat.*/
```
```typescript
❎
```

## Comments
```typescript
▌// Quo usque tandem abutere, Catilina, patientia nostra?
```
```typescript
❎
```