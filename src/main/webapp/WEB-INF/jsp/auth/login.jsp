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
<h2>Login</h2>
<form name="frm" method="post" action="/auth/login">
    <label>Username: <input type="text" name="username"/></label><br/>
    <label>Password: <input type="password" name="password"/></label><br/>
    <button type="button" id="loginBtn">Login</button>
</form>

<script>
    $("#loginBtn").on('click',function(){
        $.ajax({
            type: 'POST',
            url: '/auth/login',
            data: $("form[name=frm]").serialize(),
            dataType: 'json'
        }).done(function(response){
            console.log("성공:", response.message);
            window.location.href = response.redirectUrl;
        }).fail(function(xhr, status, error){
            try{
                console.log("실패:", xhr.status);
                alert(xhr.responseJSON.message);
            } catch (e) {
                console.log("실패")
            }

        })
    })

    // Enter 키 로그인
    $("form[name=frm]").on("keydown", function(event) {
        if (event.key === "Enter") {
            event.preventDefault(); // 기본 submit 방지
            $("#loginBtn").click(); // 버튼 클릭 트리거
        }
    });
</script>

</body>
</html>
