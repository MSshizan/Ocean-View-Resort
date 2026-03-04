# 🌊 Ocean View Resort System

![GitHub repo size](https://img.shields.io/github/repo-size/MSshizan/Ocean-View-Resort?style=flat-square)
![GitHub last commit](https://img.shields.io/github/last-commit/MSshizan/Ocean-View-Resort?style=flat-square)
![GitHub issues](https://img.shields.io/github/issues/MSshizan/Ocean-View-Resort?style=flat-square)
![GitHub license](https://img.shields.io/github/license/MSshizan/Ocean-View-Resort?style=flat-square)

---
## Overview
**Ocean View Resort System** is a complete hotel management application built with:

- **Backend:** Java Spring Boot  
- **Frontend:** Java Swing  
- **Database:** MySQL  

It manages rooms, reservations, customers, and provides reports for resort operations.

---

## Features
- Room management  
- Reservation management  
- Customer management  
- Reporting & analytics  
- Database security & connection management  

---

## Folder Structure
```text
OceanViewResortSystem/
├─ back-end/                  # Spring Boot backend
│   ├─ src/
│   ├─ .gitignore
│   └─ pom.xml
├─ front-end/                 # Java Swing frontend
│   ├─ src/
│   └─ nbproject/
├─ database/                  # MySQL dump file
│   └─ hotel.sql
├─ .gitignore
└─ README.md
```
## Prerequisites

Before running the project, ensure you have:

- Java JDK 17+  
- Maven 3.8+  
- MySQL 8+  
- NetBeans / IntelliJ IDEA  
- Git  

---

## Environment Variables

Set up environment variables to connect the backend to the database:

| Variable      | Description                     |
|---------------|---------------------------------|
| DB_HOST       | Database host (e.g., localhost) |
| DB_PORT       | Database port (e.g., 3306)      |
| DB_NAME       | Database name (hotel)           |
| DB_USER       | Database username               |
| DB_PASSWORD   | Database password               |

You can create a `.env` file or configure your IDE with these variables.

---

## Database Setup

1. Open **MySQL Workbench**  
2. Go to **File → Open SQL Script** and select `database/hotel.sql`  
3. Ensure these options are selected while importing:  
   - **Include CREATE SCHEMA**  
   - **Create dump in single transaction**  
   - **Include triggers and events** (if used)  
4. Click **Execute** to import  
5. Verify tables are created in the `hotel` schema  

---

## Backend Setup (Spring Boot)

1. Open `back-end/OVR` in **IntelliJ IDEA**  
2. Make sure environment variables are set  
3. Build the project:

```bash
mvn clean install
```

## Run the Backend

To start the Spring Boot backend, run:

```bash
mvn spring-boot:run
```
The backend will be accessible at: [http://localhost:8080](http://localhost:8080)

## Frontend Setup (Java Swing)

1. Open `front-end/OceanViewResort` in **NetBeans**
2. Ensure the backend is running
3. Run the Swing application
4. Test all functionalities:
   - Rooms
   - Reservations
   - Customers
   - Reports

---

## How to Run the Full System

1. Start MySQL database
2. Start Backend API
3. Launch Frontend application
4. Verify that rooms, reservations, and customers are visible and working

---

## Version Control (Git)

This project uses Git for version control. Recommended workflow:

```bash
# Clone the repository
git clone https://github.com/MSshizan/Ocean-View-Resort.git
cd Ocean-View-Resort

# Pull latest changes
git pull origin main

# Add new changes
git add .
git commit -m "Your descriptive commit message"
git push origin main
```

## Notes

- Do not commit sensitive data like database passwords  
- `.gitignore` excludes IDE-specific files, `target/` or `build/` folders, and OS files like `.DS_Store`  

---

## Versioning

This project follows semantic versioning:

| Version  | Description                     |
|----------|---------------------------------|
| v1.0.0   | Initial release of the system    |
| v1.1.0   |Added server-down dialog & improved login handling    |
| v1.2.0   | Addes Twilio SMS service with secrets removed    |
| v1.4.0   | Release version V1.4.0: added new features and bug fixes    |
| v1.5.0   | Release version 1.5.0: added new features and bug fixes   |


---

## License

This project is licensed under the **MIT License**.

---

## Author

**MS Shizan**  
[GitHub Profile](https://github.com/MSshizan)



