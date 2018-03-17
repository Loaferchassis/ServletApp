<%--
  Created by IntelliJ IDEA.
  User: Alexander
  Date: 09.03.2018
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Registration</title>
  </head>
  <body>
    <form method="post" action="register">
        Enter username :<input type="text" name="userName" /><br/><br/>
        Enter password :<input type="password" name="password" /><br/><br/>
        <input type="submit" value="Submit" name="registerButton" />
        <input type="submit" value="LogIn" name="loginButton" />
    </form>
    <p>${requestScope.get("errorReport")}</p>
  </body>
</html>
