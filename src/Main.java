import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Db dbManager = new Db();

            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Add student");
                System.out.println("2. Update student");
                System.out.println("3. Delete student");
                System.out.println("4. Display students");
                System.out.println("5. Search student");
                System.out.println("6. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter first name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Enter last name: ");
                        String lastName = scanner.nextLine();
                        System.out.print("Enter age: ");
                        int age = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter grade: ");
                        double grade = scanner.nextDouble();
                        dbManager.addStudent(firstName, lastName, age, grade);
                        break;
                    case 2:
                        System.out.print("Enter student ID to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter new first name: ");
                        String newFirstName = scanner.nextLine();
                        System.out.print("Enter new last name: ");
                        String newLastName = scanner.nextLine();
                        System.out.print("Enter new age: ");
                        int newAge = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter new grade: ");
                        String newGrade = scanner.nextLine();
                        dbManager.updateStudent(updateId, newFirstName, newLastName, newAge, newGrade);
                        break;
                    case 3:
                        System.out.print("Enter student ID to delete: ");
                        int deleteId = scanner.nextInt();
                        dbManager.deleteStudent(deleteId);
                        break;
                    case 4:
                        System.out.println("Choose a sorting method:");
                        System.out.println("1. ID");
                        System.out.println("2. First Name");
                        System.out.println("3. Last Name");
                        System.out.println("4. Age");
                        System.out.println("5. Grade");
                        int sortChoice = scanner.nextInt();
                        scanner.nextLine();
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
                                System.out.println("Invalid choice. Sorting by ID.");
                                dbManager.setSortMethod("id");
                        }
                        dbManager.printStudents();
                        break;
                    case 5:
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
                                scanner.nextLine();
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
                                scanner.nextLine();
                                dbManager.searchStudent(String.valueOf(searchAge));
                                break;
                            case 5:
                                dbManager.setSearchMethod("grade");
                                System.out.print("Enter student grade to search: ");
                                int searchGrade = scanner.nextInt();
                                scanner.nextLine();
                                dbManager.searchStudent(String.valueOf(searchGrade));
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                        break;
                    case 6:
                        dbManager.close();
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}