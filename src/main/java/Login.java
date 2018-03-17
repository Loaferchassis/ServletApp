import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import sun.rmi.runtime.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;

public class Login extends HttpServlet {
    static {
        PropertyConfigurator.configure(Login.class.getClassLoader().getResource("log4j.properties"));
    }

    static Logger log = Logger.getLogger(Login.class.getName());

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
        log.info("Login page loaded.");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        if (request.getParameter("loginButton") != null) {
            log.info("Login button pressed.");
            login(request, response);
        } else if (request.getParameter("registerButton") != null) {
            log.info("Register button pressed.");
            redirectToRegister(request, response);
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        log.info("Log in method called.");
        String user = req.getParameter("user");
        String pass = req.getParameter("pass");

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try (Connection conn = DriverManager.getConnection(Constants.dbPath)) {

            int response = Queries.checkPassword(conn, user, pass);
            if (response == 1) {
                log.info("Successful Log in.");
                req.getSession().setAttribute("loggedInUser", user);
                log.info("Redirecting to Home page.");
                resp.sendRedirect(req.getContextPath() + "/");
            } else if (response == 0) {
                log.warn("The password is incorrect.");
                req.setAttribute("errorReport", "The password is incorrect. Please try again.");
                req.getSession().setAttribute("loggedInUser", null);
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            } else {
                log.warn("There is no user with such Username.");
                req.setAttribute("errorReport", "There is no user with such Username. Please try again.");
                req.getSession().setAttribute("loggedInUser", null);
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
            conn.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void redirectToRegister(HttpServletRequest req, HttpServletResponse resp) {
        log.info("Redirecting to Register page.");
        try {
            resp.sendRedirect(req.getContextPath() + "/register");
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
