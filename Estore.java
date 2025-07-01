import java.sql.*;
import java.util.Scanner;

public class Estore {

    private static final String url = "jdbc:mysql://localhost:3306/data";
    private static final String username = "root";
    private static final String password = "*******";

    // Variables
    private static int id;
    private static String name;
    private static double price;
    private static int quantity;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Simple E-Store Management System");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int choice;
        while (true) {
            System.out.println(
                    "\n1. Add Item\n2. View Items\n3. Update Item Quntity\n4. Update by ID\n5. Delete Item\n6. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addItem(sc);
                    break;
                case 2:
                    viewItems();
                    break;
                case 3:
                    updateQunt(sc);
                    break;
                case 4:
                    updateID(sc);
                    break;
                case 5:
                    deleteItem(sc);
                    break;
                case 6:
                    // exit();
                    System.out.println("Exiting system...");
                    exit();
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

    }

    public static void addItem(Scanner sc) {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            sc.nextLine(); // Clear buffer
            System.out.print("Enter Item Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Item Price: ");
            double price = sc.nextDouble();
            if (price < 0) {
                System.out.println("Price cannot be negative.");
                return;
            }

            System.out.print("Enter Item Quantity: ");
            int quantity = sc.nextInt();
            if (quantity < 0) {
                System.out.println("Quantity cannot be negative.");
                return;
            }

            String query = "INSERT INTO store (name, price, quantity) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.executeUpdate();
            System.out.println("Item added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewItems() {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM store";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.printf("%-10s %-20s %-10s %-10s %-10s%n", "ID", "Name", "Price", "Quantity", "Total Price");
            while (rs.next()) {
                System.out.printf("%-10d %-20s %-10.2f %-10d %-10s%n", rs.getInt("id"), rs.getString("name"),
                        rs.getDouble("price"), rs.getInt("quantity"), rs.getDouble("price") * rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateQunt(Scanner sc) {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            System.out.print("Enter Item ID to update quantity: ");
            id = sc.nextInt();
            System.out.print("Enter new quantity: ");
            int quantity = sc.nextInt();
            if (quantity < 0) {
                System.out.println("Quantity cannot be negative. Please enter a valid quantity.");
                return;
            }
            String query = "UPDATE store SET quantity = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, quantity);
            ps.setInt(2, id);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Item quantity updated successfully.");
            } else {
                System.out.println("Item not found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateID(Scanner sc) {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            System.out.print("Enter Item ID to update: ");
            id = sc.nextInt();
            System.out.print("Enter new Item Name: ");
            name = sc.next();
            System.out.print("Enter new Item Price: ");
            price = sc.nextDouble();
            if (price < 0) {
                System.out.println("Price cannot be negative. Please enter a valid price.");
                return;
            }
            System.out.print("Enter new Item Quantity: ");
            int quantity = sc.nextInt();
            if (quantity < 0) {
                System.out.println("Quantity cannot be negative. Please enter a valid quantity.");
                return;
            }
            String query = "UPDATE store SET name = ?, price = ?, quantity = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.setInt(4, id);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Item updated successfully.");
            } else {
                System.out.println("Item not found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteItem(Scanner sc) {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            System.out.print("Enter Item ID to delete: ");
            int id = sc.nextInt();
            String query = "DELETE FROM store WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Item deleted successfully.");
            } else {
                System.out.println("Item not found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void exit() {
        System.exit(0);
    }

}
