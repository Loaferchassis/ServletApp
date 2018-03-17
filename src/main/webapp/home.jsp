<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head><title>Home</title></head>
<body>
${pageContext.request.userPrincipal.name}
<form method="post" action="addTask">
    <label for="task">Enter a new task: </label>
    <input type="text" id="task" name="task">
    <input type="submit" value="Submit" name="submitTask"/>
</form>

<form method="post" action="check" name="checkboxes">
    <c:forEach items="${requestScope.list}" var="elem">
        <c:if test="${elem.is_done == 0}">
            <input type="radio" name="elem" value=${elem.id}>${elem.task}<br>
        </c:if>
        <c:if test="${elem.is_done == 1}">
            <input type="radio" name="elem" value=${elem.id}><strike>${elem.task}</strike><br>
        </c:if>
    </c:forEach>
    <input type="hidden" name="hiddenValue"/>
    <input type="submit" value="Done" name="setDone" onclick="getDoneList()">
    <input type="submit" value="Delete" name="delete" onclick="getDoneList()">
</form>

<script>
        function getDoneList(){
            var elems = document.getElementsByName("elem");
            var ret;
            var i=0;
            while(elems[i]!=null){
                if(elems[i].checked == true)
                    ret = elems[i].value;
                i++;
            }
            document.forms.checkboxes.hiddenValue.value = ret;

        }
</script>
</body>
</html>