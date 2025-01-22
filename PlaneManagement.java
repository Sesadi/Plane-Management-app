import java.util.Scanner;


public class PlaneManagement{
    static Ticket[] tickets = new Ticket[100]; // Fixed size array for tickets
    static int ticketCount = 0;
    static int[][] seats = new int[4][];

    static {
        seats[0] = new int[14];
        seats[1] = new int[12];
        seats[2] = new int[12];
        seats[3] = new int[14];
    }

    public static void main(String[] args) {
        System.out.println("\nWelcome to the Plane Management application");


        // Main loop for user interaction
        boolean continueProgram = true;
        while (continueProgram) {
            // Display the menu
            System.out.println("\n************************************************");
            System.out.println("*                MENUE OPTIONS                 *");
            System.out.println("************************************************");
            System.out.println("    1. Buy a seat");
            System.out.println("    2. Cancel a seat");
            System.out.println("    3. Find first available seat");
            System.out.println("    4. Show seating plan");
            System.out.println("    5. Print tickets information and total sales");
            System.out.println("    6. Search ticket");
            System.out.println("    0. Quit");
            System.out.println("************************************************");

            // Get user input
            int choice;
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please select an option: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        buy_Seat();
                        break;
                    case 2:
                        cancel_Seat();
                        break;
                    case 3:
                        findFirstAvailableSeat();
                        break;
                    case 4:
                        displaySeatingPlan();
                        break;
                    case 5:
                        print_tickets_info();
                        break;
                    case 6:
                        search_Ticket();
                        break;
                    case 0:
                        continueProgram = false;
                        System.out.println("Exiting the program...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid input!");

            }

        }


    }

    static void displaySeatingPlan() {
        for (int i = 0; i < seats.length; i++) {
            char rowLetter = (char) ('A' + i);
            System.out.print("Row " + rowLetter + ": ");
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 0) {
                    System.out.print("O ");
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }

    static void buy_Seat() {
        Scanner scanner = new Scanner(System.in);

        // Get row letter and seat number from user
        System.out.print("Enter row letter (A, B, C, or D): ");
        String row = scanner.next().toUpperCase();
        System.out.print("Enter seat number (1-" + getNumberOfSeatsInRow(row) + "): ");
        int seatNumber = scanner.nextInt();

        // Validate user input
        if (!isValidRow(row) || !isValidSeatNumber(row, seatNumber) || !isSeatAvailable(row, seatNumber)) {
            System.out.println("Invalid seat selection. Please try again.");
            return;
        }

        // Convert row letter to index
        int rowIndex = row.charAt(0) - 'A';

        // Book the seat
        seats[rowIndex][seatNumber - 1] = 1; // Mark seat as booked (1)

        scanner.nextLine();

        String name = getInput(scanner, "Enter passenger's name: ");
        String surname = getInput(scanner, "Enter passenger's surname: ");
        String email = getInput(scanner, "Enter passenger's email: ");

        // Displaying inputs for demonstration
        System.out.println("Passenger's name: " + name);
        System.out.println("Passenger's surname: " + surname);
        System.out.println("Passenger's email: " + email);


        Person passenger = new Person(name, surname, email);


        double price = calculatePrice(row, seatNumber);
        Ticket ticket = new Ticket(rowIndex, seatNumber, price, passenger);


        ticket.save();

        tickets[ticketCount++] = ticket;


        System.out.println("Seat purchased successfully for " + passenger.getName() + " " + passenger.getSurname());
    }

    static String getInput(Scanner scanner, String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();

        if (input.isBlank()) {
            System.out.println("Please enter an input");
            return getInput(scanner, prompt);
        } else {
            return input;
        }
    }

    static void cancel_Seat() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter row letter (A, B, C, or D): ");
        String row = scanner.next().toUpperCase();
        System.out.print("Enter seat number (1-" + getNumberOfSeatsInRow(row) + "): ");
        int seatNumber = scanner.nextInt();

        if (!isValidRow(row) || !isValidSeatNumber(row, seatNumber) || isSeatAvailable(row, seatNumber)) {
            System.out.println("Invalid seat selection or seat already available. Please try again.");
            return;
        }

        int rowIndex = row.charAt(0) - 'A';

        seats[rowIndex][seatNumber - 1] = 0;

        // Cancel Ticket if exists
        for (int i = 0; i < ticketCount; i++) {
            if (tickets[i].getRow() == rowIndex && tickets[i].getSeat() == seatNumber) {
                // Shift remaining tickets
                for (int j = i; j < ticketCount - 1; j++) {
                    tickets[j] = tickets[j + 1];
                }
                tickets[--ticketCount] = null; // Decrement ticketCount and set the last element to null
                System.out.println("Seat " + row + seatNumber + " canceled successfully!");
                return;
            }
        }
        System.out.println("Ticket not found for seat " + row + seatNumber);
    }

    static void findFirstAvailableSeat() {
        for (int i = 0; i < seats.length; i++) {
            char rowLetter = (char) ('A' + i);
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 0) {
                    System.out.println("First available seat: Row " + rowLetter + ", Seat " + (j + 1));
                    return;
                }
            }
        }
        System.out.println("Sorry, all seats are booked.");
    }

    static boolean isValidRow(String row) {
        return row.matches("[A-D]");
    }

    static boolean isValidSeatNumber(String row, int seatNumber) {
        return seatNumber >= 1 && seatNumber <= getNumberOfSeatsInRow(row);
    }

    static boolean isSeatAvailable(String row, int seatNumber) {
        int rowIndex = row.charAt(0) - 'A';
        return seats[rowIndex][seatNumber - 1] == 0; // Seat is available if its value is 0
    }

    static int getNumberOfSeatsInRow(String row) {
        int rowIndex = row.charAt(0) - 'A';
        return seats[rowIndex].length;
    }

    static double calculatePrice(String row, int seatNumber) {
        if ((row.equals("A") || row.equals("B") || row.equals("C") || row.equals("D")) && seatNumber >= 1 && seatNumber <= 5) {
            return 200.0;
        } else if ((row.equals("A") || row.equals("B") || row.equals("C") || row.equals("D")) && seatNumber >= 6 && seatNumber <= 9) {
            return 150.0;
        } else {
            return 180.0;
        }
    }

    //task 5
    static void print_tickets_info() {
        double totalSales = 0;
        for (int i = 0; i < ticketCount; i++) {
            tickets[i].print_ticket_info();
            totalSales += tickets[i].getPrice();
        }
        System.out.println("Total Sales: $" + totalSales);
    }

    static void search_Ticket() {
        Scanner scanner = new Scanner(System.in);

        // Get row letter and seat number from user
        System.out.print("Enter row letter (A, B, C, or D): ");
        String row = scanner.next().toUpperCase();
        System.out.print("Enter seat number (1-" + getNumberOfSeatsInRow(row) + "): ");
        int seatNumber = scanner.nextInt();

        // Validate user input
        if (!isValidRow(row) || !isValidSeatNumber(row, seatNumber)) {
            System.out.println("Invalid seat selection. Please try again.");
            return;
        }

        int rowIndex = row.charAt(0) - 'A';

        // Check if the seat is booked
        if (seats[rowIndex][seatNumber - 1] == 1) {
            for (int i = 0; i < ticketCount; i++) {
                Ticket ticket = tickets[i];
                if (ticket.getRow() == rowIndex && ticket.getSeat() == seatNumber) {
                    ticket.print_ticket_info();
                    return;
                }
            }
        }

        // If the seat is not booked
        System.out.println("This seat is available.");
    }

}





