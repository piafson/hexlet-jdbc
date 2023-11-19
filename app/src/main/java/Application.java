import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Application {
    public static void main(String[] args) throws SQLException {
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {

            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            var sql2 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, "Tommy");
                preparedStatement.setString(2, "33333333");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Maria");
                preparedStatement.setString(2, "44444444");
                preparedStatement.executeUpdate();

                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }
            }

            var sqlDel = "DELETE FROM users WHERE id = (?)";
            try (var statement2 = conn.prepareStatement(sqlDel)) {
                statement2.setInt(1, 1);
                statement2.executeUpdate();
            }


            var sql3 = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql3);
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }
        }
    }
}
