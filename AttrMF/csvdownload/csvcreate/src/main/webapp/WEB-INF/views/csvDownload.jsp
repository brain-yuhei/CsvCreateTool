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
        <input type="month" id="calendar_Text" name="selectedMonth" value="${currentMonth}" required
               onchange="document.getElementById('monthForm').submit();">
    </form>

    <form id="filterForm" action="/selectPayee" method="post">
        <label for="selectedPayee">経路選択:</label>
        <select name="selectedPayee" id="selectedPayee" onchange="document.getElementById('filterForm').submit();">
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

<div class="check_button">
    <button type="button" onclick="openModal()">ダウンロード内容確認</button>
</div>   

<form action="/SelectedKeiro" method="post">
    <table border="1">
        <thead>
        <tr>
            <th><input type="checkbox" id="selectAll" onclick="selectAllCheckboxes(this)"></th>
            <th>曜日</th>
            <th>日付</th>
            <th>支払先・内容</th>
            <th>経費科目</th>
            <th>金額</th>
            <th>メモ</th>
            <th>費用負担部名</th>
            <th>費用負担部コード</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="date" items="${dateList}" varStatus="status">
            <tr>
                <td><input type="checkbox" name="selectedDates" value="${date}" checked></td>
                <td>${dayOfWeekList[status.index]}</td>
                <td>${date}</td>

                <c:set var="item" value="${wrkList[status.index]}" />

                <td><input type="text" name="payeeList[${status.index}].payee" value="${item.payee}"></td>
                <td><input type="text" name="payeeList[${status.index}].expenseCategory" value="${item.expenseCategory}"></td>
                <td><input type="text" name="payeeList[${status.index}].amount" value="${item.amount}"></td>
                <td><input type="text" name="payeeList[${status.index}].memo" value="${item.memo}"></td>
                <td><input type="text" name="payeeList[${status.index}].departmentName" value="${item.departmentName}"></td>
                <td><input type="text" name="payeeList[${status.index}].departmentCode" value="${item.departmentCode}"></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</form>

    <!-- ダウンロード内容確認画面 -->
    <div id="outputModal">
        <h3>出力内容確認</h3>
        <table border="1">
            <thead>
                <tr>
                    <th>日付</th>
                    <th>支払先・内容</th>
                    <th>経費科目</th>
                    <th>金額</th>
                    <th></th><th></th><th></th><th></th>
                    <th>メモ</th>
                    <th>費用負担部名</th>
                    <th>費用負担部コード</th>
                    <th></th><th></th><th></th><th></th><th></th><th></th><th></th>
                </tr>
            </thead>
            <tbody id="modalTableBody">
                <!-- JavaScriptで挿入 -->
            </tbody>
        </table>

        <br/>
        <button onclick="downloadCSV()">出力</button>
        <button onclick="closeModal()">閉じる</button>
    </div>

    <!-- モーダル画面のオーバーレイ -->
    <div id="modalOverlay"></div>
</body>
</html>