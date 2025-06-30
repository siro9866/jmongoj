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
<h2>회원등록</h2>

<div>
    <form name="frm" method="post">
        아이디:<input type="text" name="username">
        비밀번호:<input type="text" name="password">
        이름:<input type="text" name="name">
        이메일:<input type="text" name="email">
    </form>
    <button type="button" data-btn="btnCreate">등록</button>
</div>

<script>
    $(function () {
        $("button[data-btn='btnCreate']").on("click", function () {
            $.ajax({
                url: "/user/create",
                type: "POST",
                data: $("form").serialize(),
                success: function (data) {
                    alert(data);
                    location.href = "/user";
                }
            })
        })
    })
</script>

</body>
</html>
