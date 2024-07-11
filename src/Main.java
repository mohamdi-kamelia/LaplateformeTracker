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
                System.out.println("4. Display all students");
                System.out.println("5. Search student by ID");
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
                        dbManager.printStudents();
                        break;
                    case 5:
                        System.out.print("Enter student ID to search: ");
                        int searchId = scanner.nextInt();
                        dbManager.searchStudentById(searchId);
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