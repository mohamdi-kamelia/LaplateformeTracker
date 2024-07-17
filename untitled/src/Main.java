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
                System.out.println("9. Quitter");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Entrez le prénom : ");
                        String firstName = scanner.nextLine();
                        System.out.print("Entrez le nom de famille : ");
                        String lastName = scanner.nextLine();
                        System.out.print("Entrez l'âge : ");
                        int age = scanner.nextInt();
                        scanner.nextLine(); // Consommer la nouvelle ligne
                        System.out.print("Entrez la note : ");
                        String grade = scanner.nextLine();
                        dbManager.addStudent(firstName, lastName, age, grade);
                        break;
                    case 2:
                        System.out.print("Entrez l'identifiant de l'étudiant à mettre à jour : ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // Consommer la nouvelle ligne
                        System.out.print("Entrez le prénom : ");
                        firstName = scanner.nextLine();
                        System.out.print("Entrez le nom de famille : ");
                        lastName = scanner.nextLine();
                        System.out.print("Entrez l'âge : ");
                        age = scanner.nextInt();
                        scanner.nextLine(); // Consommer la nouvelle ligne
                        System.out.print("Entrez la note : ");
                        grade = scanner.nextLine();
                        dbManager.updateStudent(updateId, firstName, lastName, age, grade);
                        break;
                    case 3:
                        System.out.print("Entrez l'identifiant de l'étudiant à supprimer : ");
                        int deleteId = scanner.nextInt();
                        dbManager.deleteStudent(deleteId);
                        break;
                    case 4:
                        dbManager.printStudents();
                        break;
                    case 5:
                        System.out.print("Entrez l'identifiant de l'étudiant à rechercher : ");
                        int searchId = scanner.nextInt();
                        dbManager.searchStudentById(searchId);
                        break;
                    case 6:
                        System.out.println("Choisissez une méthode de tri :");
                        System.out.println("1. Prénom");
                        System.out.println("2. Nom de famille");
                        System.out.println("3. Âge");
                        System.out.println("4. Note");
                        int sortChoice = scanner.nextInt();
                        scanner.nextLine(); // Consommer la nouvelle ligne
                        switch (sortChoice) {
                            case 1:
                                dbManager.setSortMethod("first_name");
                                break;
                            case 2:
                                dbManager.setSortMethod("last_name");
                                break;
                            case 3:
                                dbManager.setSortMethod("age");
                                break;
                            case 4:
                                dbManager.setSortMethod("grade");
                                break;
                            default:
                                System.out.println("Choix invalide. Tri par identifiant.");
                                dbManager.setSortMethod("id");
                        }
                        dbManager.printStudents();
                        break;
                    case 7:
                        System.out.println("Choisissez un intervalle pour la moyenne de notes :");
                        System.out.println("1. De 0 à 20");
                        System.out.println("2. De 20 à 40");
                        System.out.println("3. De 40 à 60");
                        System.out.println("4. De 60 à 80");
                        System.out.println("5. De 80 à 100");
                        int intervalChoice = scanner.nextInt();
                        scanner.nextLine(); // Consommer la nouvelle ligne
                        dbManager.advancedSearchByMoyeneeDeNotes(intervalChoice);
                        break;
                    case 8:
                        System.out.println("Choisissez les statistiques :");
                        System.out.println("1. Par moyenne de notes");
                        System.out.println("2. Par tranche d'âge");
                        int statChoice = scanner.nextInt();
                        scanner.nextLine(); // Consommer la nouvelle ligne
                        if (statChoice == 1) {
                            dbManager.calculateStatisticsByGrade();
                        } else if (statChoice == 2) {
                            dbManager.calculateStatisticsByAge();
                        } else {
                            System.out.println("Choix invalide.");
                        }
                        break;

                    case 9:
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
