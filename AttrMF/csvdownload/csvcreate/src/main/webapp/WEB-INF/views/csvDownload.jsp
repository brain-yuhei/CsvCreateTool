<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイル簡易作成ツール</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/csvDownload.css">
    <script src="${pageContext.request.contextPath}/js/validation.js"></script>    
</head>
<body>

    <!-- タイトル：中央固定 -->
    <div class="page_header">
        <h2>CSV編集メニュー</h2>
    </div>

    <!-- 右上固定メニューとフォーム -->
    <form id="payeeForm" action="/selectPayee" method="post" method="get">
        <div class="top_right">
            <a href="/csvUpload" class="nav-button">
                <i class="fas fa-file-upload"></i> CSVファイルの登録画面へ
            </a>

            <label for="selectedPayee">選択中の経路：</label>
            <select name="selectedPayee" id="selectedPayee">
                <option value="">-- 選択してください --</option>
                <c:forEach var="payee" items="${selectedPayees}">
                    <option value="${payee}" ${payee == selectedPayee ? 'selected' : ''}>${payee}</option>
                </c:forEach>
            </select>
        </div>

        <c:if test="${not empty message}">
            <div class="message">${message}</div>
        </c:if>

        <c:if test="${not empty errormessage}">
            <div class="error-message">${errormessage}</div>
        </c:if>

        <!-- 中央の年月選択とボタン -->
        <div class="center_form">
            <label for="calendar_Text">年月選択:</label>
            <input type="month" id="calendar_Text" name="selectedMonth" value="${currentMonth}"
                   min="${minMonth}" max="${maxMonth}" required />

                <div class="action_buttons">
                    <button type="submit" name="actionType" value="history" onclick="removeRequired()">履歴を表示</button>
                    <button type="submit" name="actionType" value="create" onclick="addRequired()">新規作成</button>
                </div>
        </div>
    </form>

</body>
</html>



