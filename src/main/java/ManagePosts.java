import Database.DBAdminConnector;

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

@WebServlet(name = "ManagePosts", urlPatterns = "/manage")
public class ManagePosts extends HttpServlet {
    CallableStatement statement;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("login") == null) {
            resp.sendRedirect("/login");
        } else {
            DBAdminConnector dbConnector = DBAdminConnector.INSTANCE;
            Connection connection = dbConnector.getConnection();
            resp.setContentType("text/html");
            try {
                String query = "{call get_posts_by_author(?)}";
                statement = connection.prepareCall(query);
                statement.setString(1, (String) session.getAttribute("login"));
                ResultSet posts = statement.executeQuery();

                List<Post> postList = new ArrayList<>();
                while (posts.next()) {
                    int id = posts.getInt("id");
                    String title = posts.getString("title");
                    int views = posts.getInt("views");
                    Post p = new Post(id, title, views);
                    System.out.println(p);
                    postList.add(p);
                }
                req.setAttribute("posts", postList);
                RequestDispatcher view = req.getRequestDispatcher("manage.jsp");
                view.forward(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBAdminConnector dbConnector = DBAdminConnector.INSTANCE;
        Connection connection = dbConnector.getConnection();

        HttpSession session = req.getSession();

        String action = req.getParameter("remove");
        if(action != null) {
            try {
                String query = "{call remove_post(?,?,?)}";
                statement = connection.prepareCall(query);
                statement.setString(1, action);
                statement.setString(2, (String)session.getAttribute("login"));
                statement.registerOutParameter(3, Types.VARCHAR);
                statement.execute();

                String status = statement.getString(3);

                RequestDispatcher view = req.getRequestDispatcher("manage.jsp");
                view.forward(req, resp);

                System.out.println(status);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (req.getParameter("edit") != null) {

        }
    }
}
