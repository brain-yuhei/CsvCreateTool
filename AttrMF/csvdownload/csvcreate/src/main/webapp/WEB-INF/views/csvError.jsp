<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>不正操作エラー画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/csvError.css">   
</head>

<body>
    <div class="error-box">
        <p>${errorMessage}</p>
        <a href="/">トップページへ戻る</a>
    </div>
</body>
</html>