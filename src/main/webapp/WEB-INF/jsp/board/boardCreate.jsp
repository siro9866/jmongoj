<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login</title>

    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>

</head>
<body>
<h2>게시판 등록</h2>

<div>
    <form name="frm">
        <sec:csrfInput />
        제목:<input type="text" name="title">
        내용:<input type="text" name="content">
        <!-- 업로드할 파일 -->
        <input type="file" name="attachments" />
    </form>
    <button type="button" data-btn="btnCreate">등록</button>
</div>

<script>
    $(function () {
        $("button[data-btn='btnCreate']").on("click", function () {

            const formData = new FormData(document.querySelector("form[name=frm]")); // 파일 포함된 전체 form 데이터
            // 불필요한 빈 파일 제거
            for (const [key, value] of formData.entries()) {
                if (value instanceof File && value.size === 0) {
                    formData.delete(key);
                }
            }

            $.ajax({
                url: "/board",
                type: "POST",
                data: formData,
                processData: false,      // 중요! jQuery가 데이터 처리하지 않도록
                contentType: false,      // 중요! 자동으로 multipart/form-data로 처리
                dataType: "json",
                success: function (response) {
                    console.log(response.data);
                    alert(response.message);
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
    })
</script>

</body>
</html>
