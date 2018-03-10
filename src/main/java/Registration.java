import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Registration extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        if (req.getParameter("registerButton") != null)
            register(req, resp);
        else if (req.getParameter("loginButton") != null)
            redirectToLogin(req, resp);


    }

    private void register(HttpServletRequest req, HttpServletResponse resp) {
        String user = req.getParameter("userName");
        String pass = req.getParameter("password");

        Connection conn = null;
        PreparedStatement ins = null;
        try (PrintWriter pw = resp.getWriter()) {

            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Alexander/IdeaProjects/javacore_project/src/dbdb");

            /**
             * Handle user mistakes.
             */
            int response = Queries.instertLoginPassword(conn, user, pass);
            if (response == 3)
                pw.append("Password must be at least 5 characters long. Please try again.");
            else if (response == 2)
                pw.append("This name is already taken. Please try a different one.");
            else if (response == 1) {
                pw.append("Account created");
                resp.sendRedirect(req.getContextPath() + "/login");
            } else if (response == 0)
                pw.append("Error occurred. Please try again.");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectToLogin(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
