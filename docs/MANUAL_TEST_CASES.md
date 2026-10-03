# KAVIN TRAVELS – Manual QA Test Cases & Execution Matrix

## 1. Test Overview
A total of **30 manual test scenarios** were formulated and evaluated across the functional, negative, boundary, and session testing categories for KAVIN TRAVELS.

---

## 2. Test Execution Matrix

| Test Case ID | Module | Test Scenario | Preconditions | Test Steps | Expected Result | Actual Result | Status | Related Bug |
|---|---|---|---|---|---|---|---|---|
| **TC-MAN-001** | Auth | Register with unique credentials | Unregistered email & phone | 1. Navigate to `register.html`<br>2. Fill name, email, password, phone<br>3. Submit form | Account created, redirected to login / logged in with token stored | Registration successful, token issued | **PASS** | - |
| **TC-MAN-002** | Auth | Register with existing email | User already in system | 1. Fill registration with duplicate email<br>2. Submit form | HTTP 409 Conflict, UI displays error toast | Error shown: "Email already in use." | **PASS** | - |
| **TC-MAN-003** | Auth | Register with existing phone | User with phone exists | 1. Fill registration with duplicate phone<br>2. Submit form | HTTP 409 Conflict, UI displays error toast | Error shown: "Phone number already in use." | **PASS** | - |
| **TC-MAN-004** | Auth | Login with valid customer credentials | Registered customer account | 1. Enter email & password on `login.html`<br>2. Submit | Token saved to `sessionStorage`, navbar updates with name | Logged in successfully, navbar shows user name | **PASS** | - |
| **TC-MAN-005** | Auth | Login with invalid password | Registered account | 1. Enter valid email with wrong password<br>2. Submit | HTTP 401 Unauthorized, toast shows "Invalid email or password" | Rejected with 401 error message | **PASS** | - |
| **TC-MAN-006** | Auth | Customer logout | Active logged-in session | 1. Click logout in navbar | `sessionStorage` cleared, redirected to home | Session cleared immediately | **PASS** | - |
| **TC-MAN-007** | Booking | View car catalog on home and cars page | Backend running | 1. Open `index.html` or `cars.html` | Available vehicles rendered with prices and images | All cars load and display accurately | **PASS** | - |
| **TC-MAN-008** | Booking | Search & select Tamil Nadu pickup location | On `booking.html` | 1. Type "Coimbatore" in pickup search<br>2. Select from dropdown | Location input populated, map marker placed | Dropdown suggests hubs, marker set | **PASS** | - |
| **TC-MAN-009** | Booking | Select destination location | Pickup selected | 1. Type "Ooty" in dropoff search<br>2. Select option | Dropoff set, route plotted, distance calculated (~86km) | Map draws polyline route, distance displayed | **PASS** | - |
| **TC-MAN-010** | Booking | Select same pickup and dropoff | Booking form open | 1. Set pickup = "Coimbatore"<br>2. Set dropoff = "Coimbatore" | Validation alert / 0 km boundary warning | Handled gracefully, alerts user | **PASS** | - |
| **TC-MAN-011** | Booking | Swap pickup and dropoff locations | Both locations set | 1. Click swap icon button | Pickup and dropoff locations inverted on map and form | Locations reversed, route recalculated | **PASS** | - |
| **TC-MAN-012** | Booking | Round trip fare estimation | Valid locations selected | 1. Switch trip mode to Round Trip | Fare recalculates with 2.0x multiplier + driver allowance | Fare doubles base amount + allowance | **PASS** | - |
| **TC-MAN-013** | Booking | Past travel date submission | Logged in | 1. Force yesterday's date in pickup date<br>2. Submit booking | Backend rejects with HTTP 400 Bad Request / InvalidBookingException | Rejected with "Pickup date cannot be in the past" | **PASS** | BUG-012 |
| **TC-MAN-014** | Booking | Car already booked on travel date | Car reserved for Date X | 1. Try booking same car on Date X | Backend returns HTTP 409 Conflict | Error toast: "Car is already booked on this date" | **PASS** | BUG-010 |
| **TC-MAN-015** | Booking | Valid booking creation | Logged in customer | 1. Select car, date, route<br>2. Submit booking | Booking persisted, returns unique `KM-` ID, redirected to confirmation | Booking created with unique number | **PASS** | BUG-013 |
| **TC-MAN-016** | Confirmation | View booking confirmation details | Booking created | 1. Open confirmation page with booking ID | Correct route, date, car, and fare rendered | All details match booking request | **PASS** | - |
| **TC-MAN-017** | Confirmation | Customer B attempts to view Customer A's confirmation | Customer B logged in | 1. Customer B opens `confirmation.html?id=<CustomerA_BookingId>` | HTTP 403 Forbidden, redirected or blocked | Access denied, customer cannot view other's booking | **PASS** | BUG-003 |
| **TC-MAN-018** | Payments | Customer processes payment for own booking | Active booking confirmed | 1. Select payment mode (UPI)<br>2. Submit payment | Payment record created with status `INITIATED` | Payment saved, receipt displayed | **PASS** | BUG-009 |
| **TC-MAN-019** | Payments | Customer B attempts to process payment for Customer A booking | Customer B logged in | 1. Send `POST /api/payments` with Customer A's booking ID | HTTP 403 Forbidden | Request rejected with "Access denied" | **PASS** | BUG-008 |
| **TC-MAN-020** | My Bookings | Customer views trip history | Logged in customer | 1. Open `my-bookings.html` | Lists only current customer's trips via `/api/bookings/my` | Only current customer trips listed | **PASS** | BUG-002 |
| **TC-MAN-021** | My Bookings | Filter trips by status (CONFIRMED, CANCELLED) | Trips with various statuses | 1. Click status tabs | List filters accurately | Instant filter by tab | **PASS** | - |
| **TC-MAN-022** | My Bookings | Cancel active booking | PENDING or CONFIRMED trip | 1. Click Cancel button<br>2. Confirm dialog | Booking status changes to CANCELLED | Status updated to CANCELLED | **PASS** | - |
| **TC-MAN-023** | My Bookings | Attempt to cancel already cancelled booking | Booking is CANCELLED | 1. Send cancel request for cancelled booking | HTTP 400 Bad Request | Error: "Booking is already cancelled" | **PASS** | BUG-019 |
| **TC-MAN-024** | My Bookings | Customer B attempts to cancel Customer A's booking | Customer B logged in | 1. Send `PUT /api/bookings/{CustomerA_Id}/cancel` | HTTP 403 Forbidden | Access denied | **PASS** | BUG-003 |
| **TC-MAN-025** | Admin | Customer attempts to access `/api/admin/stats` | Logged in as customer | 1. Request `/api/admin/stats` with customer token | HTTP 403 Forbidden | Access Denied | **PASS** | - |
| **TC-MAN-026** | Admin | Customer attempts to fetch `/api/bookings` | Logged in as customer | 1. Request `GET /api/bookings` | HTTP 403 Forbidden | Access Denied | **PASS** | BUG-004 |
| **TC-MAN-027** | Admin | Customer attempts to fetch `/api/users` | Logged in as customer | 1. Request `GET /api/users` | HTTP 403 Forbidden | Access Denied | **PASS** | BUG-005 |
| **TC-MAN-028** | Admin | Admin login and dashboard load | Valid admin credentials | 1. Login at `admin-login.html`<br>2. Open `dashboard.html` | Stats render total cars, bookings, users, revenue | Admin stats displayed properly | **PASS** | - |
| **TC-MAN-029** | Session | Direct access to protected page when unauthenticated | No token in sessionStorage | 1. Directly open `my-bookings.html` | Toast displays "Please log in", redirects to `login.html` | Clean redirect to login | **PASS** | - |
| **TC-MAN-030** | Session | 401 session expiry redirect from admin page | Invalid/expired token | 1. Trigger 401 error from `admin/dashboard.html` | Redirects to absolute `.../admin/admin-login.html` | Redirects to correct admin login path | **PASS** | BUG-018 |
