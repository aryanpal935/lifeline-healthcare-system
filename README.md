# Lifeline Healthcare System

A modern healthcare management system built using **Spring Boot**, **MySQL**, **Spring Security**, and **Thymeleaf**.  
This project helps manage patient health records, authentication, dashboards, and healthcare-related data efficiently.

---

# Features

- User Registration & Login
- Secure Authentication using Spring Security
- Dashboard for Health Records
- BMI Calculation & Category Detection
- Add / Manage Health Records
- Role-Based Structure
- Responsive UI Design
- MySQL Database Integration

---

# Tech Stack

## Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA

## Frontend
- HTML
- CSS
- Thymeleaf
- JavaScript

## Database
- MySQL

## Tools & Version Control
- Git
- GitHub
- VS Code

---

# Project Structure

```bash
src/main/java/com/lifeline/lifeline
│
├── controller
├── service
├── repository
├── entity
├── dto
├── config
```

---

# Modules

## Authentication Module
- User Login
- User Registration
- Secure Password Handling

## Dashboard Module
- Health Statistics
- BMI Information
- Latest Record Display

## Health Record Module
- Add Health Records
- Store Patient Information
- Display Medical Data

---

# Screenshots

## Login Page
(Add Screenshot Here)

## Dashboard
(Add Screenshot Here)

## Add Record Page
(Add Screenshot Here)

---

# Installation & Setup

## Clone Repository

```bash
git clone https://github.com/aryanpal935/lifeline-healthcare-system.git
```

---

## Open Project

Open the project in:
- VS Code
- IntelliJ IDEA
- Spring Tool Suite

---

## Configure Database

Update your `application.properties` file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lifeline_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

## Run Project

Using Maven:

```bash
mvn spring-boot:run
```

Or run:
`LifelineApplication.java`

---

# Future Improvements

- Appointment Booking System
- Doctor Portal
- JWT Authentication
- Email Notifications
- Report Generation
- Admin Dashboard
- API Integration

---

# Learning Goals

This project was developed to improve understanding of:
- Backend Development
- Spring Boot Architecture
- Authentication & Security
- Database Integration
- MVC Pattern
- Git & GitHub Workflow

---

# Author

## Aryan Virendra Pal

GitHub:
https://github.com/aryanpal935

---

# License

This project is developed for learning and educational purposes.
