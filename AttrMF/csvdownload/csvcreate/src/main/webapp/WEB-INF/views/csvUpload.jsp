<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイルアップロード</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/csvUpload.css">
</head>

<body>

    <div class="page_header">
        <h2>CSVファイルインポート画面</h2>
    </div>

    <c:if test="${not empty message}">
        <div class="message">${message}</div>
    </c:if>

    <c:if test="${not empty errormessage}">
        <div class="error-message">${errormessage}</div>
    </c:if>

    <form method="post" action="/csvUpload" enctype="multipart/form-data">
        <label for="uploadfile">CSVファイルを選択</label>
        <input type="file" id="uploadfile" name="uploadfile" accept=".csv">
        <button type="submit">インポート</button>
        <button type="button" onclick="location.href='/'">戻る</button>
    </form>

</body>
</html>