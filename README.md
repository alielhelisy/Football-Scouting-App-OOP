# Football Scouting App

A Java Swing desktop application for managing football player scouting reports. The app lets scouts and admins sign in, add players, review players by position, search existing reports, and store scouting data locally.

## Features

- Login and account creation with role support
- Default admin and scout accounts for testing
- Dashboard with user role and daily quota information
- Player creation with position, star rating, and scout comments
- Search players by football position
- View detailed player review history
- Update and delete player records
- Local file-based persistence for players, reviews, and users
- Rating strategy support using simple average or weighted recent ratings

## OOP Concepts Used

This project demonstrates several object-oriented programming concepts:

- Encapsulation through model classes such as `Player`, `Review`, and `Person`
- Inheritance through `Admin` and `Scout`, which extend `Person`
- Polymorphism through role-specific behavior such as `dailyQuota()`
- Abstraction through the `RatingStrategy` interface
- Exception handling with custom exceptions such as `AuthException` and `DataFormatRuntimeException`
- Collections and file handling for storing players, reviews, and users

## Main Classes

- `Project.java` - application entry point and login screen
- `DashboardFrame.java` - main dashboard after login
- `ScoutingService.java` - core business logic for players, reviews, searching, and saving data
- `Player.java` - player model
- `Review.java` - scouting review model
- `Person.java`, `Admin.java`, `Scout.java` - user role model classes
- `PlayerReportFrame.java` - player report and review UI
- `SearchFrame.java` - player search UI
- `RatingStrategy.java` - rating strategy interface
- `SimpleAverageStrategy.java` - average rating implementation
- `WeightedRecentStrategy.java` - recent-review weighted rating implementation

## Default Login Accounts

You can use these accounts to test the application:

| Username | Password | Role |
| --- | --- | --- |
| `admin` | `admin123` | ADMIN |
| `scout` | `scout123` | SCOUT |

New accounts can also be created from the login screen.

## How to Run

This project uses the package name `Project`, so compile and run it from the parent folder of the `Project` directory.

```bash
javac Project/*.java
java Project.Project
```

For example, if your files are in:

```text
AliProject/Project
```

open a terminal in `AliProject`, then run the commands above.

## Data Storage

The app saves scouting data in a local file named:

```text
scouting_data.dat
```

User-created accounts are saved in the user's home directory as:

```text
.scoutingapp_users.txt
```

These files are generated while the application runs and should usually not be committed to GitHub.

## Requirements

- Java JDK 8 or newer
- A desktop environment that supports Java Swing

## Project Type

University Object Oriented Programming project built with Java and Swing.
## Author

Ali Elhelisy
