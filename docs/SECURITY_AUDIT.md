# KAVIN TRAVELS – Security Audit Report

## 1. Security Scope & Methodology
This audit evaluated the Spring Boot backend, Spring Security 6 integration, JWT implementation, REST API endpoints, and client-side credential storage mechanisms of the KAVIN TRAVELS application. Testing covered OWASP Top 10 web application vulnerabilities including Authentication/Session Management, Broken Access Control (IDOR), Cryptographic Failures, Injection, and Insecure Design.

---

## 2. Summary of Findings

| Finding ID | Vulnerability Title | Category | Severity | Initial Status | Final Status |
|---|---|---|---|---|---|
| SEC-001 | Hardcoded Database Password in Configuration | Sensitive Data Exposure | Critical | Confirmed | **FIXED** |
| SEC-002 | Hardcoded Default Admin Account Credentials | Hardcoded Credentials | Critical | Confirmed | **FIXED** |
| SEC-003 | IDOR in User Profile Fetch Endpoint | Broken Access Control | High | Confirmed | **FIXED** |
| SEC-004 | IDOR & Unauthorized Global Booking Data Exposure | Broken Access Control | High | Confirmed | **FIXED** |
| SEC-005 | Unauthorized User Directory Exposure | Broken Access Control | High | Confirmed | **FIXED** |
| SEC-006 | IDOR & Financial Payment Record Exposure | Broken Access Control | High | Confirmed | **FIXED** |
| SEC-007 | Client-Side Payment Status Forgery | Insecure Design | High | Confirmed | **FIXED** |
| SEC-008 | Cross-Customer Payment Processing for Arbitrary Bookings | Broken Access Control | High | Confirmed | **FIXED** |
| SEC-009 | Internal Error Trace & System Information Leakage | Information Disclosure | Medium | Confirmed | **FIXED** |
| SEC-010 | SQL Logging Enabled in Production Configuration | Sensitive Data Exposure | Low | Confirmed | **FIXED** |
| SEC-011 | Hibernate Open-In-View Anti-Pattern | Performance / DOS Risk | Low | Confirmed | **FIXED** |

---

## 3. Detailed Security Findings & Remediation

### SEC-001: Hardcoded Database Password in Configuration
- **Severity**: Critical (CVSS 9.1)
- **Description**: `backend/src/main/resources/application.properties` contained the root MySQL password `nivak@0328` hardcoded in Git.
- **Impact**: Any repository collaborator or leak compromises the database directly.
- **Remediation**:
  - Removed plaintext password default from `application.properties`.
  - Configured environment variable reference `${DB_PASSWORD}`.
  - Provided `application-example.properties` template with sanitized placeholder values.

### SEC-002: Hardcoded Default Admin Account Credentials
- **Severity**: Critical (CVSS 9.0)
- **Description**: `AdminUserInitializer.java` automatically seeded or updated an admin user with email `kavinmuthu84@gmail.com` and password `kavinhari@03`.
- **Impact**: Default credentials are known to anyone reading source code, giving instantaneous root administrative access to bookings, cars, users, and financials.
- **Remediation**:
  - Removed hardcoded strings from `AdminUserInitializer.java`.
  - Admin seeding now only triggers if both `${ADMIN_EMAIL}` and `${ADMIN_PASSWORD}` environment variables are explicitly supplied by system administrators.

### SEC-003: Insecure Direct Object Reference (IDOR) on User Profile
- **Severity**: High (CVSS 7.5)
- **Endpoint**: `GET /api/users/{id}`
- **Description**: Any authenticated customer could provide any numeric ID to view other users' names, emails, phone numbers, and creation dates.
- **Remediation**:
  - Enforced ownership verification in `UserService.getUserById(id, callerEmail)`.
  - Customers can only read their own profile; `ROLE_ADMIN` users can inspect user profiles.

### SEC-004: IDOR & Unauthorized Global Booking Data Exposure
- **Severity**: High (CVSS 8.5)
- **Endpoints**: `GET /api/bookings`, `GET /api/bookings/{id}`, `GET /api/bookings/user/{userId}`
- **Description**:
  1. `GET /api/bookings` returned all customer bookings to any logged-in user without role checks.
  2. `GET /api/bookings/user/{userId}` permitted any customer to harvest another customer's complete trip itinerary.
  3. `GET /api/bookings/{id}` permitted arbitrary lookup of bookings by ID.
- **Remediation**:
  - Restricted `GET /api/bookings` and `GET /api/bookings/user/{userId}` to `hasRole('ADMIN')`.
  - Created a dedicated, secure self-service endpoint `GET /api/bookings/my` that derives the customer ID solely from the authenticated JWT principal.
  - Added strict ownership validation in `BookingService.getBookingById(id, email)`.

### SEC-005: Unauthorized User Directory Exposure
- **Severity**: High (CVSS 7.5)
- **Endpoint**: `GET /api/users`
- **Description**: Endpoint returned full user list to any logged-in user, leaking PII of all customers.
- **Remediation**: Added `@PreAuthorize("hasRole('ADMIN')")` on `UserController.getAllUsers()`.

### SEC-006: IDOR & Financial Payment Record Exposure
- **Severity**: High (CVSS 7.5)
- **Endpoints**: `GET /api/payments`, `GET /api/payments/{id}`
- **Description**: Any user could view all financial payment records or query individual transaction amounts and IDs without authorization.
- **Remediation**:
  - Restricted `GET /api/payments` to `hasRole('ADMIN')`.
  - Added ownership check in `PaymentService.getPaymentById(id, email)` verifying that the payment's associated booking belongs to the caller or admin.

### SEC-007: Client-Side Payment Status Forgery
- **Severity**: High (CVSS 8.1)
- **Endpoint**: `POST /api/payments`
- **Description**: The payment creation endpoint trusted the client's `paymentStatus` field. A malicious client could send `"paymentStatus": "SUCCESSFUL"` directly, falsifying completed payments without actual transaction clearing.
- **Remediation**:
  - The backend now overrides the payment status on creation, unconditionally setting it to `PaymentStatus.INITIATED`.
  - Real status transitions should occur exclusively through verified gateway webhook callbacks.

### SEC-008: Cross-Customer Payment Processing for Arbitrary Bookings
- **Severity**: High (CVSS 7.5)
- **Endpoint**: `POST /api/payments`
- **Description**: The endpoint did not verify that the customer initiating the payment owned the target booking ID.
- **Remediation**: Added validation verifying `booking.getUser().getId().equals(caller.getId())` before recording a payment.

### SEC-009: Internal Error Trace & System Information Leakage
- **Severity**: Medium (CVSS 5.3)
- **Description**: `GlobalExceptionHandler.handleGeneral` returned `ex.getMessage()` directly to clients upon HTTP 500 errors, revealing internal SQL queries, file paths, and runtime class details.
- **Remediation**: Generic error message `"An unexpected error occurred. Please try again later."` returned to clients; full stack trace logged internally with `log.error()`.

---

## 4. Retest & Verification Results
All remediations were tested using automated unit and security test suites (`AuthServiceTest`, `BookingServiceTest`, `PaymentServiceTest`, `UserServiceTest`, `JwtServiceTest`, `BookingControllerTest`). All 25 automated tests pass with 0 failures.
