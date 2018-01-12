<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>$Title$</title>
  </head>
  <body>
    <h1>Hello</h1>
    <ul>
      <li><a href="/register">Register</a></li>
      <li><a href="/login">Login</a></li>
      <li><a href="/panel">Panel</a> </li>
      <li><a href="/logout">Logout</a> </li>
    </ul>
    <c:forEach items="${posts}" var="post">
      <p>
      <h4><c:out value="${post.title} ${post.author}"/><br /></h4>
        <c:out value="${post.text}"/>
      </p>
    </c:forEach>

    <h4>Comments</h4>

    <c:forEach items="${comments}" var="comment">
        <p>
        <h4><c:out value="${comment.author}"/>: </h4>
        <c:out value="${comment.text}"/>
        </p>
    </c:forEach>

  </body>
</html>
