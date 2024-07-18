import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    // Method to establish a connection to the database
    public static Db connectionToDatabase(Scanner scannerConnection) {
        try {
            // Prompt the user to enter the database name
            System.out.println("Enter the database name: ");
            String dbName = scannerConnection.nextLine();

            // Prompt the user to enter the username
            System.out.println("Enter the username: ");
            String username = scannerConnection.nextLine();

            // Prompt the user to enter the password
            System.out.println("Enter the password: ");
            String password = scannerConnection.nextLine();

            // Return a new database connection
            return new Db(dbName, username, password);
        } catch (SQLException e) {
            // If there is an SQL exception, wrap it in a RuntimeException and throw it
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Establish connection to the database
            Db dbManager = connectionToDatabase(scanner);

            while (true) {
                // Display the main menu options
                System.out.println("Choose an option:");
                System.out.println("1. Add a student");
                System.out.println("2. Update a student");
                System.out.println("3. Delete a student");
                System.out.println("4. Display all students");
                System.out.println("5. Search for a student");
                System.out.println("6. Sort students");
                System.out.println("7. Advanced search by average grades");
                System.out.println("8. Statistics");
                System.out.println("9. Export to a CSV file");
                System.out.println("10. Import from a CSV file");
                System.out.println("11. Exit");

                // Read the user's choice
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        // Add a new student
                        System.out.print("First name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Last name: ");
                        String lastName = scanner.nextLine();
                        System.out.print("Age: ");
                        int age = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Grade: ");
                        String grade = scanner.nextLine();
                        dbManager.addStudent(firstName, lastName, age, grade);
                        break;
                    case 2:
                        // Update an existing student
                        System.out.print("ID of the student to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("New first name: ");
                        String newFirstName = scanner.nextLine();
                        System.out.print("New last name: ");
                        String newLastName = scanner.nextLine();
                        System.out.print("New age: ");
                        int newAge = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("New grade: ");
                        String newGrade = scanner.nextLine();
                        dbManager.updateStudent(updateId, newFirstName, newLastName, newAge, newGrade);
                        break;
                    case 3:
                        // Delete a student
                        System.out.print("ID of the student to delete: ");
                        int deleteId = scanner.nextInt();
                        dbManager.deleteStudent(deleteId);
                        break;
                    case 4:
                        // Display all students
                        dbManager.printStudents();
                        break;
                    case 5:
                        // Search for a student using various methods
                        System.out.println("Choose a searching method:");
                        System.out.println("1. ID");
                        System.out.println("2. First Name");
                        System.out.println("3. Last Name");
                        System.out.println("4. Age");
                        System.out.println("5. Grade");
                        int searchChoice = scanner.nextInt();

                        switch (searchChoice) {
                            case 1:
                                dbManager.setSearchMethod("id");
                                System.out.print("Enter student ID to search: ");
                                int searchId = scanner.nextInt();
                                scanner.nextLine(); // Consume newline
                                dbManager.searchStudent(String.valueOf(searchId));
                                break;
                            case 2:
                                dbManager.setSearchMethod("first_name");
                                System.out.print("Enter student first name to search: ");
                                String searchFirstName = scanner.next();
                                dbManager.searchStudent(searchFirstName);
                                break;
                            case 3:
                                dbManager.setSearchMethod("last_name");
                                System.out.print("Enter student last name to search: ");
                                String searchLastName = scanner.next();
                                dbManager.searchStudent(searchLastName);
                                break;
                            case 4:
                                dbManager.setSearchMethod("age");
                                System.out.print("Enter student age to search: ");
                                int searchAge = scanner.nextInt();
                                scanner.nextLine(); // Consume newline
                                dbManager.searchStudent(String.valueOf(searchAge));
                                break;
                            case 5:
                                dbManager.setSearchMethod("grade");
                                System.out.print("Enter student grade to search: ");
                                int searchGrade = scanner.nextInt();
                                scanner.nextLine(); // Consume newline
                                dbManager.searchStudent(String.valueOf(searchGrade));
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                        break;
                    case 6:
                        // Sort students by various methods
                        System.out.println("Sort by:");
                        System.out.println("1. ID");
                        System.out.println("2. First Name");
                        System.out.println("3. Last Name");
                        System.out.println("4. Age");
                        System.out.println("5. Grade");
                        int sortChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        switch (sortChoice) {
                            case 1:
                                dbManager.setSortMethod("id");
                                break;
                            case 2:
                                dbManager.setSortMethod("first_name");
                                break;
                            case 3:
                                dbManager.setSortMethod("last_name");
                                break;
                            case 4:
                                dbManager.setSortMethod("age");
                                break;
                            case 5:
                                dbManager.setSortMethod("grade");
                                break;
                            default:
                                System.out.println("Invalid choice. Default sorting by ID.");
                                dbManager.setSortMethod("id");
                        }
                        break;
                    case 7:
                        // Advanced search by average grades
                        System.out.println("Display students from a certain interval :");
                        System.out.println("1. 00-5");
                        System.out.println("2. 06-10");
                        System.out.println("3. 11-15");
                        System.out.println("4. 16-20");
                        int interval = scanner.nextInt();
                        dbManager.searchByAverageGrade(interval);
                        break;
                    case 8:
                        // Display statistics
                        System.out.println("Statistics by:");
                        System.out.println("1. Grade");
                        System.out.println("2. Age");
                        int statChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        switch (statChoice) {
                            case 1:
                                dbManager.calculateStatisticsByGrade();
                                break;
                            case 2:
                                dbManager.calculateStatisticsByAge();
                                break;
                            default:
                                System.out.println("Invalid choice. Default statistics by Grade.");
                                dbManager.calculateStatisticsByGrade();
                        }
                        break;
                    case 9:
                        // Export student data to a CSV file
                        System.out.print("Enter the full path of the CSV file for export: ");
                        String exportFilePath = scanner.nextLine();
                        dbManager.exportToCSV(exportFilePath);
                        break;
                    case 10:
                        // Import student data from a CSV file
                        System.out.print("Enter the full path of the CSV file for import: ");
                        String importFilePath = scanner.nextLine();
                        dbManager.importFromCSV(importFilePath);
                        break;
                    case 11:
                        // Exit the program
                        dbManager.close();
                        System.out.println("Exiting...");
                        return;
                    default:
                        // Handle invalid choices
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            // Print SQL exception details
            e.printStackTrace();
        }
    }
}
