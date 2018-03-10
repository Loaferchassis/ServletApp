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


    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        if (request.getParameter("loginButton") != null)
            login(request, response);
        else if (request.getParameter("registerButton") != null)
            redirectToRegister(request, response);
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {
        String user = req.getParameter("user");
        String pass = req.getParameter("pass");

        try (PrintWriter pw = resp.getWriter()) {

            Class.forName("org.sqlite.JDBC");

            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Alexander/IdeaProjects/javacore_project/src/dbdb");

            if (Queries.checkPassword(conn, user, pass)) {
                req.getSession().setAttribute("loggedInUser", user);
                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                pw.append("The password is incorrect. Please try again.");
                req.getSession().setAttribute("loggedInUser", null);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectToRegister(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.sendRedirect(req.getContextPath() + "/register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
