<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login</title>

    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>

</head>
<body>
<h2>회원목록</h2>

<div>
    <ul>
        <c:forEach items="${users.content}" var="user">
            <li>${user.username}:${user.password}:${user.name}:${user.email}</li>
        </c:forEach>
    </ul>
</div>

</body>
</html>
