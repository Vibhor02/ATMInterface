import java.sql.*;
import java.util.Scanner;

public class ATMInterface {
    public static void main(String args[]) {
        Connection con = null;
        try {
            String url = "jdbc:mysql://localhost/atm";
            String username = "root";
            String password = "";
            con = DriverManager.getConnection(url, username, password);

            if (!con.isClosed())
                System.out.println("Successfully connected to MySQL server...");
            Scanner scanner = new Scanner(System.in);

            // Main loop
            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Login");
                System.out.println("2. Create New Account");
                System.out.println("3. Exit");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        login(con, scanner);
                        break;
                    case 2:
                        createNewAccount(con, scanner);
                        break;
                    case 3:
                        System.out.println("Thank you for using the ATM!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Validate account
    private static boolean validateAccount(Connection connection, String accountNumber, String pin) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ? AND pin = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.setString(2, pin);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
    }

    // Get account balance
    private static double getBalance(Connection connection, String accountNumber) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
            return 0.0;
        }
    }

    // Withdraw from account
    private static boolean withdraw(Connection connection, String accountNumber, double amount) throws SQLException {
        double currentBalance = getBalance(connection, accountNumber);
        if (currentBalance >= amount) {
            String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDouble(1, currentBalance - amount);
                statement.setString(2, accountNumber);
                statement.executeUpdate();
            }
            return true;
        }
        return false;
    }

    // Login
    private static void login(Connection connection, Scanner scanner) {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.next();
        System.out.print("Enter PIN: ");
        String pin = scanner.next();

        try {
            if (validateAccount(connection, accountNumber, pin)) {
                System.out.println("Welcome to the ATM!");

                while (true) {
                    System.out.println("Choose an option:");
                    System.out.println("1. Balance Inquiry");
                    System.out.println("2. Withdraw");
                    System.out.println("3. Exit");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            double balance = getBalance(connection, accountNumber);
                            System.out.println("Your balance is: $" + balance);
                            break;
                        case 2:
                            System.out.print("Enter withdrawal amount: $");
                            double amount = scanner.nextDouble();
                            if (withdraw(connection, accountNumber, amount)) {
                                System.out.println("Withdrawal successful.");
                            } else {
                                System.out.println("Insufficient funds.");
                            }
                            break;
                        case 3:
                            System.out.println("Thank you for using the ATM!");
                            return;
                        default:
                            System.out.println("Invalid option. Try again.");
                    }
                }
            } else {
                System.out.println("Invalid account number or PIN. Try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create a new account
    private static void createNewAccount(Connection connection, Scanner scanner) {
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter your full name: ");
        String fullName = scanner.nextLine();

        System.out.print("Enter a 4-digit PIN: ");
        String pin = scanner.next();
        

        System.out.print("Enter a 4-digit Account Number: ");
        String account_number = scanner.next();

        double initialBalance = 5000.0; // You can set an initial balance

        // Insert the new account into the database
        try {
            String sql = "INSERT INTO accounts (full_name, pin, balance,account_number) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, fullName);
                statement.setString(2, pin);
                statement.setDouble(3, initialBalance);
                statement.setString(4, account_number);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Account creation successful.");
                } else {
                    System.out.println("Account creation failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
