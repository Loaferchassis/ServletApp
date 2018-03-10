import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Queries {

    public static ResultSet selectAll(Connection connection, String tableName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM login_info");
        return statement.executeQuery();
    }

    public static boolean checkPassword(Connection connection, String login, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT password FROM login_info WHERE login IS ?");
        statement.setString(1, login);
        if (password.equals(statement.executeQuery().getString("password")))
            return true;
        else return false;
    }

    /**
     * Returns:
     * 0 - Error
     * 1 - Ok
     * 2 - Name taken
     * 3 - Password too small
     *
     * @param connection
     * @param login
     * @param password
     * @return
     * @throws SQLException
     */
    public static int instertLoginPassword(Connection connection, String login, String password) throws SQLException {
        /**
         * Check password length
         */

        if (password.length() <= 5)
            return 3;

        /**
         * Check if the name is already taken.
         */

        ResultSet rs = selectAll(connection, "longin_info");

        while (rs.next()) {
            if (login.equals(rs.getString("login")))
                return 2;
        }

        PreparedStatement statement = connection.prepareStatement("INSERT INTO login_info VALUES (?,?)");
        statement.setString(1, login);
        statement.setString(2, password);
        if (statement.executeUpdate() == 0)
            return 0;
        else return 1;
    }
}
