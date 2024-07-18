import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class Db {
    private final Connection connection;
    private static String sortMethod = "id";
    private static String searchMethod = "id";

    // Constructor to initialize database connection
    public Db(String databaseName, String databaseUser, String databasePassword) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, databaseUser, databasePassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Driver not found");
        }
    }

    // Getter for the database connection
    public Connection getConnection() {
        return connection;
    }

    // Method to close the database connection
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Method to add a student to the database
    public void addStudent(String firstName, String lastName, int age, String grade) throws SQLException {
        String query = "INSERT INTO student (first_name, last_name, age, grade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setString(4, grade);
            statement.executeUpdate();
        }
    }

    // Method to update a student's information in the database
    public void updateStudent(int id, String firstName, String lastName, int age, String grade) throws SQLException {
        String query = "UPDATE student SET first_name = ?, last_name = ?, age = ?, grade = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setString(4, grade);
            statement.setInt(5, id);
            statement.executeUpdate();
        }
    }

    // Method to delete a student from the database
    public void deleteStudent(int id) throws SQLException {
        String query = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // Method to print all students, sorted by a specified method
    public void printStudents() throws SQLException {
        String query;
        switch (sortMethod) {
            case "first_name":
                query = "SELECT * FROM student ORDER BY first_name";
                break;
            case "last_name":
                query = "SELECT * FROM student ORDER BY last_name";
                break;
            case "age":
                query = "SELECT * FROM student ORDER BY age";
                break;
            case "grade":
                query = "SELECT * FROM student ORDER BY grade";
                break;
            default:
                query = "SELECT * FROM student ORDER BY id";
        }

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Students sorted by : " + sortMethod);
            while (rs.next()) {
                System.out.printf("ID: %d, First Name: %s, Last Name: %s, Age: %d, Grade: %s%n",
                        rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getInt("age"), rs.getString("grade"));
            }
        }
    }

    // Method to search for a student based on a specific method (e.g., ID, name)
    public void searchStudent(String valueToSearch) throws SQLException {
        String query = "SELECT * FROM student WHERE " + searchMethod + " = '" + valueToSearch + "'";
        System.out.println(query);
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.printf("ID: %d, First Name: %s, Last Name: %s, Age: %d, Grade: %s%n",
                            rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                            rs.getInt("age"), rs.getString("grade"));
                } else {
                    System.out.println("Student not found.");
                }
            }
        }
    }

    // Setter for the sort method
    public void setSortMethod(String sortMethod) {
        this.sortMethod = sortMethod;
    }

    // Setter for the search method
    public void setSearchMethod(String searchMethod) {
        this.searchMethod = searchMethod;
    }

    // Method to search students by average grade intervals
    public void searchByAverageGrade(int interval) throws SQLException {
        String query;
        switch (interval) {
            case 1:
                query = "SELECT * FROM student WHERE grade >= 0 AND grade <= 5";
                break;
            case 2:
                query = "SELECT * FROM student WHERE grade > 6 AND grade <= 10";
                break;
            case 3:
                query = "SELECT * FROM student WHERE grade > 11 AND grade <= 15";
                break;
            case 4:
                query = "SELECT * FROM student WHERE grade > 16 AND grade <= 20";
                break;
            default:
                throw new IllegalArgumentException("Interval not supported: " + interval);
        }

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Students in grade interval " + interval + ":");
            while (rs.next()) {
                System.out.printf("ID: %d, First Name: %s, Last Name: %s, Age: %d, Grade: %s \n",
                        rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getInt("age"), rs.getString("grade"));
            }
        }
    }

    // Method to calculate statistics by grade
    public void calculateStatisticsByGrade() throws SQLException {
        String query = "SELECT " +
                "SUM(CASE WHEN grade BETWEEN 0 AND 5 THEN 1 ELSE 0 END) AS range_0_5, " +
                "SUM(CASE WHEN grade BETWEEN 6 AND 10 THEN 1 ELSE 0 END) AS range_6_10, " +
                "SUM(CASE WHEN grade BETWEEN 11 AND 15 THEN 1 ELSE 0 END) AS range_11_15, " +
                "SUM(CASE WHEN grade BETWEEN 16 AND 20 THEN 1 ELSE 0 END) AS range_16_20 " +  // Removed the extra comma here
                "FROM student";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                int totalStudents = rs.getInt("range_0_5") + rs.getInt("range_6_10") +
                        rs.getInt("range_11_15") + rs.getInt("range_16_20");

                System.out.println("Statistics by Grade:");
                System.out.printf("00-5: %.2f%%\n", (rs.getInt("range_0_5") * 100.0 / totalStudents));
                System.out.printf("06-10: %.2f%%\n", (rs.getInt("range_6_10") * 100.0 / totalStudents));
                System.out.printf("11-15: %.2f%%\n", (rs.getInt("range_11_15") * 100.0 / totalStudents));
                System.out.printf("16-20: %.2f%%\n", (rs.getInt("range_16_20") * 100.0 / totalStudents));
            }
        }
    }

    // Method to calculate statistics by age
    public void calculateStatisticsByAge() throws SQLException {
        String query = "SELECT " +
                "SUM(CASE WHEN age BETWEEN 18 AND 20 THEN 1 ELSE 0 END) AS age_18_20, " +
                "SUM(CASE WHEN age BETWEEN 20 AND 22 THEN 1 ELSE 0 END) AS age_20_22, " +
                "SUM(CASE WHEN age BETWEEN 22 AND 24 THEN 1 ELSE 0 END) AS age_22_24 " +
                "FROM student";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                int totalStudents = rs.getInt("age_18_20") + rs.getInt("age_20_22") + rs.getInt("age_22_24");

                System.out.println("Statistics by Age:");
                System.out.printf("18-20: %.2f%%\n", (rs.getInt("age_18_20") * 100.0 / totalStudents));
                System.out.printf("20-22: %.2f%%\n", (rs.getInt("age_20_22") * 100.0 / totalStudents));
                System.out.printf("22-24: %.2f%%\n", (rs.getInt("age_22_24") * 100.0 / totalStudents));
            }
        }
    }

    // Method to export students to a CSV file
    public void exportToCSV(String filePath) throws SQLException {
        String query = "SELECT * FROM student";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             FileWriter writer = new FileWriter(filePath)) {

            writer.append("ID,First Name,Last Name,Age,Grade\n");

            while (rs.next()) {
                writer.append(String.format("%d,%s,%s,%d,%s\n",
                        rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getInt("age"), rs.getString("grade")));
            }

            System.out.println("Successfully exported data to " + filePath);
        } catch (IOException e) {
            System.out.println("Error writing to the CSV file.");
            e.printStackTrace();
        }
    }

    // Method to import students from a CSV file
    public void importFromCSV(String filePath) {
        String query = "INSERT INTO student (first_name, last_name, age, grade) VALUES (?, ?, ?, ?)";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PreparedStatement statement = connection.prepareStatement(query)) {

            String line;
            reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                statement.setString(1, data[1]); // first_name
                statement.setString(2, data[2]); // last_name
                statement.setInt(3, Integer.parseInt(data[3])); // age
                statement.setString(4, data[4]); // grade
                statement.executeUpdate();
            }

            System.out.println("Successfully imported data from " + filePath);
        } catch (IOException | SQLException e) {
            System.out.println("Error importing from the CSV file.");
            e.printStackTrace();
        }
    }

    public void CalculateAverageGrade() {
        String query = "SELECT grade FROM student";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            int total = 0;
            int count = 0;
            while (rs.next()) {
                total += rs.getInt("grade");
                count++;
            }
            System.out.println("Average grade: " + (total / count));
        } catch (SQLException e) {
            System.out.println("Error calculating average grade.");
            e.printStackTrace();
        }
    }
}
