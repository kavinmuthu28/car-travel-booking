# KAVIN TRAVELS – Automated Testing Suite Documentation

## 1. Overview & Setup
An automated test suite was constructed from the ground up for the KAVIN TRAVELS platform. The suite leverages:
- **Framework**: JUnit 5 (Jupiter 5.10.3)
- **Mocking**: Mockito 5.11.0 with `mock-maker-subclass` configuration for full Java 25 / byte-buddy compatibility
- **Spring Integration**: Spring Boot Test (`@WebMvcTest`, `@MockBean`, `MockMvc`)
- **Execution Engine**: Maven Surefire Plugin 3.2.5

To run the automated suite:
```powershell
cd C:\travels\backend
& "C:\Users\kavin\radioconda\Library\bin\mvn.cmd" test
```

---

## 2. Test Execution Summary

| Test Class | Category | Tests Run | Passed | Failed | Skipped | Time (s) |
|---|---|---|---|---|---|---|
| `JwtServiceTest` | Security / Token | 3 | 3 | 0 | 0 | 0.174 |
| `AuthServiceTest` | Authentication & Registration | 4 | 4 | 0 | 0 | 0.188 |
| `BookingServiceTest` | Business Logic & Security | 6 | 6 | 0 | 0 | 0.163 |
| `FareCalculationServiceTest` | Pricing & Estimations | 4 | 4 | 0 | 0 | 0.012 |
| `PaymentServiceTest` | Ledger & Payment Security | 3 | 3 | 0 | 0 | 0.056 |
| `UserServiceTest` | Authorization & Profile Access | 3 | 3 | 0 | 0 | 0.015 |
| `BookingControllerTest` | REST Endpoints & Status Codes | 2 | 2 | 0 | 0 | 3.915 |
| **Total** | | **25** | **25** | **0** | **0** | **4.523** |

---

## 3. Test Cases Implemented

### 3.1 JwtServiceTest
- `testGenerateAndParseToken`: Generates token from email, ensures valid 3-part structure, parses subject, and validates token.
- `testValidateToken_TamperedToken_ReturnsFalse`: Verifies signature validation fails when payload/signature is modified.
- `testValidateToken_MalformedString_ReturnsFalse`: Tests rejection of garbage strings and empty inputs.

### 3.2 AuthServiceTest
- `testRegister_Success`: Validates normal registration, BCrypt password hashing, and JWT token issuance.
- `testRegister_DuplicateEmail_ThrowsDuplicateResource`: Verifies duplicate email registration is rejected with conflict.
- `testRegister_DuplicatePhone_ThrowsDuplicateResource`: Verifies duplicate phone number registration is rejected.
- `testLogin_Success`: Verifies authentication manager invocation and successful token delivery.

### 3.3 BookingServiceTest
- `testCreateBooking_PastDate_ThrowsInvalidBookingException` (BUG-012): Verifies bookings with past pickup dates are rejected before any DB persistence.
- `testCreateBooking_CarAlreadyBooked_ThrowsCarUnavailable` (BUG-010): Verifies car double-booking prevention on same date.
- `testGetBookingById_WrongUser_ThrowsUnauthorized` (BUG-003): Tests that Customer B attempting to retrieve Customer A's booking by ID throws `UnauthorizedException`.
- `testCancelBooking_AlreadyCompleted_ThrowsInvalidBookingException` (BUG-019): Prevents cancellation of completed trips.
- `testCancelBooking_AlreadyCancelled_ThrowsInvalidBookingException` (BUG-019): Prevents repeated cancellation of cancelled trips.
- `testCreateBooking_Success`: Verifies valid booking creation generates a `KM-` prefixed unique ID and sets `CONFIRMED` status.

### 3.4 FareCalculationServiceTest
- `testCalculateTotalFare_OneWay`: Tests 1.0x trip multiplier plus base driver allowance.
- `testCalculateTotalFare_RoundTrip`: Tests 2.0x trip multiplier plus driver allowance.
- `testEstimateFare`: Tests full breakdown response DTO calculation.
- `testNullTripTypeFallback`: Ensures null trip type defaults safely to ONE_WAY without null pointer exceptions.

### 3.5 PaymentServiceTest
- `testProcessPayment_CustomerBPayForCustomerABooking_ThrowsUnauthorized` (BUG-008): Verifies customer cannot pay for a booking belonging to someone else.
- `testProcessPayment_IgnoresClientPaymentStatus_ForcesInitiated` (BUG-009): Verifies backend enforces `INITIATED` status even if client sends `SUCCESSFUL`.
- `testGetPaymentById_UnauthorizedCustomer_ThrowsUnauthorized` (BUG-007): Verifies payment IDOR protection.

### 3.6 UserServiceTest
- `testGetUserById_UnauthorizedCustomer_ThrowsUnauthorized` (BUG-006): Verifies customer cannot read another customer's profile.
- `testGetUserById_SelfAccess_Success`: Verifies customer can read their own profile.
- `testGetUserById_AdminAccess_Success`: Verifies `ROLE_ADMIN` can inspect user profiles.

### 3.7 BookingControllerTest
- `testEstimateFareEndpoint`: Verifies `POST /api/bookings/estimate-fare` is publicly accessible and returns 200 OK with correct JSON payload.
- `testGetAllBookingsEndpoint`: Verifies `GET /api/bookings` mapping and serialization.
