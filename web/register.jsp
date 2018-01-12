<%--
  Created by IntelliJ IDEA.
  User: Jonasz
  Date: 2018-01-12
  Time: 15:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
</head>
<body>
    <form action="/register" method="post">
        Name:<input type="text" name="login"><br>
        Password:<input type="password" name="password"><br>
        Email:<input type="email" name="email"><br>
        <input type="submit" value="register">
    </form>
</body>
</html>
