# Find My Blood

> A full-stack blood donor discovery and blood-request platform built with Spring Boot, Java, MySQL, JPA/Hibernate, HTML, CSS and JavaScript.

## Project Overview

**Find My Blood** is a web application designed to connect blood donors with people who need blood. Users can register as donors, search for available donors using blood group and city, create blood requests, manage their account, and maintain donation-related information.

The project uses a **Spring Boot backend** with **MySQL** as the database and a browser-based frontend served directly by Spring Boot.

The current frontend design includes:

- Find My Blood branding
- Responsive layout for desktop, laptop, tablet and mobile devices
- Liquid Glass-inspired header, footer, buttons and UI components
- Rounded UI elements
- Light and dark theme support
- Automatic theme detection based on the device preference
- Manual light/dark theme switching
- Persistent theme preference in the browser
- Dedicated `assets` folder for the original logo and other frontend assets
- Empty logo slots in the header and footer for the project's original logo
- Mobile navigation support
- Accessible form and navigation structure
- The previous “3 lives saved per donation” visual card has been removed

**Backend functionality has not been changed as part of the frontend redesign.**

---

## Team Members

| Name | Role |
|---|---|
| **Saud Ahmad** | Backend Developer |
| **Mohammad Danish** | Frontend Developer |
| **Furkan Ahmad** | Research & Database |
| **Mohammad Faizan** | Database Setup |

---

## Technology Stack

### Backend

- Java 17 target
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA
- Hibernate ORM
- Maven
- Spring Security Crypto / BCrypt for password hashing

### Database

- **MySQL 8**
- MySQL Connector/J
- Database name: `Finyourblood_db`
- Local MySQL server: `localhost:3306`
- Database schema is managed/updated by Hibernate using `spring.jpa.hibernate.ddl-auto=update`

### Frontend

- HTML5
- CSS3
- Vanilla JavaScript
- Responsive CSS
- CSS-based Liquid Glass visual styling
- Browser `localStorage` for theme preference
- `prefers-color-scheme` for device theme detection

---

## Main Features

### 1. Donor Registration

Users can register as blood donors by providing information such as:

- Full name
- Age
- Gender
- Blood group
- Mobile number
- City
- Address
- Availability

Donor information is stored in the MySQL database.

### 2. Find My Blood

The application provides a donor-search page where users can search for donors using blood group and city.

### 3. User Registration and Login

The application provides account registration and login functionality through the authentication API.

User records include:

- Name
- Email
- Phone
- Password hash
- Terms acceptance
- Terms acceptance timestamp

Passwords are processed using password encryption rather than being stored as plain text.

### 4. Personal Account

The account section provides a dedicated area for the logged-in user and supports account-related information and donation history functionality.

### 5. Donation Records

The application contains a donation model and API for storing and retrieving donation records, including:

- Donor email
- Donation date
- Blood group
- City

### 6. Blood Requests

Users can create blood requests containing information such as:

- Requester name
- Required blood group
- City
- Contact number
- Request status

The backend also provides matching functionality for a blood request.

### 7. Blood Compatibility

The backend contains a `BloodCompatibility` utility for checking donor/recipient blood-group compatibility.

### 8. Donor Availability

Donor availability can be changed through the backend API.

### 9. Responsive Design

The frontend adapts to different screen sizes:

- Desktop
- Laptop
- Tablet
- Mobile phone
- Small mobile screens

### 10. Light/Dark Theme

The frontend supports:

- Automatic device theme detection
- Manual light/dark switching
- Saved theme preference using browser storage

### 11. Liquid Glass UI

The frontend uses a Liquid Glass-inspired visual system for:

- Header
- Footer
- Buttons
- Cards
- Forms and panels

The design also uses rounded corners and responsive spacing.

---

## Project Structure

