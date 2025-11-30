import java.util.Scanner;

// Abstract Car class
abstract class Car {
    private String brand;
    private String model;
    private int year;
    private boolean isAvailable;
    private String rentedBy;

    public Car(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.isAvailable = true;
        this.rentedBy = "";
    }

    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
    public String getRentedBy() { return rentedBy; }
    public void setRentedBy(String rentedBy) { this.rentedBy = rentedBy; }

    public abstract double calculatePrice(int days);

    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Rented by " + rentedBy;
        return brand + " " + model + " (" + year + ") - " + status;
    }
}

// Sedan class
class Sedan extends Car {
    public Sedan(String brand, String model, int year) {
        super(brand, model, year);
    }

    @Override
    public double calculatePrice(int days) {
        return days * 40;
    }
}

// SUV class
class SUV extends Car {
    public SUV(String brand, String model, int year) {
        super(brand, model, year);
    }

    @Override
    public double calculatePrice(int days) {
        return days * 60;
    }
}

// Electric class
class Electric extends Car {
    public Electric(String brand, String model, int year) {
        super(brand, model, year);
    }

    @Override
    public double calculatePrice(int days) {
        return days * 30 + 20; // fixed battery fee
    }
}

// Rental record class
class RentalRecord {
    private String clientName;
    private String carName;
    private int days;
    private double totalPrice;

    public RentalRecord(String clientName, String carName, int days, double totalPrice) {
        this.clientName = clientName;
        this.carName = carName;
        this.days = days;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return clientName + " rented " + carName + " for " + days + " days - Total: $" + totalPrice;
    }
}

// Car Rental System class
class CarRentalSystem {
    private Car[] cars;
    private RentalRecord[] history;
    private int historyCount;

    public CarRentalSystem() {
        cars = new Car[15];
        // Sedans
        cars[0] = new Sedan("Toyota", "Camry", 2020);
        cars[1] = new Sedan("Honda", "Civic", 2019);
        cars[2] = new Sedan("BMW", "3 Series", 2021);
        cars[3] = new Sedan("Mercedes", "C-Class", 2020);
        cars[4] = new Sedan("Audi", "A4", 2022);

        // SUVs
        cars[5] = new SUV("BMW", "X5", 2021);
        cars[6] = new SUV("Toyota", "Land Cruiser", 2018);
        cars[7] = new SUV("Mercedes", "GLE", 2020);
        cars[8] = new SUV("Audi", "Q7", 2021);
        cars[9] = new SUV("Jeep", "Wrangler", 2019);

        // Electric
        cars[10] = new Electric("Tesla", "Model 3", 2022);
        cars[11] = new Electric("Tesla", "Model Y", 2021);
        cars[12] = new Electric("Nissan", "Leaf", 2021);
        cars[13] = new Electric("Chevrolet", "Bolt", 2020);
        cars[14] = new Electric("Ford", "Mustang Mach-E", 2022);

        history = new RentalRecord[100]; // max 100 records
        historyCount = 0;
    }

    public void showAllCars() {
        System.out.println("\n--- All Cars ---");
        for (int i = 0; i < cars.length; i++) {
            System.out.println(i + ": " + cars[i]);
        }
    }

    public void showAvailableCars() {
        System.out.println("\n--- Available Cars ---");
        for (int i = 0; i < cars.length; i++) {
            if (cars[i].isAvailable()) {
                System.out.println(i + ": " + cars[i]);
            }
        }
    }

    public void rentCar(int index, String clientName, int days) {
        if (index < 0 || index >= cars.length) {
            System.out.println("Invalid car selection.");
            return;
        }

        Car car = cars[index];
        if (!car.isAvailable()) {
            System.out.println("Sorry, this car is already rented by " + car.getRentedBy());
            return;
        }

        double basePrice = car.calculatePrice(days);
        double discount = 0;

        if (days >= 5) discount += 0.10; // 10% discount for long rental
        if (car instanceof Electric) discount += 0.15; // 15% discount for electric cars

        double totalPrice = basePrice * (1 - discount);

        car.setAvailable(false);
        car.setRentedBy(clientName);

        String carName = car.getBrand() + " " + car.getModel();
        history[historyCount++] = new RentalRecord(clientName, carName, days, totalPrice);

        printReceipt(clientName, carName, days, basePrice, discount, totalPrice);
    }

    public void returnCar(int index, String clientName) {
        if (index < 0 || index >= cars.length) {
            System.out.println("Invalid car selection.");
            return;
        }

        Car car = cars[index];
        if (!car.isAvailable() && car.getRentedBy().equals(clientName)) {
            car.setAvailable(true);
            car.setRentedBy("");
            System.out.println("Thank you, " + clientName + ". You have returned the car.");
        } else if (car.isAvailable()) {
            System.out.println("This car is not rented currently.");
        } else {
            System.out.println("You did not rent this car!");
        }
    }

    public void showHistory() {
        System.out.println("\n--- Rental History ---");
        if (historyCount == 0) {
            System.out.println("No rentals yet.");
            return;
        }
        for (int i = 0; i < historyCount; i++) {
            System.out.println(history[i]);
        }
    }

    private void printReceipt(String clientName, String carName, int days, double basePrice, double discount, double totalPrice) {
        System.out.println("\n======== RECEIPT ========");
        System.out.println("Client: " + clientName);
        System.out.println("Car: " + carName);
        System.out.println("Days: " + days);
        System.out.println("Base Price: $" + basePrice);
        System.out.println("Discount: " + (int)(discount*100) + "%");
        System.out.println("Total Price: $" + totalPrice);
        System.out.println("=========================\n");
    }
}

// Main class
public class index {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CarRentalSystem system = new CarRentalSystem();

        System.out.println("Welcome to Car Rental System!");
        System.out.print("Please enter your name: ");
        String clientName = scanner.nextLine();

        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- Menu ---");
            System.out.println("1 - Show all cars");
            System.out.println("2 - Show available cars");
            System.out.println("3 - Rent a car");
            System.out.println("4 - Return a car");
            System.out.println("5 - Show rental history");
            System.out.println("0 - Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Очистка буфера после nextInt
            } else {
                System.out.println("Invalid input. Try again.");
                scanner.nextLine(); // Очистка некорректного ввода
                continue;
            }

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
                    scanner.nextLine(); // Очистка буфера
                    system.rentCar(rentIndex, clientName, days);
                    break;
                case 4:
                    system.showAllCars();
                    System.out.print("Enter the index of the car you want to return: ");
                    int returnIndex = scanner.nextInt();
                    scanner.nextLine(); // Очистка буфера
                    system.returnCar(returnIndex, clientName);
                    break;
                case 5:
                    system.showHistory();
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