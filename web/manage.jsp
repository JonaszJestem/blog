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

<c:forEach items="${posts}" var="post">
    <p>
    <form action="/manage" method="post">
        <button type="submit" name="remove" value="${post.id}">Remove</button>
        <button type="submit" name="edit" value="${post.id}">Edit</button>
            <c:out value="${post.title}"/>
        <a href="/post/${post.id}">Read more</a>
    </form>
    </p>
</c:forEach>
</body>
</html>
