<%--
  Created by IntelliJ IDEA.
  User: Jonasz
  Date: 2018-01-12
  Time: 20:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
    Document   : backupdb
    Created on : Oct 30, 2015, 2:55:19 PM
    Author     : Aravind Sankaran Nair
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
</head>
<body>
<form method="get" action="<%=request.getContextPath()%>/BackupRestoreDataBase" enctype="multipart/form-data">
    <center>
        <br><br><br><br><br><br><br><br><br><br><br><br>
        <table>
            <tr>
                <td>Backup</td>
                <td></td>
                <td><input type="submit" name="backup" value="Backup" /></td>
            </tr>
            <tr>
                <td>Restore</td>
                <td><input type="file" name="file" ></td>
                <td><input type="submit" name="restore" value="Restore" /></td>
            </tr>
        </table>

    </center>
</form>
</body>
</html>