# Java_project
Car Rental System – Console Application
Programming Fundamentals I – Course Project
1) Project Description – What the Program Does
This project is a console-based Car Rental System that simulates how a real rental company operates. The program is used by an employee (operator) who enters data for clients sitting in front of him. A client chooses a car, and the employee uses the system to rent or return the car.

The system supports:
Core Features:
-Show all cars with daily prices
-Show available cars
-Rent a car (with confirmation)
-Return a car (with user validation — cannot return someone else’s car)
-Search cars by brand/model
-Sort cars by year (Bubble Sort)
-Show rental history (loaded from file)
-Show a user’s active rentals (“My Rentals”)
-System statistics (cars, rentals, revenue)
-Permanent saving of rental history in history.txt
OOP Principles Used:
-Encapsulation (private fields, getters/setters)
-Inheritance (Sedan, SUV, Electric → Car)
-Polymorphism (overridden calculatePrice method)
 
Additional Concepts:
-Arrays
-Loops
-Conditionals
-Method overriding
-File I/O
-Date & Time logging

2) How to Run the Program
Requirements:
-Java 8+
-Any IDE (IntelliJ / VS Code / Eclipse) OR terminal
Steps:
1. Download the project folder
2.	Ensure file name is index.java
3.	Compile the program: javac index.java
4.	Run the program: java index
5.	Enter the client’s name
6.	Use the numeric menu to interact with the system

3) Features Explanation
1. Permanent rental history
Every RENT or RETURN action is saved to history.txt.
When the program restarts, it reads this file and restores the state of all cars.

2. My Rentals
Shows only the cars rented by the current client.

3. Confirmation before renting
The system shows chosen car + days and asks:
Confirm rental? (y/n)

4. History timestamps
Every operation is logged with:
Date: YYYY-MM-DDTHH:MM:SS

6. System Statistics
The system calculates:
-Total number of cars
-How many available	
-How many rented
-Total number of RENT operations
-Total revenue calculated from history

4) Assumptions and Limitations
-No GUI — console application only
-Users cannot add/remove cars
-History file uses simple text format

5) Example Use Case 
Client: Alikhan sits in front of the employee.
The employee opens the program, enters “Alikhan”, and rents a car on his behalf.
Later, Alikhan returns and returns the car.
The system checks that only Alikhan can return the car he rented.

6) Files Included
-index.java — main program
-history.txt — stored rental history
-README.md — documentation

7) Future Improvements
-Admin mode
-Graphical User Interface
-JSON database
