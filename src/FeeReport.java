import java.sql.*;
import java.util.Scanner;

public class FeeReport {
    public static void main(String[] args) {
        String dbUrl = "jdbc:mysql://localhost:3306/fee_management";
        String dbUser = "your_username"; 
        String dbPassword = "your_password"; 

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Fee Management System");
            System.out.println("1. Administrator Login");
            System.out.println("2. Student Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Administrator login
                    System.out.print("Enter Administrator Username: ");
                    String adminUsername = scanner.next();
                    System.out.print("Enter Administrator Password: ");
                    String adminPassword = scanner.next();
                    boolean isAdminAuthenticated = adminLogin(dbUrl, dbUser, dbPassword, adminUsername, adminPassword);

                    if (isAdminAuthenticated) {
                        // Administrator menu
                        System.out.println("Administrator menu:");
                        adminMenu(dbUrl, dbUser, dbPassword);
                    } else {
                        System.out.println("Administrator login failed.");
                    }
                    break;

                case 2:
                    // Student login
                    System.out.print("Enter Student ID: ");
                    int studentId = scanner.nextInt();
                    boolean isStudentAuthenticated = studentLogin(dbUrl, dbUser, dbPassword, studentId);

                    if (isStudentAuthenticated) {
                        // Student menu
                        System.out.println("Student Menu:");
                        studentMenu(dbUrl, dbUser, dbPassword, studentId);
                    } else {
                        System.out.println("Student login failed.");
                    }
                    break;

                case 3:
                    System.out.println("Exiting Fee Management System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    private static boolean adminLogin(String dbUrl, String dbUser, String dbPassword, String username, String password) {
        // Return true if authenticated, false otherwise
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM Administrators WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next(); // If a row is found, login is successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean studentLogin(String dbUrl, String dbUser, String dbPassword, int studentId) {
        // Return true if authenticated, false otherwise
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM Students WHERE student_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, studentId);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next(); // If a row is found, login is successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void adminMenu(String dbUrl, String dbUser, String dbPassword) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Administrator Menu:");
            System.out.println("1. View Student Details");
            System.out.println("2. Add New Student");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int adminChoice = scanner.nextInt();

            switch (adminChoice) {
                case 1:
                    // View student details
                    viewStudentDetails(dbUrl, dbUser, dbPassword);
                    break;

                case 2:
                    // Add new student
                    addNewStudent(dbUrl, dbUser, dbPassword);
                    break;
                    
                case 3:
                    // Logout
                    System.out.println("Logging out from Administrator account.");
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    private static void studentMenu(String dbUrl, String dbUser, String dbPassword, int studentId) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. View Fee Report");
            System.out.println("2. Logout");
            System.out.print("Enter your choice: ");
            int studentChoice = scanner.nextInt();

            switch (studentChoice) {
                case 1:
                    // View fee report
                    viewFeeReport(dbUrl, dbUser, dbPassword, studentId);
                    break;

                case 2:
                    // Logout
                    System.out.println("Logging out from Student account.");
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    private static void viewStudentDetails(String dbUrl, String dbUser, String dbPassword) {
        // logic to view student details
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM Students";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                System.out.println("Student Details:");
                while (resultSet.next()) {
                    int studentId = resultSet.getInt("student_id");
                    String studentName = resultSet.getString("student_name");
                    String studentEmail = resultSet.getString("student_email");
                    String studentPhone = resultSet.getString("student_phone");
                    double feeAmount = resultSet.getDouble("fee_amount");
                    System.out.println("ID: " + studentId + ", Name: " + studentName + ", Email: " + studentEmail + ", Phone: " + studentPhone + ", Fee Amount: $" + feeAmount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addNewStudent(String dbUrl, String dbUser, String dbPassword) {
        // logic to add a new student
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine();
        System.out.print("Enter student email: ");
        String studentEmail = scanner.nextLine();
        System.out.print("Enter student phone: ");
        String studentPhone = scanner.nextLine();
        System.out.print("Enter fee amount: ");
        double feeAmount = scanner.nextDouble();


        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String insertQuery = "INSERT INTO Students (student_name, student_email, student_phone,fee_amount) VALUES (?, ?, ?,?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, studentName);
                insertStatement.setString(2, studentEmail);
                insertStatement.setString(3, studentPhone);
                insertStatement.setDouble(4, feeAmount);
                int rowsAffected = insertStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Student registration successful.");
                } else {
                    System.out.println("Student registration failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewFeeReport(String dbUrl, String dbUser, String dbPassword, int studentId) {
        // logic to view fee report for a student
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT student_name, fee_amount FROM Students WHERE student_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, studentId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String studentName = resultSet.getString("student_name");
                    double feeAmount = resultSet.getDouble("fee_amount");
                    System.out.println("Student Name: " + studentName);
                    System.out.println("Fee Amount: $" + feeAmount);
                } else {
                    System.out.println("Student not found or fee amount not set.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
