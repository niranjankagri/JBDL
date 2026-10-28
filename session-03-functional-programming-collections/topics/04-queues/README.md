# Topic 4: Introduction to queues

A **queue** holds elements waiting to be processed. Usually FIFO (first in, first out); a priority
queue processes by priority instead. A **deque** ("deck", double-ended queue) can add and remove at
both ends, so it works as a queue *and* as a stack (LIFO).

## Two sets of methods

| Operation | Throws on failure | Returns a special value |
|---|---|---|
| Insert | `add(e)` | `offer(e)` → `false` if a bounded queue is full |
| Remove the head | `remove()` | `poll()` → `null` if empty |
| Look at the head | `element()` | `peek()` → `null` if empty |

Deque adds `addFirst/addLast`, `pollFirst/pollLast`, `peekFirst/peekLast`, and the stack methods
`push` (= `addFirst`) and `pop` (= `removeFirst`).

## Implementations

| Class | Order | Notes |
|---|---|---|
| `ArrayDeque` | FIFO or LIFO | Resizable array, the default choice for a queue **or a stack**; no `null`s |
| `LinkedList` | FIFO | Also a `List`; more memory per element |
| `PriorityQueue` | Smallest first (natural order or a `Comparator`) | Binary heap: O(log n) offer/poll; iteration order is **not** sorted; equal priorities in no guaranteed order |
| `ArrayBlockingQueue`, `LinkedBlockingQueue` | FIFO | Thread-safe; `put`/`take` wait when full/empty. The base of producer-consumer and thread pools (Session 5) |

Avoid `java.util.Stack`: it extends `Vector`, is synchronized on every call and exposes list methods
that break the stack idea. Use `ArrayDeque`.

## The code

| File | What it shows |
|---|---|
| [`SupportQueue.java`](../../src/main/java/jbdl/session03/queues/SupportQueue.java) | `PriorityQueue` with a `Comparator`: CRITICAL before HIGH before LOW, and a sequence number so equal severities keep arrival order; `offer`/`peek`/`poll` |
| [`BrowserHistory.java`](../../src/main/java/jbdl/session03/queues/BrowserHistory.java) | Two `ArrayDeque` stacks for back/forward; a new visit clears forward |
| [`RecentSearches.java`](../../src/main/java/jbdl/session03/queues/RecentSearches.java) | A bounded deque: newest first, repeats move to the front, the oldest is dropped |
| [`CollectionExamples.drainQueue`](../../src/main/java/jbdl/session03/collections/CollectionExamples.java) | (2024 code) FIFO vs priority order for the same input |
| [`QueuesTest.java`](../../src/test/java/jbdl/session03/queues/QueuesTest.java) | Priority with tie-breaker, `null` from an empty queue, back/forward, bounded history |

```
submit: typo(LOW) payments(CRITICAL) search(HIGH) login(CRITICAL)
drain:  payments → login → search → typo
```

## Interview questions

- **`poll` vs `remove`?** Both remove the head; on an empty queue `poll` returns `null`, `remove` throws.
- **Is a `PriorityQueue` sorted?** Only the head is guaranteed; iterating shows heap order.
- **Stack in Java?** `Deque<Integer> stack = new ArrayDeque<>(); stack.push(1); stack.pop();`
- **What is a `BlockingQueue` for?** Handing work between threads: producers `put`, consumers `take`, both wait when needed.
