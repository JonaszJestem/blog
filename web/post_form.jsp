<%--
  Created by IntelliJ IDEA.
  User: Jonasz
  Date: 2018-01-15
  Time: 12:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/edit/${post.id}" method="post">
    <p>
        <input type="text" name="title" value="${post.title}">
    </p>
    <p>
        <textarea name="content" rows="10" cols="30"> ${post.text} </textarea>
    </p>
    <p>
        <select name="category">
            <option value="bezpieczenstwo">bezpieczenstwo</option>
            <option value="programowanie">programowanie</option>
            <option value="zakupy">zakupy</option>
            <option value="technologiczne nowosci">technologiczne nowosci</option>
            <option value="inne">inne</option>
        </select>
    </p>
    <p>
        <input type="submit" value="edit">
    </p>
</form>
</body>
</html>
