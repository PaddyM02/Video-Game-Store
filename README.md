# Video Game Store

## Overview
**Video Game Store** is a Java desktop application that simulates the basic operations of a video game rental store. The application allows users to browse available games, manage customers, and handle game rentals through a graphical user interface built with Java Swing.  

This project was created as a learning exercise to practice object-oriented programming, file handling, and GUI development in Java.

## Features
- Graphical user interface using Java Swing
- View a list of available video games
- Load game data from a CSV file
- Load customer data from a text file
- Rent and return games
- Object-oriented design with clear separation of responsibilities

- Requirements -
Java JDK 8 or higher
Any Java-compatible IDE (IntelliJ IDEA, Eclipse, NetBeans) or command line

- How to Run -
Clone the repository:
git clone https://github.com/your-username/videogamestore.git
Open the project in your preferred Java IDE.
Make sure the resources folder is accessible and contains:
games.csv
customers.txt
Run the Main.java file.
Data Files
games.csv: Contains the list of video games available in the store
customers.txt: Contains customer information
These files are loaded at startup and can be modified to test different data.

- Technologies Used -
Java
Java Swing
File I/O
Object-Oriented Programming


## Project Structure
```text
VideoGameShop/
├── src/
│   └── videogamestore/
│       ├── Main.java
│       ├── Store.java
│       ├── Game.java
│       ├── Customer.java
│       ├── GameListPanel.java
│       ├── Rentable.java
│       └── ...
├── resources/
│   ├── games.csv
│   └── customers.txt
└── README.md


