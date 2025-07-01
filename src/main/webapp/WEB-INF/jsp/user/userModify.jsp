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
<h2>회원수정</h2>

<div>
    <form name="frm" method="post">
        이름:<input type="text" name="name" value="${user.name}">
        이메일:<input type="text" name="email" value="${user.email}">
    </form>
    <button type="button" data-btn="btnModify">수정</button>
</div>

<script>
    $(function () {
        $("button[data-btn='btnModify']").on("click", function () {
            $.ajax({
                url: "/user/${user.id}",
                type: "PUT",
                data: $("form").serialize(),
                dataType: "json",
                success: function (response) {
                    console.log(response.data)
                    alert(response.message)
                    location.href = "/user/" + response.data.id;
                },
                error: function(xhr, status, error) {
                    alert(xhr.responseJSON.message);
                    $(xhr.responseJSON.errors || []).each(function (i, error) {
                        alert(error.errorField + ": " + error.errorMessage);
                    });
                }
            })
        })
    })
</script>

</body>
</html>
