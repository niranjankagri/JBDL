# Topic 3: Core HTTP methods

| Method | Meaning | Safe (no change) | Idempotent (repeat = same effect) | Body | Typical success |
|---|---|---|---|---|---|
| `GET` | Read a resource or a list | ✓ | ✓ | no | 200 OK |
| `POST` | Create (or run an action) | | ✗ (two POSTs = two books) | yes | 201 Created + `Location` |
| `PUT` | Replace the whole resource | | ✓ | yes | 200 OK (or 204) |
| `PATCH` | Change part of a resource | | not guaranteed | yes | 200 OK |
| `DELETE` | Remove | | ✓ (state-wise) | no | 204 No Content |
| `HEAD` | Like GET, headers only | ✓ | ✓ | no | 200 |
| `OPTIONS` | Which methods are allowed | ✓ | ✓ | no | 200 + `Allow` |

**Safe** methods don't change state, so caches and crawlers may call them freely. **Idempotent**
methods can be retried after a timeout without fear: that's why clients and gateways retry `GET`,
`PUT` and `DELETE` but not `POST`.

## Status codes used

| Code | When |
|---|---|
| 200 OK | Successful GET, PUT, PATCH |
| 201 Created | POST created something; `Location` says where |
| 204 No Content | DELETE succeeded; nothing to return |
| 400 Bad Request | Invalid body (validation) |
| 404 Not Found | No book with that id |
| 405 Method Not Allowed | e.g. `POST /api/books/1` |
| 415 Unsupported Media Type | Body isn't JSON |
| 409 Conflict / 422 Unprocessable | Business rule violations (later sessions) |
| 401 / 403 | Not logged in / not allowed (Session 16) |
| 500 | A bug on the server |

## Real exchanges with the running app

```
$ curl -i -X POST localhost:18080/api/books -H 'Content-Type: application/json' \
       -d '{"title":"Clean Code","author":"Robert C. Martin","isbn":"9780132350884","price":450.00,"publishedYear":2008}'
HTTP/1.1 201
Location: http://localhost:18080/api/books/1
Content-Type: application/json

{"id":1,"title":"Clean Code","author":"Robert C. Martin","isbn":"9780132350884","price":450.00,"publishedYear":2008}

$ curl -X PATCH localhost:18080/api/books/1 -H 'Content-Type: application/json' -d '{"price":399.50}'
{"id":1,"title":"Clean Code",...,"price":399.50,"publishedYear":2008}

$ curl -o /dev/null -w "%{http_code}\n" -X DELETE localhost:18080/api/books/1     → 204
$ curl -o /dev/null -w "%{http_code}\n" -X DELETE localhost:18080/api/books/1     → 404

$ curl -i -X OPTIONS localhost:18080/api/books
Allow: POST,GET,HEAD,OPTIONS
```

Spring answers `HEAD` and `OPTIONS` automatically from the mappings.

## Tests

[`BookApiTest`](../../src/test/java/jbdl/session08/BookApiTest.java) checks each method's status and
body, sends the same PUT twice and expects the same state (idempotent), deletes twice (204 then 404),
and gets 405 and 415 from Spring itself.

## Interview questions

- **PUT vs PATCH?** Replace the whole resource vs change some fields.
- **PUT vs POST?** PUT to a known URL, idempotent; POST to a collection, server assigns the id, not idempotent.
- **Is DELETE idempotent if the second call returns 404?** Yes: idempotency is about the server state (the book is gone either way), not the response code.
- **Which methods are safe?** GET, HEAD, OPTIONS (and TRACE).
- **401 vs 403?** Not authenticated vs authenticated but not allowed.
