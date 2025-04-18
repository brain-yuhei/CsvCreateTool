<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイルアップロード</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Modal.css">
    <script src="${pageContext.request.contextPath}/js/modal.js"></script>
</head>

<body>

    <h2>CSVファイルアップロード画面</h2>

    <form action="/upload" method="post" enctype="multipart/form-data">
        <!-- CSVファイルのアップロード -->
        <label for="file">CSVファイルを選択:</label>
        <input type="file" id="file" name="file" required>
        <input type="hidden" name="selectedMonth" value="${currentTime}">
        <button type="submit">アップロード</button>
    </form>

</body>
</html>