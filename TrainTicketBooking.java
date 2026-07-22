import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

class Ticket {
    String bookingId;
    String passengerName;
    int age;
    String gender;
    String trainNumber;
    String trainName;
    String sourceStation;
    String destinationStation;
    String journeyDate;
    String coachType;
    String seatNumber;

    Ticket(String bookingId, String passengerName, int age, String gender,
           String trainNumber, String trainName, String sourceStation,
           String destinationStation, String journeyDate, String coachType,
           String seatNumber) {
        this.bookingId = bookingId;
        this.passengerName = passengerName;
        this.age = age;
        this.gender = gender;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.journeyDate = journeyDate;
        this.coachType = coachType;
        this.seatNumber = seatNumber;
    }
}

public class TrainTicketBooking {

    static ArrayList<Ticket> bookings = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static int bookingCounter = 0;
    static int seatCounter = 0;
    static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    bookTicket();
                    break;
                case "2":
                    viewAllTickets();
                    break;
                case "3":
                    editBooking();
                    break;
                case "4":
                    deleteBooking();
                    break;
                case "5":
                    System.out.println("\nThank you for using Train Ticket Booking System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    static void printMenu() {
        System.out.println("\n===== Train Ticket Booking System =====\n");
        System.out.println("1. Book Ticket");
        System.out.println("2. View All Tickets");
        System.out.println("3. Edit Booking");
        System.out.println("4. Delete Booking");
        System.out.println("5. Exit");
        System.out.print("\nEnter Choice: ");
    }

    static void bookTicket() {
        System.out.println("\n----- Book Ticket -----");

        String passengerName = readName("Passenger Name: ");
        int age = readAge("Age: ");

        System.out.print("Gender: ");
        String gender = scanner.nextLine().trim();

        System.out.print("Train Number: ");
        String trainNumber = scanner.nextLine().trim();

        System.out.print("Train Name: ");
        String trainName = scanner.nextLine().trim();

        System.out.print("Source Station: ");
        String sourceStation = scanner.nextLine().trim();

        System.out.print("Destination Station: ");
        String destinationStation = scanner.nextLine().trim();

        String journeyDate = readFutureDate("Journey Date (YYYY-MM-DD): ");

        System.out.print("Coach Type: ");
        String coachType = scanner.nextLine().trim();

        // Booking ID and Seat Number are auto-generated after booking.
        String bookingId = generateBookingId();
        String seatNumber = generateSeatNumber();

        Ticket ticket = new Ticket(bookingId, passengerName, age, gender,
                trainNumber, trainName, sourceStation, destinationStation,
                journeyDate, coachType, seatNumber);
        bookings.add(ticket);

        System.out.println("\nTicket booked successfully.");
        System.out.println("Booking ID : " + bookingId);
        System.out.println("Seat Number: " + seatNumber);
    }

    static void viewAllTickets() {
        System.out.println("\n----- All Tickets -----");

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        String format = "%-10s %-18s %-4s %-8s %-8s %-20s %-16s %-18s %-12s %-10s %-8s%n";
        System.out.printf(format, "BookingID", "Passenger", "Age", "Gender",
                "TrainNo", "TrainName", "Source", "Destination",
                "Date", "Coach", "Seat");
        System.out.println("-".repeat(150));

        for (Ticket t : bookings) {
            System.out.printf(format, t.bookingId, t.passengerName, t.age, t.gender,
                    t.trainNumber, t.trainName, t.sourceStation, t.destinationStation,
                    t.journeyDate, t.coachType, t.seatNumber);
        }
    }

