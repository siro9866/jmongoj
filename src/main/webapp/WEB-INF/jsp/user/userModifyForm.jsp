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
                success: function (data) {
                    location.href = "/user/${user.id}";
                },
                error: function(xhr, status, error) {
                    try {
                        const response = JSON.parse(xhr.responseText);
                        alert("에러 메시지: " + response.message);

                        // 예: 개별 필드 에러를 콘솔에 출력
                        if (response.errors) {
                            for (const field in response.errors) {
                                console.log(`${field}: ${response.errors[field]}`);
                            }
                        }

                    } catch (e) {
                        console.error("JSON 파싱 에러:", e);
                        alert("예상치 못한 에러가 발생했습니다.");
                    }
                }
            })
        })
    })
</script>

</body>
</html>
