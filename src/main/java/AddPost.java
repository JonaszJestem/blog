import Database.DBUserConnector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AddPost", urlPatterns = "/add")
public class AddPost extends HttpServlet {
    CallableStatement statement = null;
    String postID;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession(false);

        if (session == null || session.getAttribute("login") == null) {
            response.sendRedirect("/login");
            return;
        }
        String role = (String) session.getAttribute("role");
        if(!role.equalsIgnoreCase("editor") && !role.equalsIgnoreCase("admin")) {
            response.sendRedirect("/login");
            return;
        }

        RequestDispatcher requestDispatcher = request
                .getRequestDispatcher("/add_post.jsp");
        requestDispatcher.forward(request, response);
    }

    String author, title, text, category;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        System.out.println(session);
        System.out.println(session.getAttribute("login"));
        if (session == null || session.getAttribute("login") == null) {
            resp.sendRedirect("/login");
        } else {
            author = (String) session.getAttribute("login");
            title = req.getParameter("title");
            text = req.getParameter("content");
            category = req.getParameter("category");

            System.out.println("Author: " + author + " postID:" + postID + " title:" + title + " text:" + text + " cat:" + category);

            DBUserConnector dbConnector = DBUserConnector.INSTANCE;
            Connection connection = dbConnector.getConnection();
            resp.setContentType("text/html");

            try {
                String query = "{call add_post(?,?,?,?,?)}";
                statement = connection.prepareCall(query);
                statement.setString(1,title);
                statement.setString(2, category);
                statement.setString(3, author);
                statement.setString(4, text);
                statement.registerOutParameter(5, Types.VARCHAR);
                statement.execute();

                String status = statement.getString(5);
                if(status.equalsIgnoreCase("done")) {
                    resp.sendRedirect("/");
                } else {
                    resp.sendRedirect("/add");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}