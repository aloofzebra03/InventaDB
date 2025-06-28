import java.sql.*;
import java.util.Scanner;

public class imt2022036_company_products_db {
        static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
        static final String DB_URL = "jdbc:mysql://localhost/jdbc?useSSL=false";
        static final String USER = "root";
        static final String PASS = "admin";
    
        private static void rollback(Connection conn) {
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Transaction rolled back.");
                }
            } catch (SQLException se2) {
                System.err.println("Error during rollback: " + se2.getMessage());
                se2.printStackTrace(); 
            }
        }

        private static void closeResources(Statement stmt, Connection conn) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                System.err.println("Error closing statement: " + se2.getMessage());
                se2.printStackTrace(); 
            }
        
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se2) {
                System.err.println("Error closing connection: " + se2.getMessage());
                se2.printStackTrace();
            }
        }
        
        private static void displayMenu() {
            System.out.println("\nCompany-Product Database Operations");
            System.out.println("1. Add Company");
            System.out.println("2. Add Inventory");
            System.out.println("3. Add Product");
            System.out.println("4. Add Sales");
            System.out.println("5. List of Products by Company");
            System.out.println("6. Inventory Value by Company");
            System.out.println("7. Products Never Sold");
            System.out.println("8. Inventory locations in order of Value"); 
            System.out.println("9. Update Product Price");
            System.out.println("10. List Products Sold with Customer and Sales Information");
            System.out.println("11. Print highest paying customer per company");
            System.out.println("12. Delete product by productid");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
        }
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            Connection conn = null;
            Statement stmt = null;
    
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                conn.setAutoCommit(false); 
    
                int choice;
                do {
                    displayMenu();
                    choice = sc.nextInt();
                    sc.nextLine(); 
    
                    switch (choice) {
                        case 1:
                            addCompany(sc, conn);
                            break;
                        case 2:
                            addInventory(sc, conn);
                            break;
                        case 3:
                            addProduct(sc, conn);
                            break;
                        case 4:
                            addSales(sc, conn);
                            break;
                        case 5:
                            listOfProductsByCompany(sc, conn);
                            break;
                        case 6:
                            inventoryValueByCompany(sc, conn);
                            break;
                        case 7:
                            productsNeverSold(sc, conn);
                            break;
                        case 8:
                            highValueInventoryLocations(sc, conn);
                            break;
                        case 9:
                            updateProductPrice(sc,conn);
                            break;
                        case 10:
                            listProductsSoldWithInfo(sc,conn);
                            break;
                        case 11:
                            findHighestPayingCustomerPerCompany(sc,conn);
                            break;
                        case 12:
                            deleteProduct(sc,conn);
                            break;
                        case 0:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                } while (choice != 0);
    
            } catch (SQLException se) {
                se.printStackTrace();
                rollback(conn); 
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
              closeResources(stmt, conn);
            } 
            System.out.println("Goodbye!");
        }

        private static void addCompany(Scanner sc, Connection conn) throws SQLException {
            System.out.print("Enter company ID: ");
            int companyId = sc.nextInt();
            sc.nextLine(); // Consume newline
            System.out.print("Enter company name: ");
            String companyName = sc.nextLine();
            System.out.print("Enter industry: ");
            String industry = sc.nextLine();
            System.out.print("Enter address: ");
            String address = sc.nextLine();
            System.out.print("Enter website: ");
            String website = sc.nextLine();
            System.out.print("Enter contact phone: ");
            String phone = sc.nextLine();
        
            String sql = "INSERT INTO COMPANY (company_id, company_name, industry, address, website, contact_phone) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, companyId); // Set the company ID
                pstmt.setString(2, companyName);
                pstmt.setString(3, industry);
                pstmt.setString(4, address);
                pstmt.setString(5, website);
                pstmt.setString(6, phone);
        
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Company added successfully.");
                    conn.commit();
                } else {
                    rollback(conn);
                    System.out.println("Failed to add company.");
                }
                conn.commit();
            } catch (SQLException se) {
                rollback(conn);
                throw se; // Rethrow for error handling
            }
        }
        

        private static void addInventory(Scanner sc, Connection conn) throws SQLException {
            System.out.print("Enter inventory ID: ");
            int inventoryId = sc.nextInt();
            System.out.print("Enter location: ");
            sc.nextLine(); // Consume newline
            String location = sc.nextLine();
            System.out.print("Enter company ID : ");
            String companyIdStr = sc.nextLine();
        
            try {
                conn.setAutoCommit(false); // Begin transaction
        
                // Step 1: Insert with company_id as NULL
                String insertSql = "INSERT INTO INVENTORY (inventory_id, location, company_id) VALUES (?, ?, NULL)"; 
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, inventoryId);
                    insertStmt.setString(2, location);           
        
                    int rowsAffected = insertStmt.executeUpdate();
                    if (rowsAffected == 0) {
                        rollback(conn);
                        System.out.println("Failed to add inventory (initial insert).");
                        return; 
                    }
                } 
        
                // Step 2: Update with company_id
                if (!companyIdStr.isEmpty()) {
                    int companyId = Integer.parseInt(companyIdStr);        
                    String updateSql = "UPDATE INVENTORY SET company_id = ? WHERE inventory_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, companyId);
                        updateStmt.setInt(2, inventoryId);
        
                        if (updateStmt.executeUpdate() > 0) {
                            System.out.println("Inventory updated with company ID.");
                        } else {
                            rollback(conn); 
                            System.out.println("Failed to update inventory with company ID.");
                            return; 
                        }
                    } 
                } 
        
                conn.commit(); // Commit if everything is successful
            } catch (SQLException se) {
                rollback(conn);
                throw se; // Rethrow for error handling
            } finally {
                conn.setAutoCommit(true); // Restore auto-commit
            }
        }
        
        private static void addProduct(Scanner sc, Connection conn) throws SQLException {
            System.out.print("Enter product ID: ");
            int productId = sc.nextInt();
            System.out.print("Enter product name: ");
            sc.nextLine(); 
            String productName = sc.nextLine();
            System.out.print("Enter price: ");
            double price = sc.nextDouble();
            System.out.print("Enter quantity: ");
            int quantity = sc.nextInt(); 
            System.out.print("Enter inventory ID: ");
            sc.nextLine(); 
            String inventoryIdStr = sc.nextLine();
    
            try {
                conn.setAutoCommit(false);
    
                String insertSql = "INSERT INTO PRODUCT (product_id, product_name, price, quantity, inventory_id) VALUES (?, ?, ?, ?, NULL)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, productId);
                    insertStmt.setString(2, productName);
                    insertStmt.setDouble(3, price);
                    insertStmt.setInt(4, quantity);
                    insertStmt.executeUpdate(); 
                } 
    
                if (!inventoryIdStr.isEmpty()) {
                    int inventoryId = Integer.parseInt(inventoryIdStr);
                    String updateSql = "UPDATE PRODUCT SET inventory_id = ? WHERE product_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, inventoryId);
                        updateStmt.setInt(2, productId);
                        updateStmt.executeUpdate(); 
                    } 
                } 
                conn.commit(); 
            } catch (SQLException se) {
                rollback(conn);
                throw se; 
            } finally {
                conn.setAutoCommit(true);
            }
        }
    
        private static void addSales(Scanner sc, Connection conn) throws SQLException {
            System.out.print("Enter sale ID: ");
            int saleId = sc.nextInt();
            System.out.print("Enter customer name: ");
            sc.nextLine(); 
            String customerName = sc.nextLine();
            System.out.print("Enter sales date (YYYY-MM-DD): ");
            String salesDate = sc.nextLine();
            System.out.print("Enter product ID: ");
            int productId = sc.nextInt();
            System.out.print("Enter quantity: ");
            int quantity = sc.nextInt();
    
            try {
                conn.setAutoCommit(false); 
    
                double unitPrice = 0.0;
                String getPriceSql = "SELECT price FROM PRODUCT WHERE product_id = ?";
                try (PreparedStatement getPriceStmt = conn.prepareStatement(getPriceSql)) {
                    getPriceStmt.setInt(1, productId);
                    try (ResultSet rs = getPriceStmt.executeQuery()) {
                        if (rs.next()) {
                            unitPrice = rs.getDouble("price");
                        } else {
                            rollback(conn);
                            System.out.println("Product not found.");
                            return;
                        }
                    }
                }
    
                String insertSql = "INSERT INTO SALES (sales_id, customer_name, sales_date, quantity, unit_price, product_id) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, saleId);
                    insertStmt.setString(2, customerName);
                    insertStmt.setDate(3, Date.valueOf(salesDate));
                    insertStmt.setInt(4, quantity);
                    insertStmt.setDouble(5, unitPrice); 
                    insertStmt.setInt(6, productId);
                    insertStmt.executeUpdate(); 
                } 
    
                String updateSql = "UPDATE PRODUCT SET quantity = quantity - ? WHERE product_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, quantity);
                    updateStmt.setInt(2, productId);
                    if (updateStmt.executeUpdate() == 0) {
                        rollback(conn);
                        System.out.println("Failed to update product quantity. Possible reasons: Product not found or insufficient stock.");
                        return;
                    }
                } 
    
                conn.commit(); 
            } catch (SQLException se) {
                rollback(conn);
                throw se; 
            } finally {
                conn.setAutoCommit(true);
            }
        }
        

        private static void listOfProductsByCompany(Scanner sc, Connection conn) throws SQLException {
            System.out.print("Enter company name: ");
            String companyName = sc.nextLine();
        
            String sql = "SELECT PRODUCT.product_name, PRODUCT.price, INVENTORY.location " +
                         "FROM PRODUCT " +
                         "JOIN INVENTORY ON PRODUCT.inventory_id = INVENTORY.inventory_id " +
                         "JOIN COMPANY ON INVENTORY.company_id = COMPANY.company_id " +
                         "WHERE COMPANY.company_name = ?";
        
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, companyName);
        
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) { 
                        System.out.println("Products by " + companyName + ":");
                        do {
                            String productName = rs.getString("product_name");
                            double price = rs.getDouble("price");
                            String location = rs.getString("location"); 
                            System.out.println("- " + productName + " (Price: " + price + ", Location: " + location + ")");
                        } while (rs.next());
                    } else {
                        System.out.println("Company not found.");
                    }
                } 
            } catch (SQLException se) {
                throw se; // Rethrow for error handling
            } 
        }
        

        private static void inventoryValueByCompany(Scanner sc, Connection conn) throws SQLException {
            String sql = "SELECT COMPANY.company_name, SUM(PRODUCT.price * PRODUCT.quantity) AS total_value " +
                         "FROM COMPANY " +
                         "JOIN INVENTORY ON COMPANY.company_id = INVENTORY.company_id " +
                         "JOIN PRODUCT ON INVENTORY.inventory_id = PRODUCT.inventory_id " +
                         "GROUP BY COMPANY.company_id";
        
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
        
                System.out.println("Inventory Value by Company:");
                while (rs.next()) {
                    String companyName = rs.getString("company_name");
                    double totalValue = rs.getDouble("total_value");
                    System.out.println(companyName + ": " + totalValue);
                }
            } catch (SQLException se) {
                throw se; // Rethrow for error handling
            } 
        }

        private static void productsNeverSold(Scanner sc, Connection conn) throws SQLException {
            String sql = "SELECT product_name FROM PRODUCT WHERE product_id NOT IN (SELECT product_id FROM SALES)";
        
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
        
                System.out.println("Products Never Sold:");
                if (rs.next()) { // Check if there are any unsold products
                    do {
                        String productName = rs.getString("product_name");
                        System.out.println("- " + productName);
                    } while (rs.next()); 
                } else {
                    System.out.println("All products have been sold at least once.");
                }
            } catch (SQLException se) {
                throw se; // Rethrow for error handling
            } 
        }
        
        private static void highValueInventoryLocations(Scanner sc, Connection conn) throws SQLException {
            String sql = "SELECT location, SUM(PRODUCT.price * PRODUCT.quantity) AS total_value " +
                         "FROM INVENTORY JOIN PRODUCT ON INVENTORY.inventory_id = PRODUCT.inventory_id " +
                         "GROUP BY location " +
                         "ORDER BY total_value DESC"; 
    
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
    
                System.out.println("High-Value Inventory Locations:");
                while (rs.next()) {
                    String location = rs.getString("location");
                    double totalValue = rs.getDouble("total_value");
                    System.out.println(location + ": " + totalValue);
                }
            } catch (SQLException se) {
                throw se; // Rethrow for error handling
            } 
        }

        private static void updateProductPrice(Scanner sc, Connection conn) throws SQLException {
            System.out.print("Enter product name: ");
            String productName = sc.nextLine();
            System.out.print("Enter price increase percentage: ");
            double percentageIncrease = sc.nextDouble() / 100; 
        
            String sql = "UPDATE PRODUCT SET price = price * (1 + ?) WHERE product_name = ?";
        
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, percentageIncrease);
                pstmt.setString(2, productName);
        
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Product price updated.");
                    conn.commit(); 
                } else {
                    System.out.println("Product not found.");
                }
            } catch (SQLException se) {
                rollback(conn);
                throw se; 
            } 
        }

        private static void listProductsSoldWithInfo(Scanner sc, Connection conn) throws SQLException {
            String sql = "SELECT PRODUCT.product_name, SALES.customer_name, SALES.sales_date, SALES.quantity, SALES.unit_price " +
                         "FROM PRODUCT " +
                         "JOIN SALES ON PRODUCT.product_id = SALES.product_id";
        
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
        
                System.out.println("Products Sold with Customer and Sales Information:");
                System.out.format("%-20s %-20s %-15s %-10s %s\n", "Product Name", "Customer Name", "Sales Date", "Quantity", "Unit Price"); 
        
                while (rs.next()) {
                    String productName = rs.getString("product_name");
                    String customerName = rs.getString("customer_name");
                    Date salesDate = rs.getDate("sales_date");
                    int quantity = rs.getInt("quantity");
                    double unitPrice = rs.getDouble("unit_price");
        
                    System.out.format("%-20s %-20s %-15s %-10d %.2f\n", productName, customerName, salesDate, quantity, unitPrice); 
                }
            } catch (SQLException se) {
                throw se; // Rethrow for error handling
            } 
        }
        
        private static void findHighestPayingCustomerPerCompany(Scanner sc, Connection conn) throws SQLException {
            String sql = "SELECT company_name, customer_name, total_spend " +
                         "FROM ( " +
                             "SELECT COMPANY.company_name, SALES.customer_name, SUM(SALES.quantity * SALES.unit_price) AS total_spend, " +
                                     "ROW_NUMBER() OVER (PARTITION BY COMPANY.company_name ORDER BY SUM(SALES.quantity * SALES.unit_price) DESC) AS row_num " +
                             "FROM COMPANY " +
                             "JOIN INVENTORY ON COMPANY.company_id = INVENTORY.company_id " +
                             "JOIN PRODUCT ON INVENTORY.inventory_id = PRODUCT.inventory_id " + 
                             "JOIN SALES ON PRODUCT.product_id = SALES.product_id " +
                             "GROUP BY COMPANY.company_name, SALES.customer_name" +
                         ") temp " +
                         "WHERE row_num = 1";
        
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
        
                System.out.println("Highest Paying Customers per Company:");
                while (rs.next()) {
                    String companyName = rs.getString("company_name");
                    String customerName = rs.getString("customer_name");
                    double totalSpend = rs.getDouble("total_spend");
        
                    System.out.println(companyName + ": " + customerName + " (Total Spend: " + totalSpend + ")"); 
                }
            } catch (SQLException se) {
                throw se; // Rethrow for error handling
            } 
        }

        private static void deleteProduct(Scanner sc, Connection conn) throws SQLException {
            System.out.print("Enter the ID of the product to delete: ");
            int productId = sc.nextInt();
        
            String deleteSql = "DELETE FROM PRODUCT WHERE product_id = ?";
        
            try {
                conn.setAutoCommit(false); // Begin a transaction
        
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, productId);
                    int rowsAffected = deleteStmt.executeUpdate();
        
                    if (rowsAffected > 0) {
                        System.out.println("Product deleted successfully.");
                    } else {
                        System.out.println("Product not found.");
                    }
                } 
                conn.commit(); // Commit changes if successful
        
            } catch (SQLException se) {
                conn.rollback(); // Rollback in case of errors
                System.err.println("Error deleting product: " + se.getMessage());
                throw se; // Rethrow for further handling if needed
            } finally {
                conn.setAutoCommit(true); // Restore auto-commit mode
            } 
        }
        
    }

        
        
        
        


        


        
        

