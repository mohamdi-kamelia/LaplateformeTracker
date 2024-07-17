import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Db dbManager = new Db();
            while (true) {
                System.out.println("Choisissez une option :");
                System.out.println("1. Ajouter un étudiant");
                System.out.println("2. Mettre à jour l'étudiant");
                System.out.println("3. Supprimer un étudiant");
                System.out.println("4. Afficher tous les étudiants");
                System.out.println("5. Rechercher un étudiant par identifiant");
                System.out.println("6. Trier les étudiants");
                System.out.println("7. Recherche avancée par moyenne de notes");
                System.out.println("8. Statistiques");
                System.out.println("9. Exporter vers un fichier CSV");
                System.out.println("10. Importer depuis un fichier CSV");
                System.out.println("11. Quitter");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (choice) {
                    case 1:
                        System.out.print("Prénom : ");
                        String firstName = scanner.nextLine();
                        System.out.print("Nom : ");
                        String lastName = scanner.nextLine();
                        System.out.print("Âge : ");
                        int age = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Grade : ");
                        String grade = scanner.nextLine();
                        dbManager.addStudent(firstName, lastName, age, grade);
                        break;
                    case 2:
                        System.out.print("ID de l'étudiant à mettre à jour : ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Nouveau prénom : ");
                        String newFirstName = scanner.nextLine();
                        System.out.print("Nouveau nom : ");
                        String newLastName = scanner.nextLine();
                        System.out.print("Nouvel âge : ");
                        int newAge = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Nouveau grade : ");
                        String newGrade = scanner.nextLine();
                        dbManager.updateStudent(updateId, newFirstName, newLastName, newAge, newGrade);
                        break;
                    case 3:
                        System.out.print("ID de l'étudiant à supprimer : ");
                        int deleteId = scanner.nextInt();
                        dbManager.deleteStudent(deleteId);
                        break;
                    case 4:
                        dbManager.printStudents();
                        break;
                    case 5:
                        System.out.print("Entrez l'ID de l'étudiant à rechercher : ");
                        int searchId = scanner.nextInt();
                        dbManager.searchStudentById(searchId);
                        break;
                    case 6:
                        System.out.println("Trier par :");
                        System.out.println("1. ID");
                        System.out.println("2. Prénom");
                        System.out.println("3. Nom");
                        System.out.println("4. Âge");
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
                                System.out.println("Choix invalide. Tri par défaut par ID.");
                                dbManager.setSortMethod("id");
                        }
                        break;
                    case 7:
                        System.out.println("Recherche avancée par moyenne de notes :");
                        System.out.println("1. 0-20");
                        System.out.println("2. 20-40");
                        System.out.println("3. 40-60");
                        System.out.println("4. 60-80");
                        System.out.println("5. 80-100");
                        int interval = scanner.nextInt();
                        dbManager.advancedSearchByMoyeneeDeNotes(interval);
                        break;
                    case 8:
                        System.out.println("Statistiques par :");
                        System.out.println("1. Grade");
                        System.out.println("2. Âge");
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
                                System.out.println("Choix invalide. Statistiques par défaut par Grade.");
                                dbManager.calculateStatisticsByGrade();
                        }
                        break;
                    case 9:
                        System.out.print("Entrez le chemin complet du fichier CSV pour l'exportation : ");
                        String exportFilePath = scanner.nextLine();
                        dbManager.exportToCSV(exportFilePath);
                        break;
                    case 10:
                        System.out.print("Entrez le chemin complet du fichier CSV pour l'importation : ");
                        String importFilePath = scanner.nextLine();
                        dbManager.importFromCSV(importFilePath);
                        break;
                    case 11:
                        dbManager.close();
                        System.out.println("Sortie...");
                        return;
                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

