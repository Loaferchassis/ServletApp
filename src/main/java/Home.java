
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;


public class Home extends HttpServlet {

    static Logger log = Logger.getLogger(Home.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null)
            resp.sendRedirect(req.getContextPath() + "/login");
        else {
            printTasks(req, (String) req.getSession().getAttribute("loggedInUser"));
            req.getRequestDispatcher("/home.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        if (req.getParameter("submitTask") != null) {
            log.info("Login button pressed.");
            submitTask(req, resp);
        }
    }

    private void submitTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Alexander/IdeaProjects/javacore_project/src/db");
            String task = req.getParameter("task");
            String userName = (String) req.getSession().getAttribute("loggedInUser");
            Queries.addTask(conn, userName, task);
            printTasks(req, userName);
            req.getRequestDispatcher("/home.jsp").forward(req, resp);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printTasks(HttpServletRequest req, String userName) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Alexander/IdeaProjects/javacore_project/src/db");
            ArrayList<String> list = Queries.getUserTasks(conn, userName, 0);
            req.setAttribute("list", list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
