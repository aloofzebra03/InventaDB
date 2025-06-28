# InventaDB (JDBCProject)

A terminal-based Java application demonstrating CRUD operations on a relational database using JDBC and MySQL.

## Overview

This Project implements a simple inventory and sales management system. It connects to a MySQL database via JDBC, executes SQL scripts to set up schema and data, and provides a console menu for performing Create, Read, Update, and Delete operations across multiple tables.

## Key Features

* **Schema Initialization**

  * Create `COMPANY`, `PRODUCT`, `INVENTORY`, and `SALES` tables via `sql/create.sql`.
  * Enforce referential integrity with foreign keys and cascade deletes in `sql/alter.sql`.
* **Data Population**

  * Insert sample records into each table using `sql/insert.sql`.
  * Update null references to establish proper relationships with `sql/update.sql`.
* **JDBC Operations**

  * Establish connection using `DriverManager` and MySQL Connector/J.
  * Perform CRUD on all tables:

    * **Add** new companies, products, inventory entries, and sales.
    * **View** list of records or search by primary key.
    * **Modify** existing records (e.g., update price or stock).
    * **Delete** records with cascading effects.
* **Console Interface**

  * Text-based menus guide the user through operations.
  * Input validation and error handling for robust interactions.

## Tech Stack

* **Language:** Java (JDK ≥ 11)
* **Database:** MySQL
* **JDBC Driver:** mysql-connector-java
* **Build Tool:** `javac` & `java` CLI, optional VS Code

## Prerequisites

* MySQL server installed and running.
* MySQL user with privileges for creating databases and tables.
* `mysql-connector-java-<version>.jar` on the classpath.
* JDK 11 or higher installed.

## Folder Structure

```
CompanyProductsJDBC/                 # Project root
├── sql/                             # SQL scripts
│   ├── create.sql                   # Create database & tables fileciteturn4file3
│   ├── insert.sql                   # Insert sample data fileciteturn4file4
│   ├── update.sql                   # Update relationships fileciteturn4file5
│   └── alter.sql                    # Add foreign keys & constraints fileciteturn4file2
├── src/                             # Java source
│   └── imt2022036_company_products_db.java   # Main application
├── class/                           # Compiled `.class` files
│   └── *.class
└── README.md                        # Project documentation
```

## Setup & Build

1. **Execute SQL scripts:**

   ```sh
   mysql -u <username> -p < sql/create.sql
   mysql -u <username> -p < sql/insert.sql
   mysql -u <username> -p < sql/update.sql
   mysql -u <username> -p < sql/alter.sql
   ```
2. **Compile Java code:**

   ```sh
   javac -cp "/path/to/mysql-connector-java.jar" -d class src/imt2022036_company_products_db.java
   ```

## Run Application

```sh
java -cp "class:/path/to/mysql-connector-java.jar" imt2022036_company_products_db
```

Follow the console menu to perform CRUD operations on companies, products, inventory, and sales.
