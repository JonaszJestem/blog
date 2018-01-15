import Database.DBAdminConnector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "BackupRestoreDB", urlPatterns = "/backup")
public class BackupRestoreDB extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession(false);

        if (session == null || session.getAttribute("login") == null) {
            resp.sendRedirect("/login");
            return;
        }
        String role = (String) session.getAttribute("role");
        if(!role.equalsIgnoreCase("admin")) {
            resp.sendRedirect("/login");
            return;
        }

        RequestDispatcher view = req.getRequestDispatcher("backupdb.jsp");
        view.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session=request.getSession(false);

        if (session == null || session.getAttribute("login") == null) {
            response.sendRedirect("/login");
            return;
        }
        String role = (String) session.getAttribute("role");
        if(!role.equalsIgnoreCase("admin")) {
            response.sendRedirect("/login");
            return;
        }

        System.out.println(request.getParameter("backup"));
        System.out.println(request.getParameter("restore"));

        if(request.getParameter("backup") != null){
            String status = DBAdminConnector.INSTANCE.backup();
            if(status.equals("y")){
                System.out.println("backup successful");
                response.sendRedirect("/panel");
            }else if(status.equals("n")){
                System.out.println("backup failed");
                response.sendRedirect("/backup");
            }
        }
        else if(request.getParameter("restore")!=null){

            String fileName=request.getParameter("file");
            System.out.println(fileName);
            boolean status=DBAdminConnector.INSTANCE.mysqlDatabaseRestore("C:\\backup\\"+fileName);
            if(status==true){
                System.out.println("restore success ");
                response.sendRedirect("/panel");
            }else{
                System.out.println("restore failure ");
                response.sendRedirect("/panel");
            }
        } else {
            response.sendRedirect("/backup");
        }
    }
}