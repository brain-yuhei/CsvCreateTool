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

    <!-- タイトル：中央固定 -->
    <div class="page_header">
        <h2>CSV編集メニュー</h2>
    </div>

    <!-- 右上固定メニュー -->
    <div class="top_right">
        
        <a href="/csvUpload" class="nav-button">
            <i class="fas fa-file-upload"></i> CSVファイルの登録画面へ
        </a>

        <form id="filterForm" action="/selectPayee" method="post">
            <label for="selectedPayee">選択中の経路：</label>
            <select name="selectedPayee" id="selectedPayee">
                <option value="">-- 選択してください --</option>
                <c:forEach var="payee" items="${selectedPayees}">
                    <option value="${payee}" ${payee == selectedPayee ? 'selected' : ''}>${payee}</option>
                </c:forEach>
            </select>
            <input type="hidden" name="selectedMonth" value="${currentMonth}" required />
        </form>
    </div>

    <c:if test="${not empty message}">
        <div class="error-message">${message}</div>
    </c:if>

    <!-- 中央の年月選択とボタン -->
    <div class="center_form">

        <form id="monthForm" action="/currentMonth" method="get">
            <label for="calendar_Text">年月選択:</label>
            <input type="month" id="calendar_Text" name="selectedMonth" value="${currentMonth}"
                   min="${minMonth}" max="${maxMonth}" required
                   onchange="document.getElementById('monthForm').submit();">
        </form>

        <div class="action_buttons">
            <form action="/selectPayee" method="post">
                <input type="hidden" name="selectedMonth" value="${currentMonth}" />
                <input type="hidden" name="selectedPayee" value="${selectedPayee}" />
                <button type="submit" name="actionType" value="history">履歴を表示</button>
                <button type="submit" name="actionType" value="create">新規作成</button>
            </form>
        </div>

    </div>

</body>
</html>


