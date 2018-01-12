import Database.DBBackup;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class BackupRestoreDB extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if(request.getParameter("backup")!=null){

            String status= DBBackup.mysqlDatabaseBackUp();
            if(status.equals("y")){

                HttpSession session=request.getSession(true);
                session.setAttribute("currentSessionUser", "");
                response.sendRedirect("backupsuccess.jsp");
            }else if(status.equals("n")){

                response.sendRedirect("backupfailure.jsp");
            }


        }   else if(request.getParameter("restore")!=null){

            String fileName=request.getParameter("file");

            boolean status=DBBackup. mysqlDatabaseRestore("dbName","dbUserName","dbPassword","C:\\folderName\\"+fileName);
            if(status==true){
                System.out.println("restore success ");
                HttpSession session=request.getSession(true);
                session.setAttribute("currentSessionUser", "");
                response.sendRedirect("restoresuccess.jsp");
            }else{
                System.out.println("restore failure ");
                response.sendRedirect("restorefailure.jsp");
            }

        }
    }
}