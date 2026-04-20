# 🏦 Java Console Banking System

> Production-style console banking app built with Java 17, JDBC, and MySQL — featuring layered architecture, custom exceptions, and real database persistence.

![Java](https://img.shields.io/badge/Java-17-orange)
![Maven](https://img.shields.io/badge/Build-Maven-blue)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)
![JDBC](https://img.shields.io/badge/Persistence-JDBC-green)
![Architecture](https://img.shields.io/badge/Architecture-Layered-purple)

---

## 🚀 What It Does

- ✅ User registration and login with validation
- ✅ Create Savings and Current accounts
- ✅ Deposit, Withdraw, Transfer funds
- ✅ View full transaction statement
- ✅ Real-time MySQL database persistence
- ✅ Custom exception handling
- ✅ Unique reference number per transaction

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Build Tool | Maven |
| Database | MySQL 8 |
| Persistence | JDBC |
| Logging | Logback |
| Testing | JUnit 5 |

---

## 🏗️ Project Architecture
src/main/java/com/banking/
├── model/         Account · Transaction · User
├── dao/           AccountDAO · TransactionDAO · UserDAO
├── service/       AccountService · AuthService
├── util/          DBConnection · InputValidator · AccountNumberGenerator
├── exception/     Custom exceptions
├── cli/           BankingCLI (menu system)
└── Main.java      Entry point

---

## ⚙️ Run Locally

### Prerequisites
- Java 17
- MySQL 8
- Maven

### Steps

**1. Clone the repo**
```bash
git clone https://github.com/raam214/java-banking-system.git
cd java-banking-system
```

**2. Set up MySQL database**
```sql
CREATE DATABASE banking_system;
```
Then run `schema.sql` in MySQL Workbench.

**3. Create config file**

Create `src/main/resources/db.properties`:

db.url=jdbc:mysql://localhost:3306/banking_system?useSSL=false&serverTimezone=UTC
db.username=root
db.password=your_password

**4. Build and run**
```bash
mvn clean install
java -jar target/banking-system-1.0.0.jar
```

---

## 📸 Demo
╔══════════════════════════════════════╗
║     JAVA CONSOLE BANKING SYSTEM      ║
║         Built by Ram Dukare          ║
╚══════════════════════════════════════╝
========== MAIN MENU ==========

Register
Login
Exit

========== BANKING MENU ==========

Create Account
Deposit
Withdraw
Transfer
View Statement
View My Accounts
Logout


---

## 👨‍💻 Author

**Ram Dukare** — [Portfolio](https://raam214.me/) 
