# Hospital Management System

A console-based Java application for managing hospital and insurance records, built for my HND Software Development unit.

## Features
- Add, view and delete patients, doctors, drugs, prescriptions, visits and insurance claims (15 menu operations)
- Data saved to and loaded from a MariaDB database using JDBC
- Input validation and error handling for invalid input and missing IDs

## Design
- Object-oriented: abstract `Person` class extended by `Patient` and `Doctor`, with encapsulation and polymorphism
- Classes: Person, Patient, Doctor, Drug, Insurance, Prescription, Visit, Claim, DatabaseConnection, HospitalApp
- Prepared statements for all database writes
- ArrayList and HashMap for in-memory storage and fast lookup by ID

## Tech
Java, JDBC, MariaDB, SQL

## How to run
1. Install MariaDB and import the database: `mysql -u root -p < hospital.sql`
2. If your MariaDB login is different, update `src/DatabaseConnection.java`
3. On Windows, double-click `Run Hospital App.bat` (it compiles and runs the app)
