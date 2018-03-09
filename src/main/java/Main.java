import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class Main extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (PrintWriter w = resp.getWriter()) {

            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Alexander/IdeaProjects/javacore_project/src/dbdb");
            PreparedStatement st = conn.prepareStatement("SELECT * FROM names");
            ResultSet rs = st.executeQuery();
            while (rs.next())
                w.append(rs.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
