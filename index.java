import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
// Base class for all cars
class Car {
    // Encapsulation: private fields
    private String brand;
    private String model;
    private int year;
    private boolean isAvailable;
    private String rentedBy;

    // Constructor to initialize car object
    public Car(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.isAvailable = true;   // by default car is available
        this.rentedBy = "";        // no renter at the beginning
    }

    // Getters and setters (encapsulation)
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
    public String getRentedBy() { return rentedBy; }
    public void setRentedBy(String rentedBy) { this.rentedBy = rentedBy; }

    // Polymorphism: this method will be overridden in subclasses
    // Base implementation (not really used, but needed for inheritance)
    public double calculatePrice(int days) {
        return days * 50; // default price per day
    }

    // String representation of a car
    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Rented by " + rentedBy;
        return brand + " " + model + " (" + year + ") - " + status;
    }
}

// Sedan class (subclass of Car)
// Demonstrates inheritance + overriding
class Sedan extends Car {
    public Sedan(String brand, String model, int year) {
        super(brand, model, year); // call parent constructor
    }

    // Override method from Car (runtime polymorphism)
    @Override
    public double calculatePrice(int days) {
        return days * 40; // cheaper per day
    }
}

// SUV class (subclass of Car)
class SUV extends Car {
    public SUV(String brand, String model, int year) {
        super(brand, model, year);
    }
    @Override
    public double calculatePrice(int days) {
        return days * 60; // more expensive per day
    }
}

// Electric car class (subclass of Car)
class Electric extends Car {
    public Electric(String brand, String model, int year) {
        super(brand, model, year);
    }

    @Override
    public double calculatePrice(int days) {
        // cheaper per day + small fixed battery fee
        return days * 30 + 20;
    }
}

// RentalRecord class - stores one history line
class RentalRecord {
    private String line; // just store full text line
    public RentalRecord(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return line;
    }
}

// Main system logic class
class CarRentalSystem {
    // Array of cars (fixed size)
    private Car[] cars;
    // Array of history records
    private RentalRecord[] history;
    private int historyCount; // how many records we actually have

    // Constructor: create cars and load history from file
    public CarRentalSystem() {
        cars = new Car[15];

        // Create Sedans (inheritance in action)
        cars[0] = new Sedan("Toyota", "Camry", 2020);
        cars[1] = new Sedan("Honda", "Civic", 2019);
        cars[2] = new Sedan("BMW", "3 Series", 2021);
        cars[3] = new Sedan("Mercedes", "C-Class", 2020);
        cars[4] = new Sedan("Audi", "A4", 2022);

        // Create SUVs
        cars[5] = new SUV("BMW", "X5", 2021);
        cars[6] = new SUV("Toyota", "Land Cruiser", 2018);
        cars[7] = new SUV("Mercedes", "GLE", 2020);
        cars[8] = new SUV("Audi", "Q7", 2021);
        cars[9] = new SUV("Jeep", "Wrangler", 2019);

        // Create Electric cars
        cars[10] = new Electric("Tesla", "Model 3", 2022);
        cars[11] = new Electric("Tesla", "Model Y", 2021);
        cars[12] = new Electric("Nissan", "Leaf", 2021);
        cars[13] = new Electric("Chevrolet", "Bolt", 2020);
        cars[14] = new Electric("Ford", "Mustang Mach-E", 2022);

        history = new RentalRecord[200];
        historyCount = 0;

        // Load existing history from file and restore car states
        loadHistoryFromFile();
    }
    // Load history from history.txt and apply RENTED/RETURNED to cars
    private void loadHistoryFromFile() {
        try {
            File file = new File("history.txt");
            if (!file.exists()) return; // if no history yet, just skip
            Scanner fscan = new Scanner(file);
            while (fscan.hasNextLine()) {
                String line = fscan.nextLine();

                // Save line as record for showing later
                history[historyCount++] = new RentalRecord(line);

                // Restore rented/available state based on line text
                processHistoryLine(line);
            }
            fscan.close();
        } catch (Exception e) {
            System.out.println("Error loading history.");
        }
    }

    // Interpret line and update car state
    private void processHistoryLine(String line) {
        if (line.contains("RENTED")) {
            String[] parts = line.split(" ");
            String renter = parts[0];
            String brand = parts[2];
            String model = parts[3];

            Car car = findCar(brand, model);
            if (car != null) {
                car.setAvailable(false);
                car.setRentedBy(renter);
            }
        }
        if (line.contains("RETURNED")) {
            String[] parts = line.split(" ");

            String renter = parts[0];
            String brand = parts[2];
            String model = parts[3];

            Car car = findCar(brand, model);
            if (car != null && car.getRentedBy().equals(renter)) {
                car.setAvailable(true);
                car.setRentedBy("");
            }
        }
    }

    // Helper method to find a car by brand and model
    private Car findCar(String brand, String model) {
        for (Car c : cars) {
            if (c.getBrand().equalsIgnoreCase(brand) &&
                c.getModel().equalsIgnoreCase(model)) {
                return c;
            }
        }
        return null;
    }