    static void editBooking() {
        System.out.println("\n----- Edit Booking -----");
        System.out.print("Enter Booking ID to edit: ");
        String bookingId = scanner.nextLine().trim();

        Ticket ticket = findTicket(bookingId);
        if (ticket == null) {
            System.out.println("Booking not found.");
            return;
        }

        System.out.println("Leave a field blank to keep the current value.");
        System.out.println("(Booking ID and Seat Number cannot be changed.)");

        System.out.print("Passenger Name (" + ticket.passengerName + "): ");
        String passengerName = scanner.nextLine().trim();
        if (!passengerName.isEmpty()) {
            if (isValidName(passengerName)) {
                ticket.passengerName = passengerName;
            } else {
                System.out.println("Invalid name. Keeping previous value.");
            }
        }

        System.out.print("Age (" + ticket.age + "): ");
        String ageInput = scanner.nextLine().trim();
        if (!ageInput.isEmpty()) {
            Integer parsedAge = parseAge(ageInput);
            if (parsedAge != null) {
                ticket.age = parsedAge;
            } else {
                System.out.println("Invalid age. Keeping previous value.");
            }
        }

        System.out.print("Gender (" + ticket.gender + "): ");
        String gender = scanner.nextLine().trim();
        if (!gender.isEmpty()) ticket.gender = gender;

        System.out.print("Train Number (" + ticket.trainNumber + "): ");
        String trainNumber = scanner.nextLine().trim();
        if (!trainNumber.isEmpty()) ticket.trainNumber = trainNumber;

        System.out.print("Train Name (" + ticket.trainName + "): ");
        String trainName = scanner.nextLine().trim();
        if (!trainName.isEmpty()) ticket.trainName = trainName;

        System.out.print("Source Station (" + ticket.sourceStation + "): ");
        String sourceStation = scanner.nextLine().trim();
        if (!sourceStation.isEmpty()) ticket.sourceStation = sourceStation;

        System.out.print("Destination Station (" + ticket.destinationStation + "): ");
        String destinationStation = scanner.nextLine().trim();
        if (!destinationStation.isEmpty()) ticket.destinationStation = destinationStation;

        System.out.print("Journey Date (" + ticket.journeyDate + "): ");
        String journeyDate = scanner.nextLine().trim();
        if (!journeyDate.isEmpty()) {
            if (isValidFutureDate(journeyDate)) {
                ticket.journeyDate = journeyDate;
            } else {
                System.out.println("Invalid date or date is in the past. Keeping previous value.");
            }
        }

        System.out.print("Coach Type (" + ticket.coachType + "): ");
        String coachType = scanner.nextLine().trim();
        if (!coachType.isEmpty()) ticket.coachType = coachType;

        System.out.println("\nBooking updated successfully.");
    }

    static void deleteBooking() {
        System.out.println("\n----- Delete Booking -----");
        System.out.print("Enter Booking ID to delete: ");
        String bookingId = scanner.nextLine().trim();

        Ticket ticket = findTicket(bookingId);
        if (ticket == null) {
            System.out.println("Booking not found.");
            return;
        }

        bookings.remove(ticket);
        System.out.println("\nBooking deleted successfully.");
    }

    static Ticket findTicket(String bookingId) {
        for (Ticket t : bookings) {
            if (t.bookingId.equalsIgnoreCase(bookingId)) {
                return t;
            }
        }
        return null;
    }

    static String generateBookingId() {
        bookingCounter++;
        return String.format("BK%03d", bookingCounter);
    }

    static String generateSeatNumber() {
        seatCounter++;
        int coach = (seatCounter - 1) / 72 + 1;
        int seat = (seatCounter - 1) % 72 + 1;
        return String.format("S%d-%02d", coach, seat);
    }

    // ----- Validation helpers -----

    static boolean isValidName(String name) {
        return name.matches("[A-Za-z ]+");
    }

    static String readName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (isValidName(input)) {
                return input;
            }
            System.out.println("Name can only contain alphabets and spaces.");
        }
    }

    static Integer parseAge(String input) {
        try {
            int age = Integer.parseInt(input);
            if (age >= 1 && age <= 150) {
                return age;
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static int readAge(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            Integer age = parseAge(input);
            if (age != null) {
                return age;
            }
            System.out.println("Age must be a number between 1 and 150.");
        }
    }

    static boolean isValidFutureDate(String input) {
        try {
            LocalDate date = LocalDate.parse(input, DATE_FORMAT);
            return !date.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    static String readFutureDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (isValidFutureDate(input)) {
                return input;
            }
            System.out.println("Journey date must be a valid date (YYYY-MM-DD) and not in the past.");
        }
    }
}
