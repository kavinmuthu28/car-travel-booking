# KAVIN TRAVELS – Payment Integration & Lifecycle Audit

## 1. Overview & Architectural Findings
The KAVIN TRAVELS platform contains payment recording structures, including:
- **Entity**: `com.cartravel.booking.entity.Payment`
- **Controller**: `com.cartravel.booking.controller.PaymentController`
- **Service**: `com.cartravel.booking.service.PaymentService`
- **Database Table**: `payments`
- **Frontend Interaction**: `confirmation.html` and `confirmation.js` (Simulated checkout flow)

### Critical Finding: Gateway Integration Reality
> [!IMPORTANT]
> The current system **does NOT integrate with a live third-party payment gateway** (such as Razorpay, Stripe, PayU, or Cashfree). It functions as an **internal payment ledger and simulation module**.
> The application creates local payment transaction records and persists them directly into the MySQL database. Do not represent the application as possessing live banking or PCI-DSS certified gateway integration.

---

## 2. Payment Lifecycle & Flow

```
[Customer Confirmation Page]
          │
          ▼
Select Payment Mode (UPI / NetBanking / Card / Cash)
          │
          ▼
POST /api/payments { bookingId, paymentMethod, amount, transactionId }
          │
          ├── Authorization Check: Does caller own bookingId? (SEC-008 Fix)
          │     ├── NO  ──> 403 Forbidden
          │     └── YES ──> Proceed
          │
          ├── Status Setting: Server forces status = INITIATED (SEC-007 Fix)
          │
          ▼
Save Payment Record (MySQL: `payments` table)
          │
          ▼
Return PaymentResponseDTO { id, transactionId, paymentStatus: 'INITIATED', ... }
```

---

## 3. Discovered Vulnerabilities & Remediation

### 1. Insecure Status Setting by Client (Fixed)
- **Original Behavior**: Client sent `"paymentStatus": "SUCCESSFUL"` in JSON body, and the backend persisted it directly.
- **Remediation**: The backend now forces `paymentStatus = PaymentStatus.INITIATED` regardless of client request values.

### 2. Lack of Booking Ownership Check (Fixed)
- **Original Behavior**: Any authenticated user could submit a payment for any booking ID in the database.
- **Remediation**: `PaymentService.processPayment` now queries the associated `Booking` and verifies that `booking.getUser().getId().equals(caller.getId())` (or caller is `ROLE_ADMIN`).

### 3. Payment Record Snooping via IDOR (Fixed)
- **Original Behavior**: `GET /api/payments/{id}` and `GET /api/payments` exposed transaction amounts, methods, and booking associations to any user.
- **Remediation**:
  - `GET /api/payments` restricted to `hasRole('ADMIN')`.
  - `GET /api/payments/{id}` restricted to the booking owner or `ROLE_ADMIN`.

---

## 4. Production Roadmap for Real Gateway Integration
To transition from the current ledger simulation to a production payment gateway:
1. **Order Creation**: Create gateway order on backend (e.g. Razorpay Order API) and return order ID to frontend.
2. **Checkout UI**: Launch gateway modal (Razorpay Checkout / Stripe Elements) with order token.
3. **Webhook Callback**: Implement a cryptographically verified webhook endpoint (`/api/payments/webhook`) validating HMAC-SHA256 signatures before updating payment status to `SUCCESSFUL`.
4. **Idempotency**: Implement idempotency keys to prevent duplicate transactions under network retries.
