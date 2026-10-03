# KAVIN TRAVELS – Application Audit Baseline

## 1. Executive Summary & Original State
- **Project**: KAVIN TRAVELS – Car Travel Booking System
- **Repository**: [https://github.com/kavinmuthu28/car-travel-booking](https://github.com/kavinmuthu28/car-travel-booking)
- **Local Workspace**: `C:\travels`
- **Initial Commit Hash**: `4f5110de0b2b527d0f65558e10233dbe0fc3cc2b`
- **Audit Branch**: `full-audit-bug-fixes`
- **Technology Stack**:
  - **Backend**: Spring Boot 3.3.4, Java 17 (tested on JDK 25 runtime), Spring Security 6, Spring Data JPA, Hibernate, JJWT 0.12.6, Maven
  - **Database**: MySQL 8.x (`car_travel_booking`)
  - **Frontend**: Vanilla HTML5, CSS3, JavaScript (Fetch API, SessionStorage, Leaflet OpenStreetMap)
  - **Test Suite**: JUnit 5, Mockito 5.11 (subclass mock-maker), Spring Boot Test, MockMvc

---

## 2. Original Application Architecture & File Mapping

```
car-travel-booking/
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/cartravel/booking/
│       │   ├── CarTravelBookingApplication.java
│       │   ├── config/ (AdminUserInitializer, CorsConfig, GoogleMapsConfig, SecurityConfig)
│       │   ├── controller/ (AdminController, AuthController, BookingController, CarController, DestinationController, PaymentController, UserController)
│       │   ├── dto/ (auth, booking, car, destination, payment, user)
│       │   ├── entity/ (Booking, Car, Destination, Payment, User)
│       │   ├── enums/ (BookingStatus, PaymentMethod, PaymentStatus, TripType, UserRole + JPA Converters)
│       │   ├── exception/ (Custom exceptions + GlobalExceptionHandler)
│       │   ├── repository/ (BookingRepository, CarRepository, DestinationRepository, PaymentRepository, UserRepository)
│       │   ├── security/ (CustomUserDetailsService, JwtAuthenticationFilter, JwtService)
│       │   └── service/ (AuthService, BookingService, CarAvailabilityService, CarService, DestinationService, FareCalculationService, PaymentService, RouteService, UserService)
│       └── resources/
│           ├── application.properties
│           └── application-example.properties
├── database/
│   ├── queries/ (debugging-queries.sql, useful-queries.sql)
│   ├── sample-data/ (sample-data.sql)
│   └── schema/ (car_travel_booking.sql)
├── frontend/
│   ├── index.html, booking.html, cars.html, confirmation.html, my-bookings.html, login.html, register.html, contact.html
│   ├── admin/ (admin-login.html, dashboard.html, bookings.html, cars.html, users.html, payments.html, and redirect stubs)
│   ├── css/ (admin, auth, booking, cars, confirmation, footer, global, home, my-bookings, navbar, responsive, style)
│   └── js/ (admin, api, auth, booking, cars, config, confirmation, google-maps, home, location-selector, main, my-bookings, data/locations)
└── docs/
    ├── api-documentation.md
    ├── booking-flow.md
    ├── database-design.md
    └── google-maps-setup.md
```

---

## 3. Frontend-to-Backend API Mapping

| Frontend Page / Component | JS File | Backend Endpoint | HTTP Method | Auth Required | Purpose |
|---|---|---|---|---|---|
| `login.html`, `admin/admin-login.html` | `auth.js` | `/api/auth/login` | POST | No | User/Admin authentication |
| `register.html` | `auth.js` | `/api/auth/register` | POST | No | Customer sign up |
| Global Navbar / Session Check | `auth.js`, `main.js` | `/api/auth/me` | GET | Yes | Load active user session |
| `index.html`, `cars.html` | `home.js`, `cars.js` | `/api/cars` | GET | No | Browse fleet catalog |
| `cars.html` (filters) | `cars.js` | `/api/cars/available` | GET | No | Check car availability by date |
| `booking.html` (estimate) | `booking.js` | `/api/bookings/estimate-fare` | POST | No | Calculate trip pricing |
| `booking.html` (submit) | `booking.js` | `/api/bookings` | POST | Yes | Create customer booking |
| `confirmation.html` | `confirmation.js` | `/api/bookings/{id}` | GET | Yes | Fetch booking confirmation |
| `confirmation.html` (payment) | `confirmation.js` | `/api/payments` | POST | Yes | Record booking payment |
| `my-bookings.html` | `my-bookings.js` | `/api/bookings/my` *(fixed)* | GET | Yes | List customer trips |
| `my-bookings.html` (cancel) | `my-bookings.js` | `/api/bookings/{id}/cancel` | PUT | Yes | Cancel reserved trip |
| `admin/dashboard.html` | `admin.js` | `/api/admin/stats` | GET | Yes (ADMIN) | Admin KPIs & summary |
| `admin/bookings.html` | `admin.js` | `/api/bookings` | GET | Yes (ADMIN) | Admin trip management |
| `admin/bookings.html` (status) | `admin.js` | `/api/bookings/{id}/status` | PUT | Yes (ADMIN) | Admin change trip status |
| `admin/cars.html` (CRUD) | `admin.js` | `/api/cars`, `/api/cars/{id}` | POST/PUT/DEL | Yes (ADMIN) | Fleet management |
| `admin/users.html` | `admin.js` | `/api/users` | GET | Yes (ADMIN) | User directory |
| `admin/payments.html` | `admin.js` | `/api/payments` | GET | Yes (ADMIN) | Financial ledger audit |

---

## 4. Baseline Risks Identified Prior to Fixes
1. **Critical Credential Exposure**: Plaintext root database password (`nivak@0328`) committed to repository in `application.properties`.
2. **Critical Admin Hardcoding**: Default admin credentials (`kavinmuthu84@gmail.com` / `kavinhari@03`) hardcoded in `AdminUserInitializer.java`.
3. **High IDOR Vulnerabilities**: Insecure Direct Object References in `BookingController`, `UserController`, and `PaymentController`.
4. **Data Integrity Risks**: Lack of server-side past-date validation in bookings, payment status tampering via client payload, and JPQL enum query string mismatch in `BookingRepository`.
5. **Information Leakage**: Unhandled exceptions leaking internal system stack traces in `GlobalExceptionHandler`.
6. **Zero Pre-existing Automated Tests**: Initial repository had 0 unit or integration tests.
