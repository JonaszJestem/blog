import Database.DBAdminConnector;
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

@WebServlet(name = "ManagePosts", urlPatterns = "/manage")
public class ManagePosts extends HttpServlet {
    CallableStatement statement;
    List<Post> postList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        DBUserConnector dbConnector = DBUserConnector.INSTANCE;
        Connection connection = dbConnector.getConnection();
        postList.clear();

        if(role.equalsIgnoreCase("admin")) {
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT id,title,views FROM posts ORDER BY time_created DESC LIMIT 100";
                ResultSet posts = statement.executeQuery(query);

                getPosts(posts);



            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if (role.equalsIgnoreCase("editor")){
            try {
                String query = "{call get_posts_by_author(?)}";
                statement = connection.prepareCall(query);
                statement.setString(1, (String) session.getAttribute("login"));
                ResultSet posts = statement.executeQuery();

                getPosts(posts);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        req.setAttribute("posts", postList);

        RequestDispatcher view = req.getRequestDispatcher("manage.jsp");
        view.forward(req, resp);
    }

    private void getPosts(ResultSet posts) throws SQLException {
        while (posts.next()) {
            int id = posts.getInt("id");
            String title = posts.getString("title");
            int views = posts.getInt("views");
            Post p = new Post(id, title, views);
            postList.add(p);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBUserConnector dbConnector = DBUserConnector.INSTANCE;
        Connection connection = dbConnector.getConnection();

        HttpSession session = req.getSession(false);

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

                resp.sendRedirect("/manage");

                System.out.println(status);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        action = req.getParameter("edit");
        if (action != null) {
            resp.sendRedirect("/edit/"+action);
        }
    }
}
