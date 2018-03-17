
import javafx.util.Pair;
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
import java.util.List;


public class Home extends HttpServlet {

    static Logger log = Logger.getLogger(Home.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null)
            resp.sendRedirect(req.getContextPath() + "/login");
        else {
            printTasks(req, (String) req.getSession().getAttribute("loggedInUser"), 2);
            req.getRequestDispatcher("/home.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        if (req.getParameter("submitTask") != null) {
            log.info("Add task button pressed.");
            submitTask(req, resp);
        } else if (req.getParameter("setDone") != null) {
            log.info("Done button pressed.");
            if (setDoneOrDelete(req, 0) == false)
                log.warn("Task is NOT setDone!");
        } else if (req.getParameter("delete") != null) {
            log.info("Delete button pressed.");
            if (setDoneOrDelete(req, 1) == false)
                log.warn("Task is NOT deleted!");
        }
        printTasks(req, (String) req.getSession().getAttribute("loggedInUser"), 2);
        req.getRequestDispatcher("/home.jsp").forward(req, resp);
    }

    private void submitTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Alexander/IdeaProjects/javacore_project/src/db");
            String task = req.getParameter("task");
            String userName = (String) req.getSession().getAttribute("loggedInUser");
            Queries.addTask(conn, userName, task);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printTasks(HttpServletRequest req, String userName, int taskType) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Alexander/IdeaProjects/javacore_project/src/db");
            ArrayList<TaskObject> list = Queries.getUserTasks(conn, userName, taskType);
            req.setAttribute("list", list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 0 - setDoneUnDone
     * 1 - SetUnDone
     * 2 - delete
     *
     * @param req
     * @param action
     */
    private boolean setDoneOrDelete(HttpServletRequest req, int action) {
        try {
            String obj = req.getParameter("hiddenValue");
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Alexander/IdeaProjects/javacore_project/src/db");
            if (action == 0)
                if (Queries.isTaskDone(conn, Integer.valueOf(obj)))
                    return Queries.setTaskDoneUnDone(conn, Integer.valueOf(obj), 0);
                else return Queries.setTaskDoneUnDone(conn, Integer.valueOf(obj), 1);

            else if (action == 1)
                return Queries.deleteTask(conn, Integer.valueOf(obj));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

