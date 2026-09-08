# ?? KAVIN TRAVELS — Car Travel & Booking Platform

A modern, full-stack car rental and outstation travel booking platform designed for journeys across South India. Features dynamic Leaflet routing, comprehensive Tamil Nadu 38-district transit datasets, categorized location search, JWT authentication, and an executive administration portal.

---

## ?? Project Structure

```text
car-travel-booking/
+-- frontend/                 # Customer & Admin Web Interface
¦   +-- index.html            # Homepage with quick ride search & popular routes
¦   +-- cars.html             # Fleet catalog with multi-facet filters
¦   +-- booking.html          # 4-step booking wizard with Leaflet map routing
¦   +-- confirmation.html     # Real-time booking confirmation receipt
¦   +-- login.html            # Customer authentication
¦   +-- register.html         # New customer registration
¦   +-- my-bookings.html      # Customer bookings dashboard & cancellation
¦   +-- contact.html          # 24/7 customer support & inquiry form
¦   +-- admin/                # Executive Administration Portal
¦   ¦   +-- admin-login.html  # Secure Admin portal entry
¦   ¦   +-- dashboard.html    # Real-time business metrics & stats
¦   ¦   +-- cars.html         # Fleet CRUD management
¦   ¦   +-- bookings.html     # Live booking tracking & status updates
¦   ¦   +-- users.html        # Customer accounts ledger
¦   ¦   +-- payments.html     # Transaction ledger & status
¦   +-- css/                  # Modular responsive stylesheets
¦   +-- js/                   # Central API, auth, booking, & admin logic
¦   ¦   +-- data/locations.js # 38 Tamil Nadu districts & tourist hubs
¦   +-- images/               # Optimized assets & hero backgrounds
¦
+-- backend/                  # Spring Boot 3 REST API Backend
¦   +-- pom.xml               # Maven dependencies (Security, JPA, MySQL, JWT)
¦   +-- src/                  # Controllers, Entities, DTOs, Services, Repositories
¦
+-- database/                 # MySQL Database Scripts
¦   +-- schema/               # DDL table creation scripts
¦   +-- sample-data/          # Seed data for fleet and routes
¦   +-- queries/              # Debugging & operational SQL queries
¦
+-- docs/                     # Architecture & API documentation
+-- .gitignore                # Git exclusion rules
+-- README.md                 # Project overview & documentation
```

---

## ?? Key Features

* **38-District Tamil Nadu Location System**: Instant searchable dropdown covering all 38 districts, major airports, railway junctions, and hill stations.
* **Interactive Leaflet & OpenStreetMap Routing**: Visual polyline route rendering, auto-bounding, and accurate driving distance / duration calculations.
* **From/To Location Swap**: One-click swap button with smooth animation that dynamically re-routes and recalculates trip distance.
* **4-Step Booking Wizard**: Location selection ? Schedule & Passengers ? Vehicle Selection ? Review & Confirmation.
* **Stateless JWT Security**: Secure customer and administrator authentication with Spring Security and BCrypt password hashing.
* **Admin Management Portal**: Real-time fleet CRUD, booking status transitions, customer accounts, and revenue tracking.
* **Fully Responsive UI**: Optimized for mobile, tablet, and ultra-wide desktop displays.

---

## ?? Quick Start

### 1. Database Setup
1. Create a MySQL database named `car_travel_booking`.
2. Execute `database/schema/car_travel_booking.sql` followed by `database/sample-data/sample-data.sql`.

### 2. Backend Setup
1. Configure database credentials in `backend/src/main/resources/application.properties` (or set `DB_USERNAME` and `DB_PASSWORD` environment variables).
2. Run the Spring Boot application:
   ```bash
   mvn clean spring-boot:run
   ```

### 3. Frontend Setup
Open `frontend/index.html` in any modern web browser or serve with a local static web server (e.g., Live Server or Nginx).

---

## ?? License
© 2026 KAVIN TRAVELS. All rights reserved.