```text
Find-My-Blood/
│
├── pom.xml
├── README.md
├── .gitignore
│
├── .vscode/
│   └── launch.json
│
└── src/
    └── main/
        ├── java/
        │   └── com/example/blooddonorfinder/
        │       ├── BloodDonorFinderApplication.java
        │       │
        │       ├── config/
        │       │   ├── CorsConfig.java
        │       │   ├── DataSeeder.java
        │       │   ├── GlobalExceptionHandler.java
        │       │   └── PasswordConfig.java
        │       │
        │       ├── controller/
        │       │   ├── AuthController.java
        │       │   ├── BloodRequestController.java
        │       │   ├── DonationController.java
        │       │   └── DonorController.java
        │       │
        │       ├── entity/
        │       │   ├── User.java
        │       │   ├── Donor.java
        │       │   ├── Donation.java
        │       │   └── BloodRequest.java
        │       │
        │       ├── repository/
        │       │   ├── UserRepository.java
        │       │   ├── DonorRepository.java
        │       │   ├── DonationRepository.java
        │       │   └── BloodRequestRepository.java
        │       │
        │       ├── service/
        │       │   ├── AuthService.java
        │       │   ├── DonorService.java
        │       │   └── BloodRequestService.java
        │       │
        │       └── util/
        │           └── BloodCompatibility.java
        │
        └── resources/
            ├── application.properties
            │
            └── static/
                ├── index.html
                ├── find-blood.html
                ├── account.html
                ├── login.html
                ├── styles.css
                ├── script.js
                ├── find-blood.js
                ├── account.js
                ├── login.js
                ├── theme.js
                │
                └── assets/
                    ├── .gitkeep
                    └── README.txt
```

---

## Database

The project uses **MySQL**, not H2.

### Database

```text
Finyourblood_db
```

### Connection

The application is configured for:

```text
Host: localhost
Port: 3306
Username: root
Database: Finyourblood_db
```

The password is **not stored directly in the project**. The application reads it from the Windows environment variable:

```text
DB_PASSWORD
```

The relevant configuration is:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/Finyourblood_db?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

### Database Tables

Hibernate creates/updates the application's tables automatically. The current application contains these main tables:

```text
users

donor

donation

blood_request
```

To inspect the database in MySQL Workbench:

```sql
USE Finyourblood_db;
SHOW TABLES;
```

To inspect stored records:

```sql
SELECT * FROM users;
SELECT * FROM donor;
SELECT * FROM donation;
SELECT * FROM blood_request;
```

---

## Environment Variable Setup

The MySQL password should be stored as a Windows user environment variable instead of being committed to GitHub.

Create:

```text
DB_PASSWORD
```

and set its value to the local MySQL `root` password.

You can verify that Windows exposes the variable without printing the password:

### PowerShell

```powershell
if ($env:DB_PASSWORD) { "DB_PASSWORD is set" } else { "DB_PASSWORD is NOT set" }
```

After creating or changing the variable, restart VS Code so that the Spring Boot process can receive the updated environment.

**Never commit the actual MySQL password to GitHub.**

---

## Running the Project

### Prerequisites

Install:

1. Java 17 or a compatible Java version for the project
2. Maven
3. MySQL 8
4. MySQL Workbench (recommended for database management)
5. VS Code or another Java IDE

### Step 1 — Create the database

Open MySQL Workbench and run:

```sql
CREATE DATABASE Finyourblood_db;
```

If the database already exists, do not create it again.

### Step 2 — Configure the MySQL password

Set the Windows environment variable:

```text
DB_PASSWORD=your_mysql_password
```

Do not place the real password in `application.properties`.

### Step 3 — Open the project

Open the project folder in VS Code.

### Step 4 — Start Spring Boot

From the project root:

```bash
mvn spring-boot:run
```

Alternatively, run:

```text
BloodDonorFinderApplication.java
```

from your IDE.

### Step 5 — Confirm startup

The application is ready when the console shows messages similar to:

```text
HikariPool-1 - Added connection ...
Tomcat started on port 8080
Started BloodDonorFinderApplication
```

### Step 6 — Open the website

Open:

```text
http://localhost:8080/
```

Do **not** open `index.html` directly using `file://`. The frontend is designed to be served by Spring Boot.

---

## API Endpoints

### Authentication

Base path:

