import java.sql.*;

public class Db {
    private static final String URL = "jdbc:mysql://localhost:3306/Ecole";
    private static final String USER = "root";
    private static final String PASSWORD = "Mounir-1992";

    private final Connection connection;
    private static String sortMethod = "id";

    public Db() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Driver not found");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void addStudent(String firstName, String lastName, int age, String grade) throws SQLException {
        String query = "INSERT INTO student (first_name, last_name, age, grade, MoyeneeDeNotes) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setString(4, grade);
            statement.executeUpdate();

            updateAverageGrade();
        }
    }

    public void updateStudent(int id, String firstName, String lastName, int age, String grade) throws SQLException {
        String query = "UPDATE student SET first_name = ?, last_name = ?, age = ?, grade = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setString(4, grade);
            statement.setInt(5, id);
            statement.executeUpdate();

            updateAverageGrade();
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String query = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

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

    public void searchStudentById(int id) throws SQLException {
        String query = "SELECT * FROM student WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
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

    public void setSortMethod(String sortMethod) {
        this.sortMethod = sortMethod;
    }

    public void updateAverageGrade() throws SQLException {
        String updateQuery = "UPDATE student SET average_grade = (SELECT AVG(CAST(grade AS DECIMAL(3,2))) FROM student)";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            updateStmt.executeUpdate();
        }
    }

    public void advancedSearchByMoyeneeDeNotes(int interval) throws SQLException {
        String query;
        switch (interval) {
            case 1:
                query = "SELECT * FROM student WHERE average_grade >= 0 AND average_grade <= 20";
                break;
            case 2:
                query = "SELECT * FROM student WHERE average_grade > 20 AND average_grade <= 40";
                break;
            case 3:
                query = "SELECT * FROM student WHERE average_grade > 40 AND average_grade <= 60";
                break;
            case 4:
                query = "SELECT * FROM student WHERE average_grade > 60 AND average_grade <= 80";
                break;
            case 5:
                query = "SELECT * FROM student WHERE average_grade > 80 AND average_grade <= 100";
                break;
            default:
                throw new IllegalArgumentException("Interval not supported: " + interval);
        }

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Students in average grade interval " + interval + ":");
            while (rs.next()) {
                System.out.printf("ID: %d, First Name: %s, Last Name: %s, Age: %d, Grade: %s, Average Grade: %.2f%n",
                        rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getInt("age"), rs.getString("grade"), rs.getDouble("average_grade"));
            }
        }
    }

    public void calculateStatisticsByGrade() throws SQLException {
        String query = "SELECT " +
                "SUM(CASE WHEN MoyeneeDeNotes BETWEEN 0 AND 20 THEN 1 ELSE 0 END) AS range_0_20, " +
                "SUM(CASE WHEN MoyeneeDeNotes BETWEEN 20 AND 40 THEN 1 ELSE 0 END) AS range_20_40, " +
                "SUM(CASE WHEN MoyeneeDeNotes BETWEEN 40 AND 60 THEN 1 ELSE 0 END) AS range_40_60, " +
                "SUM(CASE WHEN MoyeneeDeNotes BETWEEN 60 AND 80 THEN 1 ELSE 0 END) AS range_60_80, " +
                "SUM(CASE WHEN MoyeneeDeNotes BETWEEN 80 AND 100 THEN 1 ELSE 0 END) AS range_80_100 " +
                "FROM student";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                int totalStudents = rs.getInt("range_0_20") + rs.getInt("range_20_40") +
                        rs.getInt("range_40_60") + rs.getInt("range_60_80") +
                        rs.getInt("range_80_100");

                System.out.println("Statistics by Grade:");
                System.out.printf("0-20: %.2f%%\n", (rs.getInt("range_0_20") * 100.0 / totalStudents));
                System.out.printf("20-40: %.2f%%\n", (rs.getInt("range_20_40") * 100.0 / totalStudents));
                System.out.printf("40-60: %.2f%%\n", (rs.getInt("range_40_60") * 100.0 / totalStudents));
                System.out.printf("60-80: %.2f%%\n", (rs.getInt("range_60_80") * 100.0 / totalStudents));
                System.out.printf("80-100: %.2f%%\n", (rs.getInt("range_80_100") * 100.0 / totalStudents));
            }
        }
    }

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

}
