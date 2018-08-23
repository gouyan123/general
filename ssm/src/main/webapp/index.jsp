<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/test/register.do" method="POST">
        name:<input type="text" name="name"/></br></br>
        age :<input type="text" name="age"/></br></br>
        <input type="submit" value="submit"/>
    </form>
</body>
</html>
