# ADR 001: Redis Locking Strategy for Seat Reservation

## Status
Accepted

## Context
In a "Flash Sale" scenario (e.g., Avengers premiere), thousands of users may attempt to select the same seat simultaneously.
We need a mechanism to temporarily "hold" a seat while the user proceeds to payment.
Using the primary database (PostgreSQL) for temporary locks creates excessive write load and latency issues.

## Decision
We will use **Redis** as a distributed locking mechanism to handle temporary seat reservations.

### 1. Key Naming Convention
We will use a colon-separated namespace to ensure readability and prevent collisions.
* **Pattern:** `lock:seat:{seatUUID}`
* **Value:** `{userId}` (The UUID of the user holding the lock)
* **Example:** `lock:seat:550e8400-e29b-41d4-a716-446655440000` -> `user-123-uuid`

### 2. Time To Live (TTL)
The lock duration will be set to **60 Seconds**.
* **Reasoning:** 60 seconds provides sufficient time for the frontend to confirm the selection and initiate the payment intent workflow.
* **Trade-off:** Shorter times frustrate users; longer times leave inventory in "limbo" if a user abandons the cart.

### 3. Fallback Policy
If the Redis cluster is unavailable (Connection Refused / Timeout):
* **Strategy:** Fail-Fast.
* **Action:** The API will return an HTTP 503 (Service Unavailable) with a message "High traffic, please try again."
* **Reasoning:** We explicitly avoid falling back to PostgreSQL for locking. In a high-concurrency event, shifting the locking load to the persistent database risks cascading failure and data corruption.

## Consequences
* **Positive:** Extremely fast feedback for users (sub-millisecond locks). Zero load on Postgres for "browsing/selecting" seats.
* **Negative:** If Redis crashes, no new bookings can be started until it recovers (System availability is tied to Redis).