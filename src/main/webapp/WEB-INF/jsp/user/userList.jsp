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
            <li data-id="${user.id}">${user.username}:${user.name}:${user.email}</li>
        </c:forEach>
    </ul>
</div>

<button type="button" data-btn="btnCreate">등록</button>

<script>
    $(function () {
        $("li").click(function () {
            location.href = "/user/" + $(this).data("id");
        });

        $('[data-btn="btnCreate"]').click(function () {
            location.href = "/user/create";
        })

    });
</script>
</body>
</html>
