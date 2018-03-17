import org.apache.log4j.Logger;

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

    static Logger log = Logger.getLogger(Login.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
        log.info("Register page loaded.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        if (req.getParameter("registerButton") != null) {
            log.info("Submit button pressed");
            register(req, resp);
        } else if (req.getParameter("loginButton") != null) {
            log.info("Login button pressed");
            redirectToLogin(req, resp);
        }
    }

    private void register(HttpServletRequest req, HttpServletResponse resp) {
        String user = req.getParameter("userName");
        String pass = req.getParameter("password");

        PreparedStatement ins = null;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(Constants.dbPath);) {


            /**
             * Handle user mistakes.
             */
            int response = Queries.instertLoginPassword(conn, user, pass);
            if (response == 4) {
                req.setAttribute("errorReport", "Username should not be empty. Please try again.");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
            } else if (response == 3) {
                req.setAttribute("errorReport", "Password must be at least 5 characters long. Please try again.");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
            } else if (response == 2) {
                req.setAttribute("errorReport", "This name is already taken. Please try a different one.");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
            } else if (response == 1) {
                log.info("Account created.");
                req.setAttribute("errorReport", "Account created.");
                resp.sendRedirect(req.getContextPath() + "/login");
            } else if (response == 0) {
                req.setAttribute("errorReport", "Error occurred. Please try again.");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
            }

            conn.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private void redirectToLogin(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