    // Save one line to history.txt
    private void saveHistoryToFile(String text) {
        try {
            FileWriter writer = new FileWriter("history.txt", true);
            writer.write(text + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    // Show all cars (with status)
    public void showAllCars() {
        System.out.println("\n--- All Cars ---");
        for (int i = 0; i < cars.length; i++) { 
            System.out.println(i + ": " + cars[i]);
        }
    }

    // Show only available cars
    public void showAvailableCars() {
        System.out.println("\n--- Available Cars ---");
        for (int i = 0; i < cars.length; i++) {
            if (cars[i].isAvailable()) { // conditional if
                System.out.println(i + ": " + cars[i]);
            }
        }
    }

    // Search cars by brand or model
    public void searchCar(String keyword) {
        System.out.println("\n--- Search Results ---");
        boolean found = false;

        for (Car car : cars) {
            if (car.getBrand().equalsIgnoreCase(keyword) ||
                car.getModel().equalsIgnoreCase(keyword)) {
                System.out.println(car);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No car found with: " + keyword);
        }
    }

    // Sort cars by year using bubble sort (simple algorithm)
    public void sortCarsByYear() {
        for (int i = 0; i < cars.length - 1; i++) {
            for (int j = 0; j < cars.length - i - 1; j++) {
                if (cars[j].getYear() > cars[j + 1].getYear()) {
                    Car temp = cars[j];
                    cars[j] = cars[j + 1];
                    cars[j + 1] = temp;
                }
            }
        }
        System.out.println("\nCars sorted by year!");
    }

    // Rent a car by index
    public void rentCar(int index, String clientName, int days) {
        if (index < 0 || index >= cars.length) {
            System.out.println("Invalid car selection.");
            return;
        }
        if (days <= 0) {
            System.out.println("Days must be greater than 0.");
            return;
        }
        Car car = cars[index];

        if (!car.isAvailable()) {
            System.out.println("Sorry, this car is already rented by " + car.getRentedBy());
            return;
        }

        // Polymorphism: actual method depends on real car type (Sedan/SUV/Electric)
        double basePrice = car.calculatePrice(days);
        double discount = 0;

        // Extra discount rules 
        if (days >= 5) discount += 0.10;         // long rental discount
        if (car instanceof Electric) discount += 0.15; // eco discount

        double totalPrice = basePrice * (1 - discount);

        car.setAvailable(false);
        car.setRentedBy(clientName);

        String line = clientName + " RENTED " +
                car.getBrand() + " " + car.getModel() +
                " for " + days + " days | Total: $" + totalPrice;

        history[historyCount++] = new RentalRecord(line);
        saveHistoryToFile(line);

        printReceipt(clientName, car.getBrand() + " " + car.getModel(),
                days, basePrice, discount, totalPrice);
    }

    // Return a car by index
    public void returnCar(int index, String clientName) {
        if (index < 0 || index >= cars.length) {
            System.out.println("Invalid car selection.");
            return;
        }
        Car car = cars[index];
        if (car.isAvailable()) {
            System.out.println("This car is not rented currently.");
            return;
        }
        if (!car.getRentedBy().equals(clientName)) {
            System.out.println("You cannot return this car. It was rented by: " + car.getRentedBy());
            return;
        }
        car.setAvailable(true);
        car.setRentedBy("");
        String line = clientName + " RETURNED " +
                car.getBrand() + " " + car.getModel();

        history[historyCount++] = new RentalRecord(line);
        saveHistoryToFile(line);
        System.out.println("Thank you, " + clientName + ". You have returned the car.");
    }
    // Show full rental history
    public void showHistory() {
        System.out.println("\n--- Full Rental History ---");
        if (historyCount == 0) {
            System.out.println("No history yet.");
            return;
        }
        for (int i = 0; i < historyCount; i++) {
            System.out.println(history[i]);
        }
    }

    // Print receipt for the client
    private void printReceipt(String clientName, String carName, int days,
                              double basePrice, double discount, double totalPrice) {
        System.out.println("\n======== RECEIPT ========");
        System.out.println("Client: " + clientName);
        System.out.println("Car: " + carName);
        System.out.println("Days: " + days);
        System.out.println("Base Price: $" + basePrice);
        System.out.println("Discount: " + (int)(discount * 100) + "%");
        System.out.println("Total Price: $" + totalPrice);
        System.out.println("=========================\n");
    }
}

// Main class with console menu
public class index {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CarRentalSystem system = new CarRentalSystem(); // create system object

        System.out.println("Welcome to Car Rental System!");
        System.out.print("Please enter your name: ");
        String clientName = scanner.nextLine(); // store client name for this session
        int choice = -1;
        // Main loop (while) - console menu
        while (choice != 0) {
            System.out.println("\n--- Menu ---");
            System.out.println("1 - Show all cars");
            System.out.println("2 - Show available cars");
            System.out.println("3 - Rent a car");
            System.out.println("4 - Return a car");
            System.out.println("5 - Show rental history");
            System.out.println("6 - Search car");
            System.out.println("7 - Sort cars by year");
            System.out.println("0 - Exit");
            System.out.print("Enter your choice: ");

            // Check if input is integer
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); 
            } else {
                System.out.println("Invalid input. Try again.");
                scanner.nextLine(); 
                continue;
            }

            // switch statement: simple control flow
            switch (choice) {
                case 1:
                    system.showAllCars();
                    break;
                case 2:
                    system.showAvailableCars();
                    break;
                case 3:
                    system.showAvailableCars();
                    System.out.print("Enter the index of the car you want to rent: ");
                    int rentIndex = scanner.nextInt();
                    System.out.print("Enter number of days: ");
                    int days = scanner.nextInt();
                    scanner.nextLine();
                    system.rentCar(rentIndex, clientName, days);
                    break;
                case 4:
                    system.showAllCars();
                    System.out.print("Enter the index of the car you want to return: ");
                    int returnIndex = scanner.nextInt();
                    scanner.nextLine();
                    system.returnCar(returnIndex, clientName);
                    break;
                case 5:
                    system.showHistory();
                    break;
                case 6:
                    System.out.print("Enter brand or model: ");
                    String keyword = scanner.nextLine();
                    system.searchCar(keyword);
                    break;
                case 7:
                    system.sortCarsByYear();
                    break;
                case 0:
                    System.out.println("Thank you, " + clientName + "! Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }
}