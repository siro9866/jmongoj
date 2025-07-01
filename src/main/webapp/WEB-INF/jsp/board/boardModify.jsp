<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<spring:message code="confirm.modify" var="CONFIRM_MODIFY" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login</title>

    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>

</head>
<body>
<h2>게시판 수정</h2>

<div>
    <form name="frm">
        <sec:csrfInput />
        제목:<input type="text" name="title" value="${board.title}">
        내용:<input type="text" name="content" value="${board.content}">
        <!-- 업로드할 파일 -->
        <input type="file" name="files" />
    </form>
    <button type="button" data-btn="btnModify">수정</button>
</div>

<script>
    $(function () {
        $("button[data-btn='btnModify']").on("click", function () {

            const confirmed = confirm("${CONFIRM_MODIFY}");
            if (!confirmed) {
                return;
            }

            const formData = new FormData(document.querySelector("form[name=frm]")); // 파일 포함된 전체 form 데이터

            $.ajax({
                url: "/board/${board.id}",
                type: "PUT",
                data: formData,
                processData: false,      // 중요! jQuery가 데이터 처리하지 않도록
                contentType: false,      // 중요! 자동으로 multipart/form-data로 처리
                dataType: "json",
                success: function (response) {
                    console.log(response.data)
                    alert(response.message)
                    location.href = "/board/" + response.data.id;
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
