# ⚡ Electricity Bill Management System

A Java Swing desktop application for managing electricity consumers, bills, and payments.

---

## 📁 Project Structure

```
electricity-bill-system/
├── schema.sql                   ← Run this in MySQL first
├── src/
│   ├── Main.java                ← Entry point
│   ├── model/
│   │   ├── Admin.java
│   │   ├── Consumer.java
│   │   ├── Bill.java
│   │   └── Payment.java
│   ├── dao/
│   │   ├── AdminDAO.java
│   │   ├── ConsumerDAO.java
│   │   ├── BillDAO.java
│   │   └── PaymentDAO.java
│   ├── service/
│   │   ├── ConsumerService.java
│   │   ├── BillService.java
│   │   └── PaymentService.java
│   └── ui/
│       ├── AppTheme.java
│       ├── LoginFrame.java
│       ├── AdminDashboard.java
│       └── ConsumerDashboard.java
└── lib/
    └── mysql-connector-j-8.x.x.jar   ← Download separately
```

---

## ⚙️ Setup Instructions

### 1. MySQL Setup
```sql
-- Open MySQL and run:
source /path/to/schema.sql
```
This creates the `electricity_db` database with all tables and sample data.

### 2. Configure DB Credentials
Edit `src/util/DBConnection.java`:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/electricity_db";
private static final String USER     = "root";       // ← your MySQL username
private static final String PASSWORD = "root";       // ← your MySQL password
```

### 3. Download MySQL Connector/J
Download from: https://dev.mysql.com/downloads/connector/j/
Place the JAR in a `lib/` folder.

### 4. Compile
```bash
mkdir -p out
javac -cp "lib/mysql-connector-j-8.x.x.jar" \
      -sourcepath src \
      -d out \
      src/Main.java
```

### 5. Run
```bash
java -cp "out:lib/mysql-connector-j-8.x.x.jar" Main
# On Windows use semicolons:
java -cp "out;lib\mysql-connector-j-8.x.x.jar" Main
```

---

## 🔐 Default Credentials

| Role     | Username / Meter | Password  |
|----------|-----------------|-----------|
| Admin    | `admin`         | `admin123`|
| Consumer | `MTR-001`       | `pass123` |
| Consumer | `MTR-002`       | `pass456` |
| Consumer | `MTR-003`       | `pass789` |

---

## 🖥️ Features

### Admin Panel
- 📊 Dashboard with live stats (total consumers, pending bills, revenue)
- 👥 Consumer Management — Add, Edit, Delete, Search consumers
- 📄 Bill Management — Generate bills (auto rate by connection type), Mark as Paid, Delete
- 💳 Payment History — Full payment log with transaction IDs

### Consumer Portal
- 👤 Profile overview (meter number, type, address, status)
- 📄 My Bills — View all bills with color-coded status (Paid=green, Overdue=red)
- 💳 Pay Bill — In-app payment with method selection
- 🧾 Payment History — All past payments with transaction IDs

---

## 💡 Bill Rates (auto-filled)

| Connection Type | Rate/Unit |
|----------------|-----------|
| Residential    | Rs 15.00  |
| Commercial     | Rs 25.00  |
| Industrial     | Rs 20.00  |

---

## 🛠️ Using an IDE (IntelliJ / Eclipse)

1. Open the `src` folder as a Java project
2. Add `mysql-connector-j.jar` to project libraries
3. Set `Main` as the run configuration
4. Run!
