# Hostel Complaint Management System

A Java Swing desktop application where students register, log in and submit hostel complaints, and admins view and manage them.

## Features
- Student registration and login
- Submit and view complaints
- Admin dashboard to manage complaints

## Tech Stack
- Java (Swing)
- MySQL
- JDBC (mysql-connector-j 9.6.0)

## Setup
1. Install JDK and MySQL.
2. Create the database and tables (add your SQL script as `database.sql`).
3. Update the DB username and password in the connection code.
4. Compile and run:

   javac -cp "lib/mysql-connector-j-9.6.0.jar" -d out src/**/*.java
   java -cp "out;lib/mysql-connector-j-9.6.0.jar" Main