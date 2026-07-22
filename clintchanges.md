# Prompt for Claude

You are an experienced Java developer and frontend developer.

Generate a **very simple Train Ticket Booking System** for educational purposes.

**IMPORTANT:** This is **NOT** a full-stack application.

Generate **three completely independent files** that have **no integration or dependency** on each other.

The only thing common between them should be the names of the data fields (columns).

---

# File 1 – Java Backend

Create **only one Java file**.

Requirements:

* Java 22
* No Spring Boot
* No Maven
* No Gradle
* No external libraries
* Everything inside a single `.java` file
* Runs directly from the terminal using:

  ```bash
  javac TrainTicketBooking.java
  java TrainTicketBooking
  ```
* Use only Java Collections (preferably `ArrayList`) for temporary storage.
* Do **not** connect to any database.
* Do **not** read or write files.
* Do **not** use networking.
* Data exists only while the program is running.

## Console Menu

Use a switch-case menu that repeatedly displays until the user chooses Exit.

```
===== Train Ticket Booking System =====

1. Book Ticket
2. View All Tickets
3. Edit Booking
4. Delete Booking
5. Exit

Enter Choice:
```

## Operations

### 1. Book Ticket

Ask the user to enter:

* Booking ID
* Passenger Name
* Age
* Gender
* Train Number
* Train Name
* Source Station
* Destination Station
* Journey Date
* Coach Type
* Seat Number

Store the booking in an ArrayList.

Display:

```
Ticket booked successfully.
```

---

### 2. View All Tickets

Display all stored bookings in a neat tabular format.

If no records exist:

```
No bookings found.
```

---

### 3. Edit Booking

Search using Booking ID.

Allow editing all fields except Booking ID.

Display success message.

If Booking ID is not found:

```
Booking not found.
```

---

### 4. Delete Booking

Delete using Booking ID.

Display success message.

If not found:

```
Booking not found.
```

---

### 5. Exit

Terminate the program gracefully.

---

## Coding Requirements

* Use classes appropriately.
* Create a Ticket class.
* Store Ticket objects inside an ArrayList.
* Use methods for each operation.
* Validate menu choices.
* Keep the code clean and readable.
* No unnecessary complexity.

---

# File 2 – SQL File

Generate **only one SQL file**.

This SQL file must be completely independent.

It must not reference Java or HTML.

## Database

Create database:

```sql
TrainTicketBookingDB
```

Create one table:

```
TicketBookings
```

Columns:

* booking_id
* passenger_name
* age
* gender
* train_number
* train_name
* source_station
* destination_station
* journey_date
* coach_type
* seat_number

After creating the table,

Insert **exactly 5 realistic sample booking records**.

Use Indian railway examples.

Example trains:

* 12841 Coromandel Express
* 20817 Rajdhani Express
* 12626 Kerala Express
* 12301 Howrah Rajdhani
* 22952 Mumbai Rajdhani

The SQL file should execute successfully on MySQL.

---

# File 3 – Frontend

Create **only one HTML file**.

Everything must be inside:

```
index.html
```

No external files.

Include CSS and JavaScript inside the same HTML file using `<style>` and `<script>` tags.

The page must work completely offline.

Do **not** connect to Java.

Do **not** connect to SQL.

Do **not** call APIs.

Use JavaScript objects or LocalStorage to temporarily store booking data.

---

## UI Requirements

When the page opens,

Immediately display the **Book Ticket** form.

No login page.

No home page.

No navigation menu.

---

## Booking Form Fields

* Booking ID
* Passenger Name
* Age
* Gender
* Train Number
* Train Name
* Source Station
* Destination Station
* Journey Date
* Coach Type
* Seat Number

Provide proper labels and input validation.

---

## After Booking

When the user clicks **Book Ticket**:

Display the booked ticket below the form in a clean ticket/card layout showing all entered details.

Below the displayed ticket, provide three buttons:

* Edit Booking
* Delete Booking
* Book New Ticket

---

## Edit Booking

Allow the user to modify the ticket details.

Update the displayed ticket after saving.

---

## Delete Booking

Delete the current booking.

Return the user to an empty booking form.

---

## Book New Ticket

Clear the form.

Allow creating another booking.

---

## Design Requirements

Create a simple, modern interface.

Use:

* Clean white background
* Blue primary color
* Rounded input fields
* Rounded buttons
* Card layout
* Responsive design
* Proper spacing
* Simple typography
* Professional appearance

No frameworks.

No Bootstrap.

Only:

* HTML
* CSS
* Vanilla JavaScript

---

# Important Constraints

1. Generate exactly three independent files:

   * `TrainTicketBooking.java`
   * `TrainTicketBooking.sql`
   * `index.html`

2. There must be **no integration** between the Java file, SQL file, and HTML file.

3. The Java application runs entirely in the terminal.

4. The SQL file only creates the database, table, and inserts five sample records.

5. The HTML page runs independently in the browser using JavaScript for temporary storage.

6. Keep the data fields (column names) consistent across all three files, but do not connect them in any way.

7. The code should be beginner-friendly, clean, well-structured, and easy to understand while following good programming practices.