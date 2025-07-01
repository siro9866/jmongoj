<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<spring:message code="confirm.delete" var="CONFIRM_DELETE" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 공통 -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Login</title>

    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>

</head>
<body>
<h2>게시판 상세</h2>

<div>
    <ul>
        <li>${board.title}</li>
        <li>${board.content}</li>
    </ul>

    <button type="button" data-btn="btnList">목록</button>
    <button type="button" data-btn="btnModify">수정</button>
    <button type="button" data-btn="btnDelete">삭제</button>
</div>

<script>
    $(function () {
        $('[data-btn="btnList"]').click(function () {
            location.href = "/board";
        });
        $('[data-btn="btnModify"]').click(function () {
            location.href = "/board/${board.id}/modify";
        });
        $('[data-btn="btnDelete"]').click(function () {

            const confirmed = confirm("${CONFIRM_DELETE}");
            if (!confirmed) {
                return;
            }
            const token = $('meta[name="_csrf"]').attr('content');
            const header = $('meta[name="_csrf_header"]').attr('content');

            $.ajax({
                url: "/board/${board.id}",
                type: "DELETE",
                dataType: "json",
                beforeSend: function(xhr) {
                    xhr.setRequestHeader(header, token); // ✅ CSRF 토큰 헤더에 추가
                },
                success: function (response) {
                    alert(response.message);            // "성공적으로 생성되었습니다"
                    location.href = "/board";
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
