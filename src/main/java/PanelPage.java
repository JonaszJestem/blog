import Database.DBAdminConnector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "PanelPage", urlPatterns = "/panel")
public class PanelPage extends HttpServlet {
    RequestDispatcher view;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession(false);
        if(session == null || session.getAttribute("login") == null) {
            resp.sendRedirect("/login");
        }
        else {
            req.setAttribute("login", session.getAttribute("login"));
            req.setAttribute("role", session.getAttribute("role"));
            view = req.getRequestDispatcher("panel.jsp");
            view.forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession(false);

        if (req.getParameter("manage") != null) {
            System.out.println((String)session.getAttribute("role"));
            System.out.println(((String)session.getAttribute("role")).equalsIgnoreCase("admin"));
            if(((String)session.getAttribute("role")).equalsIgnoreCase("editor") || ((String)session.getAttribute("role")).equalsIgnoreCase("admin")) {
                resp.sendRedirect("/manage");
            }
        } else if (((String)session.getAttribute("role")).equalsIgnoreCase("admin")) {
           if(req.getParameter("backup") != null) {
               DBAdminConnector.INSTANCE.backup();
           }
           else if(req.getParameter("restore") != null) {
               resp.sendRedirect("/restore");
           }
        }
    }
}
