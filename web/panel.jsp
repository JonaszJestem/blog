<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>$Title$</title>
  </head>
  <body>

  <ul>
    <li><a href="/register">Register</a></li>
    <li><a href="/login">Login</a></li>
    <li><a href="/panel">Panel</a> </li>
    <li><a href="/logout">Logout</a> </li>
  </ul>

  <h1>Hello <c:out value="${login}"/></h1>
  <h1><c:out value="${role}"/></h1>

  <h2>Available actions: </h2>
  <c:choose>
    <c:when test="${role == 'admin'}">

      <form action="/panel" method="post">
        <button type="submit" name="manage" value="manage">Manage posts</button>
        <button type="submit" name="backup" value="backup">Backup db</button>
        <button type="submit" name="restore" value="restore">Restore db</button>
      </form>


    </c:when>

    <c:when test="${role == 'editor'}">
  <form action="/panel" method="post">
    <button type="submit" name="manage" value="manage">Manage posts</button>
  </form>
    </c:when>

    <c:otherwise>
      <p>No actions available</p>
    </c:otherwise>
  </c:choose>


  </body>
</html>
