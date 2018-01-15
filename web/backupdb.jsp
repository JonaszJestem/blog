<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>$Title$</title>
</head>
<body>
    <form action="/backup" method="post">
        <button type="submit" name="backup" value="backup">Backup</button><br /><br />

        <input type="file" name="file" >
        <button type="submit" name="restore" value="restore">Restore</button>
    </form>
</body>
</html>