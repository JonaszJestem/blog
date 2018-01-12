import Database.DBAdminConnector;
import Database.DBUserConnector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LoginPage", urlPatterns = "/login")
public class LoginPage extends HttpServlet {
    CallableStatement statement = null;
    String login, password, status;
    RequestDispatcher view;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("login.jsp");
        view.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        login = req.getParameter("login");
        password = req.getParameter("password");

        System.out.println(login + " " + password);
        DBAdminConnector dbConnector = DBAdminConnector.INSTANCE;
        Connection connection = dbConnector.getConnection();
        resp.setContentType("text/html");

        try {
            String query = "{call login(?,?,?)}";
            statement = connection.prepareCall(query);
            statement.setString(1, login);
            statement.setString(2, password);
            statement.registerOutParameter(3, Types.VARCHAR);
            statement.execute();

            status = statement.getString(3);

            System.out.println(status);
            if(status.startsWith("logged")) {
                HttpSession session=req.getSession();
                session.setAttribute("login", login);
                session.setAttribute("role", status.split(" ")[1]);
                resp.sendRedirect("/");
            }
            else {
                view = req.getRequestDispatcher("login.jsp");
                view.forward(req,resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
