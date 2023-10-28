import java.util.*;

public class BugTrackingSystem {
    static class Bug {
        private int id;
        private String description;
        private String status;

        public Bug(int id, String description, String status) {
            this.id = id;
            this.description = description;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Bug ID: " + id + "\nDescription: " + description + "\nStatus: " + status;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Bug> bugs = new ArrayList<>();
        int nextId = 1;

        while (true) {
            System.out.println("\nBug Tracking System Menu:");
            System.out.println("1. Add Bug");
            System.out.println("2. View All Bugs");
            System.out.println("3. Update Bug Status");
            System.out.println("4. Quit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            switch (choice) {
                case 1:
                    System.out.print("Enter bug description: ");
                    String description = scanner.nextLine();
                    bugs.add(new Bug(nextId++, description, "Open"));
                    System.out.println("Bug added successfully!");
                    break;
                case 2:
                    if (bugs.isEmpty()) {
                        System.out.println("No bugs to display.");
                    } else {
                        for (Bug bug : bugs) {
                            System.out.println("\n" + bug);
                        }
                    }
                    break;
                case 3:
                    if (bugs.isEmpty()) {
                        System.out.println("No bugs to update.");
                    } else {
                        System.out.print("Enter bug ID: ");
                        int bugId = scanner.nextInt();
                        scanner.nextLine();  // Consume the newline
                        System.out.print("Enter new status: ");
                        String newStatus = scanner.nextLine();

                        boolean bugFound = false;
                        for (Bug bug : bugs) {
                            if (bug.getId() == bugId) {
                                bug.setStatus(newStatus);
                                bugFound = true;
                                System.out.println("Bug status updated successfully!");
                                break;
                            }
                        }

                        if (!bugFound) {
                            System.out.println("Bug not found with ID: " + bugId);
                        }
                    }
                    break;
                case 4:
                    System.out.println("Exiting Bug Tracking System.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
