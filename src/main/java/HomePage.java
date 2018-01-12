import Database.DBAdminConnector;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HomePage", urlPatterns = "")
public class HomePage extends HttpServlet {
    Statement statement = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBAdminConnector dbConnector = DBAdminConnector.INSTANCE;
        Connection connection = dbConnector.getConnection();
        resp.setContentType("text/html");
        try {
            statement = connection.createStatement();
            String getPosts = "SELECT * FROM latest LIMIT 10";
            ResultSet posts = statement.executeQuery(getPosts);

            List<Post> postList = new ArrayList<>();
            while(posts.next()) {
                int id = posts.getInt("id");
                String title = posts.getString("title");
                String author = posts.getString("nickname");
                Date date = posts.getDate("time_created");
                String text = posts.getString("text");
                Post p = new Post(id, title, author, date, text);
                //System.out.println(p);
                postList.add(p);
            }
            req.setAttribute("posts", postList);
            RequestDispatcher view = req.getRequestDispatcher("index.jsp");
            view.forward(req,resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