```text
/api/auth
```

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/signup` | Create a user account |
| POST | `/api/auth/login` | Log in a user |
| GET | `/api/auth/me` | Retrieve the current user information |

### Donors

Base path:

```text
/api/donors
```

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/donors` | Register a donor |
| GET | `/api/donors` | Get donors |
| GET | `/api/donors/{id}` | Get a donor by ID |
| GET | `/api/donors/search` | Search/filter donors |
| PATCH | `/api/donors/{id}/toggle-availability` | Toggle donor availability |
| DELETE | `/api/donors/{id}` | Delete a donor |

Search supports blood group and city filtering through the backend search endpoint.

### Donations

Base path:

```text
/api/donations
```

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/donations` | Get donation records |
| POST | `/api/donations` | Create a donation record |

### Blood Requests

Base path:

```text
/api/requests
```

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/requests` | Create a blood request |
| GET | `/api/requests` | Get blood requests |
| GET | `/api/requests/{id}` | Get a specific request |
| GET | `/api/requests/{id}/matches` | Find compatible donor matches |

---

## Frontend Pages

### Home

```text
http://localhost:8080/
```

Main landing page containing the project introduction, services, donor registration form and contact information.

### Find My Blood

```text
http://localhost:8080/find-blood.html
```

Used to search for available blood donors.

### Login / Sign Up

```text
http://localhost:8080/login.html
```

Used for account login and registration.

### My Account

```text
http://localhost:8080/account.html
```

Provides the account area and donation-related information.

---

## Frontend Assets

The frontend contains a dedicated folder for project assets:

```text
src/main/resources/static/assets/
```

This folder is intended for:

- Original project logo
- Images
- Icons
- Other static frontend assets

The header and footer currently contain dedicated empty logo slots so the original logo can be inserted without redesigning the layout.

---

## Security Notes

- MySQL credentials should be supplied through environment variables.
- Do not commit the actual `DB_PASSWORD` to GitHub.
- Passwords are handled using password encryption in the backend.
- The project is configured for local development and should be reviewed and hardened before production deployment.
- The database uses `ddl-auto=update`, which is convenient for development but should be evaluated carefully for production environments.

---

## GitHub Upload Checklist

Before pushing the project to GitHub:

- [ ] Remove any real passwords from source files.
- [ ] Confirm `application.properties` uses `${DB_PASSWORD}`.
- [ ] Confirm `.gitignore` excludes local secrets and build output.
- [ ] Do not commit `.env` files containing credentials.
- [ ] Do not commit MySQL root passwords.
- [ ] Confirm the application still starts using the environment variable.
- [ ] Confirm the project connects to `Finyourblood_db`.

---

## Troubleshooting

### MySQL says `Unknown database`

Check that the database exists:

```sql
SHOW DATABASES;
```

Then:

```sql
USE Finyourblood_db;
SHOW TABLES;
```

### MySQL says `Access denied for user 'root'`

Check that `DB_PASSWORD` contains the correct MySQL root password and restart VS Code after changing the Windows environment variable.

### Tables are not visible in MySQL Workbench

Run:

```sql
USE Finyourblood_db;
SHOW TABLES;
```

Then refresh the **Schemas** panel in MySQL Workbench and expand **Tables**.

### Website does not load

Confirm that Spring Boot is running and that the console contains:

```text
Tomcat started on port 8080
Started BloodDonorFinderApplication
```

Then open:

```text
http://localhost:8080/
```

### Frontend cannot reach the backend

Make sure the page is being served through Spring Boot rather than opened directly from the filesystem with a `file://` URL.

---

## Project Purpose

Find My Blood is intended to make blood-donor discovery more organized and accessible by bringing donor registration, blood-group/city search, blood requests, donor availability and donation information into one application.

The project combines a responsive frontend with a Spring Boot REST backend and MySQL database.

---

## Team

**Saud Ahmad** — Backend Developer  
**Mohammad Danish** — Frontend Developer  
**Furkan Ahmad** — Research & Database  
**Mohammad Faizan** — Database Setup

---

## License / Usage

This repository is a project developed by the listed team members. Add the project's intended license here if one is chosen for public distribution.
