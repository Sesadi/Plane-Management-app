import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
class Ticket {
    private int row;
    private int seat;
    private double price;
    private Person passenger;

    // Constructor
    public Ticket(int row, int seat, double price, Person passenger) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.passenger = passenger;
    }

    // Getters
    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }

    public double getPrice() {
        return price;
    }

    public Person getPassenger() {
        return passenger;
    }

    // Save ticket information to a file
    public void save() {
        try (FileWriter writer = new FileWriter("tickets.txt", true)) {
            writer.write(toString() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving ticket information: " + e.getMessage());
        }
    }

    // Print ticket information
    public void print_ticket_info() {
        System.out.println("Ticket Details:");
        System.out.println("Row: " + (char) ('A' + row));
        System.out.println("Seat: " + seat);
        System.out.println("Price: $" + price);
        System.out.println("Passenger Info:");
        passenger.printPersonInfo();
    }

    // Override toString for file saving
    @Override
    public String toString() {
        return "Row: " + (char) ('A' + row) +
                ", Seat: " + seat +
                ", Price: $" + price +
                ", Passenger: " + passenger.getName() + " " + passenger.getSurname() +
                " (" + passenger.getEmail() + ")";
    }
}