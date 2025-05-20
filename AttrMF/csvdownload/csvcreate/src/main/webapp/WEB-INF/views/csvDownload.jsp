<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイル簡易作成ツール</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/csvDownload.css">
    <script src="${pageContext.request.contextPath}/js/modal.js"></script>    
</head>
<body>

<h2>CSV編集画面</h2>

<div class="upload_button">
    <a href="/csvUpload">
        <button type="button">CSVファイル登録</button>
    </a>
</div>

<div class="form_group">
    <form id="monthForm" action="/currentMonth" method="get">
        <label for="calendar_Text">年月選択:</label>
        <input type="month" id="calendar_Text" name="selectedMonth" value="${currentMonth}" min="${minMonth}" max="${maxMonth}" required
               onchange="document.getElementById('monthForm').submit();">
    </form>

    <form id="filterForm" action="/selectPayee" method="post">
        <label for="selectedPayee">経路選択:</label>
        <select name="selectedPayee" id="selectedPayee">
            <option value="">-- 選択してください --</option>
            <c:forEach var="payee" items="${selectedPayees}">
                <option value="${payee}" ${payee == selectedPayee ? 'selected' : ''}>${payee}</option>
            </c:forEach>
        </select>
        <input type="hidden" name="selectedMonth" value="${currentMonth}" required />
        <button type="button" onclick="submitPayeeForm()">CSV管理表生成画面へ</button>
    </form>
    
</div>

<c:if test="${not empty message}">
    <div class="error-message">${message}</div>
</c:if>
 

</body>
</html>

