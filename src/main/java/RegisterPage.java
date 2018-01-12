import Database.DBAdminConnector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RegisterPage", urlPatterns = "/register")
public class RegisterPage extends HttpServlet {
    CallableStatement statement;
    String login, password, email, status;
    RequestDispatcher view;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("register.jsp");
        view.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        login = req.getParameter("login");
        password = req.getParameter("password");
        email = req.getParameter("email");

        System.out.println(login + " " + password + " " + email);
        DBAdminConnector dbConnector = DBAdminConnector.INSTANCE;
        Connection connection = dbConnector.getConnection();
        resp.setContentType("text/html");
        try {
            String query = "{call add_user(?,?,?,?)}";
            statement = connection.prepareCall(query);
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.registerOutParameter(4, Types.VARCHAR);
            statement.execute();

            status = statement.getString(4);

            System.out.println(status);
            if(status.equalsIgnoreCase("added")) {
                resp.sendRedirect("/login");
            }
            else {
                view = req.getRequestDispatcher("register.jsp");
                view.forward(req,resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
