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

@WebServlet(name = "PostPage", urlPatterns = "/post/*")
public class PostPage extends HttpServlet {
    CallableStatement statement = null;
    String postID;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBUserConnector dbConnector = DBUserConnector.INSTANCE;
        Connection connection = dbConnector.getConnection();
        response.setContentType("text/html");
        System.out.println(request.getPathInfo());
        if(request.getPathInfo().replaceAll("/", "").matches("-?\\d+")){
            postID = request.getPathInfo().replaceAll("/", "");
            try {
                Integer.parseInt(postID);
            } catch (NumberFormatException ex) {
                response.sendRedirect("/");
                return;
            }
        }
        try {
            String query = "{call get_post(?)}";
            statement = connection.prepareCall(query);
            statement.setString(1, postID);
            ResultSet posts = statement.executeQuery();

            List<Post> postList = new ArrayList<>();
            while(posts.next()) {
                int id = posts.getInt("id");
                String title = posts.getString("title");
                String author = posts.getString("nickname");
                Date date = posts.getDate("time_created");
                String text = posts.getString("text");
                Post p = new Post(id, title, author, date, text);
                System.out.println(p);
                postList.add(p);
            }

            if(postList.size() == 0) {
                response.sendRedirect("/");
                return;
            }

            request.setAttribute("posts", postList);

            query = "{call get_comments_to_post(?)}";
            statement = connection.prepareCall(query);
            statement.setString(1, postID);
            ResultSet comments = statement.executeQuery();

            List<Comment> commentsList = new ArrayList<>();
            while(comments.next()) {
                String author = comments.getString("nickname");
                String text = comments.getString("text");
                Date time = comments.getDate("timestamp");
                Comment c = new Comment(author,text,time);
                System.out.println(c);
                commentsList.add(c);
            }
            request.setAttribute("comments", commentsList);


            RequestDispatcher requestDispatcher = request
                    .getRequestDispatcher("/post.jsp");
            requestDispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String author, comment;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        author = (String)session.getAttribute("login");
        comment = req.getParameter("comment");

        System.out.println(author + " " + postID + " " + comment);

        if(session == null || comment == null) return;
        if(author == null) resp.sendRedirect("/login");

        DBUserConnector dbConnector = DBUserConnector.INSTANCE;
        Connection connection = dbConnector.getConnection();
        resp.setContentType("text/html");

        try {
            String query = "{call add_comment(?,?,?,?)}";
            statement = connection.prepareCall(query);
            statement.setInt(1, Integer.parseInt(postID));
            statement.setString(2, comment);
            statement.setString(3, author);
            statement.registerOutParameter(4, Types.VARCHAR);
            statement.execute();

            String status = statement.getString(4);

            System.out.println(status);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

