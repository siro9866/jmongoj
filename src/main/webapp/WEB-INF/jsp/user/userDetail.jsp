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
<h2>회원상세</h2>

<div>
    <ul>
        <li>${user.username}</li>
        <li>${user.name}</li>
        <li>${user.email}</li>
    </ul>

    <button type="button" data-btn="btnList">목록</button>
    <button type="button" data-btn="btnModify">수정</button>
    <button type="button" data-btn="btnDelete">삭제</button>
</div>

<script>
    $(function () {
        $('[data-btn="btnList"]').click(function () {
            location.href = "/user";
        });
        $('[data-btn="btnModify"]').click(function () {
            location.href = "/user/${user.id}/modify";
        });
        $('[data-btn="btnDelete"]').click(function () {
            $.ajax({
                url: "/user/${user.id}",
                type: "DELETE",
                dataType: "json",
                success: function (response) {
                    alert(response.message);            // "성공적으로 생성되었습니다"
                    location.href = "/user";
                },
                error: function(xhr, status, error) {
                    alert(xhr.responseJSON.message);
                    $(xhr.responseJSON.errors || []).each(function (i, error) {
                        alert(error.errorField + ": " + error.errorMessage);
                    });
                }
            })
        })
    });
</script>
</body>
</html>
