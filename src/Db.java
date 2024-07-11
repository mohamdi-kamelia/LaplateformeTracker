import java.sql.*;

public class Db {
    private static final String URL = "jdbc:mysql://localhost:3306/studentDB";
    private static final String USER = "root";
    private static final String PASSWORD = "patesaup0ulet";

    private Connection connection;

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

    public void addStudent(String firstName, String lastName, int age, double grade) throws SQLException {
        String query = "INSERT INTO student (first_name, last_name, age, grade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setDouble(4, grade);
            statement.executeUpdate();
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
        String query = "SELECT * FROM student";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " " +
                        resultSet.getString("first_name") + " " +
                        resultSet.getString("last_name") + " " +
                        resultSet.getInt("age") + " " +
                        resultSet.getDouble("grade"));
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

}
