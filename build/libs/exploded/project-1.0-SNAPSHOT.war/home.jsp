<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head><title>HUI</title></head>
<body>
${pageContext.request.userPrincipal.name}
<form method="post" action="addTask">
    <label for="task">Enter a new task: </label>
    <input type="text" id="task" name="task">
    <input type="submit" value="Submit" name="submitTask"/>
</form>

<c:forEach items="${requestScope.list}" var="elem">
- <c:out value="${elem}"/><p>
    </c:forEach>
</body>
</html>