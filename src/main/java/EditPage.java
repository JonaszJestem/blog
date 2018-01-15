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

@WebServlet(name = "EditPage", urlPatterns = "/edit/*")
public class EditPage extends HttpServlet {
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

        DBUserConnector dbConnector = DBUserConnector.INSTANCE;
        Connection connection = dbConnector.getConnection();
        response.setContentType("text/html");
        System.out.println(request.getPathInfo());
        if (request.getPathInfo().replaceAll("/", "").matches("-?\\d+")) {
            postID = request.getPathInfo().replaceAll("/", "");
        }
        try {
            String query = "{call get_post(?)}";
            statement = connection.prepareCall(query);
            statement.setString(1, postID);
            ResultSet posts = statement.executeQuery();

            if(posts.next()) {
                int id = posts.getInt("id");
                String title = posts.getString("title");
                String author = posts.getString("nickname");
                Date date = posts.getDate("time_created");
                String text = posts.getString("text");
                Post p = new Post(id, title, author, date, text);

                request.setAttribute("post", p);
            }
            RequestDispatcher requestDispatcher = request
                    .getRequestDispatcher("/post_form.jsp");
            requestDispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String author, title, text, category;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("login") == null) {
            resp.sendRedirect("/login");
            return;
        }
        String role = (String) session.getAttribute("role");
        if(!role.equalsIgnoreCase("editor") && !role.equalsIgnoreCase("admin")) {
            resp.sendRedirect("/login");
            return;
        }

        author = (String) session.getAttribute("login");
        title = req.getParameter("title");
        text = req.getParameter("content");
        category = req.getParameter("category");

        System.out.println("Author: " + author + " postID:" + postID + " title:" + title + " text:" + text + " cat:" + category);

        DBUserConnector dbConnector = DBUserConnector.INSTANCE;
        Connection connection = dbConnector.getConnection();
        resp.setContentType("text/html");

        try {
            String query = "{call edit_post(?,?,?,?,?,?)}";
            statement = connection.prepareCall(query);
            statement.setString(1, author);
            statement.setInt(2, Integer.parseInt(postID));
            statement.setString(3, title);
            statement.setString(4, category);
            statement.setString(5, text);
            statement.registerOutParameter(6, Types.VARCHAR);
            statement.execute();

            String status = statement.getString(6);

            System.out.println(status);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}