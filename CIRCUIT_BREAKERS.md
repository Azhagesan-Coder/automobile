# Downstream circuit breakers

CustomerManagement protects inventory lookups. SalesManagement protects customer and inventory validation independently, plus the Twilio SMS provider. ReportManagement protects sales retrieval and inventory enrichment independently. Local database and filesystem operations are outside these circuits.

Each service owns independent, thread-safe Resilience4j circuits. Only the remote operation is recorded; input validation, missing references, and stock checks retain their existing behavior. HTTP 4xx responses are ignored except 429. Provider failures and timeouts count as failures. Open circuits return HTTP 503; ordinary dependency failures retain HTTP 502. Reports fail if a source is unavailable rather than returning incomplete totals. SMS is never retried automatically.

Defaults: a count-based window of 20 calls, at least 10 calls before evaluation, 50% failure threshold, 30 seconds open, and 3 probes in half-open. The next call after the open interval starts recovery. Configure each service locally or through Config Server:

```yaml
downstream:
  circuit:
    window-size: 20
    minimum-calls: 10
    failure-rate: 50
    open-seconds: 30
    half-open-calls: 3
```

Existing REST connect/read timeouts and the customer lookup deadline remain in effect. A circuit breaker does not cancel an in-flight operation. Twilio uses its SDK transport timeouts.

The API gateway has one circuit per backend route using Spring Cloud CircuitBreaker. HTTP 500, 502, 503 and 504 count as failures; fallback returns HTTP 503. Authentication remains required. Gateway thresholds use `resilience4j.circuitbreaker.configs.default` in its application configuration. Transport deadlines bound requests; the asynchronous time limiter is disabled to avoid abandoning write requests while they continue executing.

References: [Resilience4j](https://resilience4j.readme.io/docs/circuitbreaker) and [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/reference/4.3/spring-cloud-gateway-server-webmvc/filters/circuitbreaker-filter.html).
