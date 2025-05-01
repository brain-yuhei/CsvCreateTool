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

<!-- 年月・経路選択 + 作成ボタンのフォーム -->
<form action="/generateWorkTable" method="post">
    <label for="selectedMonth">年月選択:</label>
    <input type="month" id="selectedMonth" name="selectedMonth" value="${currentMonth}" required>

    <label for="selectedPayee">経路選択:</label>
    <select id="selectedPayee" name="selectedPayee" required>
        <option value="">-- 選択してください --</option>
        <c:forEach var="payee" items="${selectedPayees}">
            <option value="${payee}" ${payee == selectedPayee ? 'selected' : ''}>${payee}</option>
        </c:forEach>
    </select>

    <button type="submit">作成</button>
</form>

<!--メッセージ-->
<c:if test="${not empty message}">
    <div class="error-message">${message}</div>
</c:if>


<!-- モーダル表示 -->
<form action="/confirmSelection" method="post" id="confirmForm">
    <c:forEach var="info" items="${dateInfoList}" varStatus="status">
        <c:if test="${info.checked}">
            <input type="hidden" name="selectedDates" value="${info.date}" />
        </c:if>
    </c:forEach>
    <button type="submit">モーダル簡易確認</button>
</form>
 


<!-- CSV管理表 -->
<form action="/saveWorkTable" method="get">
    <input type="hidden" name="selectedPayee" value="${selectedPayee}" />
    <input type="hidden" name="selectedMonth" value="${selectedMonth}" />
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
                <th>費用負担部門名</th>
                <th>費用負担部門コード</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="info" items="${dateInfoList}" varStatus="status">
                <tr>
                    <td>
                        <input type="checkbox" name="selectedDates" value="${info.date}"
                            <c:if test="${info.checked}">checked</c:if> >
                    </td>
                    <td>${info.dayOfWeek}</td>
                    <td>
                        ${info.date}
                        <input type="hidden" name="koutsuuhiList[${status.index}].date" value="${info.date}" />
                    </td>

                    <c:set var="item" value="${wrkList[status.index]}" />
                    <td><input type="text" name="koutsuuhiList[${status.index}].payeeContent" value="${item.payee}" /></td>
                    <td><input type="text" name="koutsuuhiList[${status.index}].expense_category" value="${item.expenseCategory}" /></td>
                    <td><input type="text" name="koutsuuhiList[${status.index}].amountInclusiveTax" value="${item.amount}" /></td>
                    <td><input type="text" name="koutsuuhiList[${status.index}].memo" value="${item.memo}" /></td>
                    <td><input type="text" name="koutsuuhiList[${status.index}].department_name" value="${item.departmentName}" readonly /></td>
                    <td><input type="text" name="koutsuuhiList[${status.index}].department_code" value="${item.departmentCode}" readonly /></td>
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
                <th>メモ</th>
                <th>費用負担部門名</th>
                <th>費用負担部門コード</th>
            </tr>
        </thead>
        <tbody id="modalTableBody">
            <c:forEach var="item" items="${modalDataList}">
                <tr>
                    <td>${item.date}</td>
                    <td>${item.payeeContent}</td>
                    <td>${item.expense_category}</td>
                    <td>${item.amountInclusiveTax}</td>
                    <td>${item.memo}</td>
                    <td>${item.department_name}</td>
                    <td>${item.department_code}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <br/>
    <button onclick="downloadCSV()">出力</button>
    <button onclick="closeModal()">閉じる</button>
</div>

<!-- モーダル画面のオーバーレイ--> 
<div id="modalOverlay"></div>

</body>
</html>

