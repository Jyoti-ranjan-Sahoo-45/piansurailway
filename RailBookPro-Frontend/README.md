# RailBook Pro — Frontend

A modern, responsive Railway Ticket Booking UI built with **HTML5, CSS3, and vanilla
JavaScript (ES6)** — no frameworks, no build step. All CRUD works entirely against
**LocalStorage**, seeded with the same sample data as the backend, so the app runs
fully standalone.

## Highlights

- Clean enterprise-style UI: sidebar, navbar, dashboard cards, tables, filters,
  rounded buttons, modals, toasts, and a notifications panel.
- FontAwesome icons (via CDN), soft color palette, responsive down to mobile.
- Role-based experience: **Passenger** and **Admin** areas.
- Form validation on every form (required, email, phone, password, dates, seat count…).
- Modular JavaScript — shared store, auth, service layer, layout shell, and a
  generic admin CRUD engine.

## Running

No build or server is strictly required — it's static files. But because pages use
relative script paths and (optionally) a future `fetch()` backend, the simplest way
is any static server:

```bash
cd RailBookPro-Frontend
# Option A: VS Code "Live Server" extension → right-click index.html → Open with Live Server
# Option B: Python
python -m http.server 5500
# then open http://localhost:5500
```

You can also just double-click `index.html` to open it directly in a browser.

## Demo credentials

| Role      | Username | Password      |
|-----------|----------|---------------|
| Admin     | `admin`  | `admin123`    |
| Passenger | `rahul`  | `password123` |
| Passenger | `priya`  | `password123` |

New passengers can also self-register via the Register page.

## Pages

### Public / Auth
- `index.html` — landing / entry (redirects if already logged in)
- `login.html` — login with Remember Me + Forgot Password (UI)
- `register.html` — passenger self-registration

### Passenger
- `dashboard.html` — cards: Upcoming Trips, Booking History, Cancelled, Favourite Routes
- `search.html` — search trains by source/destination/date/number/name
- `booking.html` — select train/date/class/passengers, live fare summary, generates PNR
- `history.html` — booking history with tabs (All / Upcoming / Completed / Cancelled) + cancel
- `profile.html` — edit profile + change password

### Admin (`admin/`)
- `dashboard.html` — 7 stat cards + recent bookings
- `users.html` — manage users (CRUD, search, role, active toggle)
- `trains.html` — manage trains (CRUD)
- `stations.html` — manage stations (CRUD)
- `routes.html` — manage routes (CRUD)
- `schedules.html` — manage schedules (CRUD, auto-computed duration)
- `bookings.html` — view/search/filter all bookings, cancel
- `reports.html` — bookings, seat occupancy, cancellation summary, users reports

## Project structure

```
RailBookPro-Frontend/
├── index.html  login.html  register.html
├── dashboard.html  search.html  booking.html  history.html  profile.html
├── admin/        (dashboard, users, trains, stations, routes, schedules, bookings, reports)
├── css/
│   └── styles.css
├── js/
│   ├── store.js       LocalStorage data layer + seed data
│   ├── utils.js       DOM helpers, toasts, formatting, validation
│   ├── auth.js        session, login/register/logout, page guards
│   ├── api.js         service layer (bookings, notifications, stats)
│   ├── layout.js      sidebar + topbar + notifications shell
│   └── admin-crud.js  generic table + modal CRUD engine
├── assets/
└── images/
```

## Data & persistence

- On first load, `store.js` seeds LocalStorage (`railbookpro_db`) with 5+ records per
  major entity (users, stations, trains, routes, schedules, bookings, notifications).
- All create/update/delete operations persist immediately to LocalStorage.
- To reset to seed data, run `Store.reset()` in the browser console, or clear site data.

## Connecting to the backend later

The frontend is intentionally decoupled. To switch from LocalStorage to the real
Spring Boot API, replace the function bodies in `js/api.js` (and the auth calls in
`js/auth.js`) with `fetch()` calls to `http://localhost:8080/api/...`, attaching the
JWT as `Authorization: Bearer <token>`. The rest of the UI stays unchanged because it
only talks to the `Api` / `Auth` modules.

## Notes

- No React / Angular / Bootstrap (per spec) — only FontAwesome via CDN for icons.
- No payment gateway or third-party booking APIs.
