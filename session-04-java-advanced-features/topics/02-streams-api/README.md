# Topic 2: The Streams API

A stream is a **pipeline** over a source of data:

```
source            intermediate operations (lazy, return a new Stream)          terminal operation (runs it)
orders.stream() → filter → map → distinct → sorted → ...                    → toList / collect / sum / findFirst
```

- **Lazy:** nothing runs until the terminal operation. Short-circuit operations (`findFirst`,
  `anyMatch`, `limit`) stop as soon as they have the answer. The test records which orders were
  looked at: for `firstCancelled` only orders 1, 2, 3, not 4 and 5.
- **Single use:** a second terminal operation on the same stream throws `IllegalStateException`.
- **Doesn't change the source:** it produces new results.

## Operations used in the code

| Operation | Kind | Example in [`OrderReports`](../../src/main/java/jbdl/session04/streams/OrderReports.java) |
|---|---|---|
| `filter(Predicate)` | intermediate | only delivered orders |
| `map(Function)`, `mapToLong` | intermediate | order → customer; order → total |
| `flatMap(order -> order.items().stream())` | intermediate | all line items of all orders, one stream |
| `distinct`, `sorted(comparator)`, `limit(n)` | intermediate | unique names A-Z; top 2 orders by value |
| `peek` | intermediate | debugging only (records visits for the laziness test) |
| `toList()` (Java 16) | terminal | unmodifiable list |
| `sum`, `count`, `max(comparator)` | terminal | revenue; best-selling product |
| `findFirst` | terminal, short-circuit | first cancelled order |
| `collect(groupingBy(key, downstream))` | terminal | revenue per city (`summingLong`), orders per status (`counting`) |
| `collect(partitioningBy(predicate))` | terminal | big vs small orders: always a `true` and a `false` key |
| `collect(joining(", ", "[", "]"))` | terminal | `[Bengaluru, Delhi, Pune]` |

## Results on the test data

Five orders ([`TestOrders`](../../src/test/java/jbdl/session04/testdata/TestOrders.java)), one cancelled:

| Report | Result |
|---|---|
| Delivered customers | `[Asha, Kiran, Meena]` |
| Revenue (without cancelled) | 10,500 |
| Revenue by city | `{Bengaluru=6000, Delhi=2100, Pune=2400}` |
| Orders ≥ 2,000 | `true=[1, 4], false=[2, 3, 5]` |
| Best-selling product | `pen` (10 + 45 + 30) |
| Top 2 by value | `[4, 1]` |

## Streams vs loops

Streams read like the question ("revenue per city of non-cancelled orders") and combine well. A loop
is still fine, and clearer when you need early `break`s with complex state, checked exceptions, or
to change local variables (lambdas can't).

## Interview questions

- **Intermediate vs terminal?** Intermediate returns a stream and is lazy; terminal produces a result and runs the pipeline.
- **`map` vs `flatMap`?** `map`: one element → one element. `flatMap`: one element → a stream, flattened into one stream.
- **`groupingBy` vs `partitioningBy`?** Any number of keys vs exactly `true`/`false`.
- **`toList()` vs `collect(Collectors.toList())`?** `toList()` is unmodifiable; `Collectors.toList()` gives no guarantee (an `ArrayList` today).
- **Can you reuse a stream?** No. Create a new one from the source.
- **What is `Optional` for?** A result that may be absent (`findFirst`, `max`) without returning `null`.
