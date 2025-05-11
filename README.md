# Project for Ocado Technology Java Internship in Wrocław, by Cyprian Kozubek

# Payment Method Optimizer
## Purpose
This Java 21 application selects optimal payment methods 
for customer orders based on available promotions and loyalty points.
Its goal is to maximize total discount and minimize card payments by preferring
loyalty points when beneficial.
---
## Input

The application requires two input files in JSON format:

- `orders.json`: list of orders with possible payment promotions.
- `paymentmethods.json`: list of available payment methods, their discount percentage and available limit.

---
## Output

After execution, the program prints to standard output the available payment methods,
total amount of discounts for each order and amount to pay after discount
, example output:
* Available payment methods:
* Method: PUNKTY, Amount: 100.00
* Method: BosBankrut, Amount: 200.00
* Method: mZysk, Amount: 180.00
* No special discount for ORDER4
* Order ORDER1 - discount: 15.00
* Amount to pay for ORDER1 after discount: 85.00
* Order ORDER2 - discount: 10.00
* Amount to pay for ORDER2 after discount: 190.00
* Order ORDER3 - discount: 15.00
* Amount to pay for ORDER3 after discount: 135.00
---
## Basic information how to run application
How to generate fat-jar for this project:
* In power shell in correct project path write : `mvn clean package`
* In IntelliJ IDEA: on right toolbar, click symbol 
M -> Lifecycle-> package (double click or run button)

After compilation you should find created .jar file in:
`target/Cyprian_Kozubek_Java21_Wroclaw-1.0-SNAPSHOT-jar-with-dependencies.jar`

How to run program:
When generating of .jar file is ready, please run:
* `java -jar target/Cyprian_Kozubek_Java21_Wroclaw-1.0-SNAPSHOT-jar-with-dependencies.jar src/main/java/Promotion/orders.json src/main/java/Promotion/paymentmethods.json`
---
## Project structure:
* in java directory there are:
  * Main.java, main function of application
  * orders.json and paymentmethods.json, files with orders and paymentmethods in json format
  * Promotion package, package that contains three packages model, serive and util:
    * model package, contains main application objects:
    Order, PaymentDecision and PaymentMethod.
    * service package, contains application logic 
    * util package, contains utility classes
* In test directory there is Promotion.test package that contains 
unit tests made in JUNIT5
---