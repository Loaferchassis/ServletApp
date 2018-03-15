import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Queries {

    public static ResultSet selectAll(Connection connection, String tableName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM login_info");
        return statement.executeQuery();
    }

    /**
     * 0 - Incorrect password
     * 1 - Ok
     * 2 - No users with this login
     *
     * @param connection
     * @param login
     * @param password
     * @return
     * @throws SQLException
     */
    public static int checkPassword(Connection connection, String login, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT password FROM login_info WHERE login IS ?");
        statement.setString(1, login);
        ResultSet rs = statement.executeQuery();
        if (!rs.next())
            return 2;
        else if (password.equals(rs.getString("password")))
            return 1;
        else return 0;
    }

    /**
     * Returns:
     * 0 - Error
     * 1 - Ok
     * 2 - Name taken
     * 3 - Password too small
     * 4 - Username is empty
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

        if (login.isEmpty())
            return 4;

        if (password.length() < 5)
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

    /**
     * taskType parameter:
     * 0 - only unfinished tasks
     * 1 - only finished tasks
     * 2 - all tasks
     *
     * @param connection
     * @param user
     * @param taskType
     * @return
     */
    public static ArrayList<String> getUserTasks(Connection connection, String user, int taskType) throws SQLException {
        ArrayList<String> returnList = new ArrayList<>();
        PreparedStatement statement;

        if (taskType == 2) {
            statement = connection.prepareStatement("SELECT task FROM tasks WHERE user = ?");
        } else {
            statement = connection.prepareStatement("SELECT task FROM tasks WHERE user = ? AND is_done= ?");
            statement.setInt(2, taskType);
        }
        statement.setString(1, user);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            returnList.add(rs.getString("task"));
        }
        return returnList;

    }

    public static boolean addTask(Connection connection, String user, String task) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO tasks(user,task,is_done) VALUES (?,?,0)");
        statement.setString(1, user);
        statement.setString(2, task);
        if (statement.executeUpdate() == 0)
            return false;
        else return true;
    }

    public static boolean deleteTask(Connection connection, Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM tasks WHERE id=?");
        if (statement.executeUpdate() == 0)
            return false;
        else return true;

    }

    public static boolean setTaskDone(Connection connection, Integer id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE tasks SET is_done=1 WHERE id=?");
        statement.setInt(1, id);
        if (statement.executeUpdate() == 0)
            return false;
        else return true;
    }
}
