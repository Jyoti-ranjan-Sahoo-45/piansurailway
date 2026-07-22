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

        System.out.print("Booking ID: ");
        String bookingId = scanner.nextLine().trim();

        System.out.print("Passenger Name: ");
        String passengerName = scanner.nextLine().trim();

        int age = readInt("Age: ");

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

        System.out.print("Journey Date (YYYY-MM-DD): ");
        String journeyDate = scanner.nextLine().trim();

        System.out.print("Coach Type: ");
        String coachType = scanner.nextLine().trim();

        System.out.print("Seat Number: ");
        String seatNumber = scanner.nextLine().trim();

        Ticket ticket = new Ticket(bookingId, passengerName, age, gender,
                trainNumber, trainName, sourceStation, destinationStation,
                journeyDate, coachType, seatNumber);
        bookings.add(ticket);

        System.out.println("\nTicket booked successfully.");
    }

    static void viewAllTickets() {
        System.out.println("\n----- All Tickets -----");

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        String format = "%-10s %-18s %-4s %-8s %-8s %-20s %-14s %-14s %-12s %-10s %-8s%n";
        System.out.printf(format, "BookingID", "Passenger", "Age", "Gender",
                "TrainNo", "TrainName", "Source", "Destination",
                "Date", "Coach", "Seat");
        System.out.println("-".repeat(140));

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

        System.out.print("Passenger Name (" + ticket.passengerName + "): ");
        String passengerName = scanner.nextLine().trim();
        if (!passengerName.isEmpty()) ticket.passengerName = passengerName;

        System.out.print("Age (" + ticket.age + "): ");
        String ageInput = scanner.nextLine().trim();
        if (!ageInput.isEmpty()) {
            try {
                ticket.age = Integer.parseInt(ageInput);
            } catch (NumberFormatException e) {
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
        if (!journeyDate.isEmpty()) ticket.journeyDate = journeyDate;

        System.out.print("Coach Type (" + ticket.coachType + "): ");
        String coachType = scanner.nextLine().trim();
        if (!coachType.isEmpty()) ticket.coachType = coachType;

        System.out.print("Seat Number (" + ticket.seatNumber + "): ");
        String seatNumber = scanner.nextLine().trim();
        if (!seatNumber.isEmpty()) ticket.seatNumber = seatNumber;

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

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
