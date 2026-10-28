# Topic 4: Hashing algorithms

A **hash function** turns a value into a number. Two very different uses:

| | For lookup (`hashCode`) | Cryptographic (SHA-256) |
|---|---|---|
| Output | 32-bit `int` | 256 bits (64 hex digits) |
| Goal | Fast, spreads keys evenly over buckets | One-way, practically collision-free |
| Collisions | Expected and handled (`"Aa"` and `"BB"` share a hash) | Infeasible to find |
| Used for | `HashMap`, `HashSet` | Checksums, signatures, content addressing |

`String.hashCode` is `s[0]·31^(n-1) + s[1]·31^(n-2) + … + s[n-1]` with `int` overflow; the test checks
our own version against it.

## How HashMap uses the hash

[`SimpleHashMap`](../../src/main/java/jbdl/session04/hashing/SimpleHashMap.java) is a small hash map
written from scratch, the same design as `java.util.HashMap`:

```
key ── hashCode() ──▶ h ── h ^ (h >>> 16) ──▶ hash ── hash & (capacity - 1) ──▶ bucket index
                          mix high bits into        capacity is a power of two, so this
                          the low bits              is a fast "hash mod capacity"

buckets: [0] → null
         [1] → (key7, 7) → (key3, 3) → null      ← collisions chained in a list
         [2] → (key1, 1) → null
         ...
```

1. **`put`** finds the bucket, walks its chain comparing `hash` first (cheap) and `equals` only when
   hashes match; replaces the value if the key exists, otherwise adds a node.
2. **Resize:** when `size > capacity × 0.75` (the load factor), the table doubles and every entry is
   placed again. The test adds 13 keys: 13 > 16 × 0.75 = 12, so capacity becomes 32.
3. **Java 8+:** `HashMap` turns a chain longer than 8 into a red-black tree, so a bad case is
   O(log n) instead of O(n).

## The `equals`/`hashCode` contract in practice

| Situation | Effect (tested) |
|---|---|
| Good `hashCode` | Longest chain ≤ 3 for 13 keys: O(1) lookups |
| Constant `hashCode` (`BadKey` returns 42) | Still **correct**, but all 100 keys in one chain: O(n) lookups |
| Key changed after `put` (`MutableKey.rename`) | `get` looks in the bucket of the *new* hash: returns `null`; the entry is lost inside the map |
| `equals` overridden without `hashCode` | Equal keys land in different buckets: duplicates, failed lookups |

So: keys should be **immutable** (`String`, `Integer`, records), and `equals` and `hashCode` must use
the same fields.

## The code

| File | What it shows |
|---|---|
| [`SimpleHashMap.java`](../../src/main/java/jbdl/session04/hashing/SimpleHashMap.java) | Buckets, hash spreading, index by mask, chaining, resize at 0.75 |
| [`HashingFacts.java`](../../src/main/java/jbdl/session04/hashing/HashingFacts.java) | `String.hashCode` formula, a colliding key, a mutable key, SHA-256 |
| [`HashingTest.java`](../../src/test/java/jbdl/session04/hashing/HashingTest.java) | Formula, index, resize, 100-long chain, lost mutable key, SHA-256 of `"abc"` |

## Interview questions

- **How does `HashMap.get` work?** hash → bucket → walk the chain/tree comparing hash then `equals`.
- **What is the load factor?** Fill ratio that triggers a resize (0.75: a balance of memory and collisions).
- **Why is the capacity a power of two?** `hash & (n - 1)` is a fast modulo, and resize splits each bucket into two predictable ones.
- **What changed in Java 8?** Long chains become trees (treeify threshold 8).
- **Can two unequal objects have the same hash code?** Yes, that's a collision. Equal objects must have the same hash code.
- **Why not store passwords with SHA-256?** It's fast, so it's fast to brute-force; use a slow, salted hash (bcrypt, Session 16).
