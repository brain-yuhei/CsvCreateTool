<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイルアップロード</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/csvUpload.css">
    <script src="${pageContext.request.contextPath}/js/modal.js"></script>
</head>

<body>

    <h2>CSVファイルアップロード画面</h2>

    <form>
        <label>CSVファイルを選択</label>
        <input>
        <button>アップロード</button>
    </form>

</body>
</html>