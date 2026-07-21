Use the following prompt with Claude:

---

# Prompt: Generate an Industry-Standard Railway Ticket Booking System

You are an expert Full Stack Software Architect and Senior Java Developer.

Generate a **production-quality Railway Ticket Booking System** following enterprise software development standards.

## Technology Stack

### Backend Application

* Java 22
* Spring Boot 3.x
* Spring MVC
* Spring Data JPA
* Maven
* REST APIs
* MySQL (Online Database)
* Hibernate ORM
* Bean Validation
* Lombok
* JWT Authentication
* BCrypt Password Encryption
* Global Exception Handling
* DTO Pattern
* Layered Architecture
* CORS Configuration
* Logging

### Frontend Application

Create this as a completely separate project.

Technology:

* HTML5
* CSS3
* Vanilla JavaScript (ES6)
* Fetch API
* Responsive Design
* Modular JS
* No React
* No Angular
* No Bootstrap unless absolutely required

### Database

* MySQL
* Include SQL Script
* Include sample data

---

# IMPORTANT

Create TWO completely separate applications.

Application 1
Backend (Spring Boot)

Application 2
Frontend (HTML/CSS/JS)

Do NOT integrate them together.

The frontend should be prepared to call REST APIs later, but currently use **temporary local storage (LocalStorage / Mock JS Objects)** for all CRUD operations.

The backend should also be standalone with its own sample database.

No payment gateway.

No third-party booking APIs.

---

# Project Name

RailBook Pro

---

# UI Requirements

Create a modern railway booking application.

The UI should look similar to enterprise products.

Use:

* Soft colors
* Professional typography
* Proper spacing
* Cards
* Tables
* Search filters
* Rounded buttons
* Responsive Layout
* Sidebar where appropriate
* Navbar
* Dashboard Cards
* Icons (FontAwesome)

The design must be easy to use for beginners.

---

# Authentication

Implement a proper login system.

Roles:

## Admin

Can:

* Login
* Logout
* Dashboard
* Manage trains
* Manage stations
* Manage routes
* Manage schedules
* View bookings
* Cancel bookings
* Manage users
* View reports

## Passenger

Can:

* Register
* Login
* Logout
* Edit profile
* Search trains
* View train schedule
* Book tickets
* Cancel tickets
* View booking history

Passwords must be encrypted in backend.

Frontend may temporarily use LocalStorage.

---

# Modules

## Login Module

* Login
* Register
* Forgot Password UI
* Remember Me UI
* Logout

---

## Dashboard

Passenger Dashboard

Cards:

* Upcoming Trips
* Booking History
* Cancelled Tickets
* Favourite Routes

Admin Dashboard

Cards:

* Total Users
* Total Trains
* Total Bookings
* Today's Trips
* Cancelled Tickets
* Revenue Placeholder
* Active Routes

Charts are optional.

---

## User Management

Admin

* Add User
* Edit User
* Delete User
* Search User
* Filter User

Passenger

* Update Profile
* Change Password
* View Profile

---

## Train Management

CRUD

Fields:

* Train Number
* Train Name
* Train Type
* Source
* Destination
* Total Seats
* Available Seats
* Running Days
* Status

---

## Station Management

CRUD

Fields:

* Station Code
* Station Name
* City
* State
* Platform Count

---

## Route Management

CRUD

Fields:

* Route ID
* Train
* Source
* Destination
* Distance
* Intermediate Stations

---

## Schedule Management

CRUD

Fields:

* Train
* Departure
* Arrival
* Duration
* Platform
* Running Days

---

## Train Search

Search by

* Source
* Destination
* Date
* Train Number
* Train Name

Display

* Seat Availability
* Arrival
* Departure
* Duration

---

## Ticket Booking

Passenger selects

* Train
* Date
* Class
* Passenger Count

Generate booking.

Generate

* PNR
* Booking ID

Update available seats.

---

## Booking History

Display

* Upcoming
* Completed
* Cancelled

Allow

* Cancel Booking

---

## Reports

Admin

Reports

* Bookings
* Trains
* Users
* Seat Occupancy
* Cancellation Summary

Display using tables.

---

## Notifications

Simple notification system.

Examples

Booking Successful

Ticket Cancelled

Profile Updated

---

# Validation

Validate every form.

Examples

Required Fields

Email

Password

Phone Number

Seat Count

Train Number

Dates

No invalid data.

---

# Backend Architecture

Use proper enterprise architecture.

Controller

↓

Service

↓

Repository

↓

Database

Use

* DTO
* Entity
* Mapper
* Exception
* Validation
* Config
* Security
* Utility

Package structure should follow Spring Boot best practices.

---

# Database Tables

Users

Roles

Stations

Routes

Trains

Schedules

Bookings

BookingPassengers

Notifications

AuditLogs

---

# Sample Data

Populate the project with realistic demo data.

Create at least **5 records** for every major table:

Users

Stations

Routes

Trains

Schedules

Bookings

Notifications

Use realistic Indian railway names.

Example

Train

22952 Mumbai Rajdhani

12841 Coromandel Express

20817 Bhubaneswar Rajdhani

12301 Howrah Rajdhani

12626 Kerala Express

Stations

Bhubaneswar

Howrah

New Delhi

Chennai Central

Mumbai CSMT

Use SQL insert scripts.

Frontend should contain the same 5 sample records inside LocalStorage initialization.

---

# Temporary Storage (Frontend)

Since the frontend is independent,

Store everything using

* LocalStorage

or

* Mock JavaScript Arrays

Every CRUD operation should work without backend integration.

Data should persist using LocalStorage.

---

# Backend

Backend should connect to MySQL.

Provide

application.properties

SQL Schema

Insert Script

REST APIs

Swagger (optional)

No frontend integration.

---

# Folder Structure

## Backend

```
RailBookPro-Backend/
```

Complete Maven project.

---

## Frontend

```
RailBookPro-Frontend/
```

Contains

```
index.html

login.html

register.html

dashboard.html

search.html

booking.html

history.html

profile.html

admin/

css/

js/

assets/

images/
```

---

# Code Quality

Follow industry standards.

* Clean Code
* SOLID Principles
* Reusable Components
* Modular JavaScript
* Proper Naming
* Comments only where necessary
* Consistent formatting
* No duplicated code

---

# Deliverables

Generate:

1. Complete Spring Boot backend project (Java 22).
2. Complete HTML/CSS/JavaScript frontend project.
3. MySQL schema (`schema.sql`).
4. Sample data (`data.sql`) with at least 5 records per major table.
5. REST API documentation.
6. README files for both projects explaining setup and execution.
7. Both applications must run independently without requiring integration. The frontend should use LocalStorage with seeded sample data, while the backend should operate against the MySQL database with its own sample data. No payment integration is required.