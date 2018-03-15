<%--
  Created by IntelliJ IDEA.
  User: Alexander
  Date: 05.03.2018
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Login form</title>
</head>
<body>
<form method="post" action="login">
    Username :<input type="text" name="user"/><br/><br/>
    Password :<input type="password" name="pass"/><br/><br/>
    <input type="submit" value="Login" name="loginButton" />
    <input type="submit" value="Register" name="registerButton"/>
</form>
<p>${requestScope.get("errorReport")}</p>

</body>

</html>

