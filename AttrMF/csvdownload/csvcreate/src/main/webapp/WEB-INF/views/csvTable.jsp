<!-- JSPファイル (csvTable.jsp) -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイル管理画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/csvTable.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css">
    <script src="${pageContext.request.contextPath}/js/modal.js"></script>
    <script src="${pageContext.request.contextPath}/js/error-message.js"></script>
    <script src="${pageContext.request.contextPath}/js/row-add.js"></script> <!-- 新規追加 -->
</head>
<body>

<!-- 成功・エラーメッセージ -->
<c:if test="${saveSuccess}">
    <script>alert("データを一時保存しました。");</script>
</c:if>
<c:if test="${not empty message}">
    <div class="message">${message}</div>
</c:if>
<c:if test="${not empty errormessage}">
    <div class="error-message">${errormessage}</div>
</c:if>

<!-- ボタンエリア -->
<div class="check_button">
    <button id="checkOutputBtn" onclick="openModal()">出力内容確認</button>
    <button type="button" onclick="location.href='/'">戻る</button>
</div>

<!-- フォーム -->
<form id="csvForm" action="/saveWorkTable" method="post">
    <input type="hidden" name="selectedMonth" value="${currentMonth}" />
    <input type="hidden" name="selectedPayee" value="${selectedPayee}" />
    <button type="submit">一時保存</button>
    <div class="scrollable-table-container">
        <table class="fixed-header-table" border="1">
            <thead>
                <tr>
                    <th>選択</th>
                    <th>曜日</th>
                    <th>日付</th>
                    <th>支払先・内容</th>
                    <th>経費科目</th>
                    <th>金額</th>
                    <th>メモ</th>
                    <th>費用負担部門名</th>
                    <th>費用負担部門コード</th>
                    <th>行追加</th>
                </tr>
            </thead>
            <tbody id="mainTableBody">
                <c:forEach var="info" items="${dateInfoList}" varStatus="status">
                    <c:set var="item" value="${wrkList[status.index]}" />
            
                    <tr data-date="${info.date}" id="section-${info.date}">
                        <td>
                            <input type="checkbox" name="koutsuuhiList[${status.index}].checked" value="true"
                            <c:if test="${info.checked}">checked</c:if> />
                            <input type="hidden" name="_koutsuuhiList[${status.index}].checked" value="off" />
                        </td>
                        <td>${info.dayOfWeek}</td>
                        <td data-type="date">${info.date}
                            <input type="hidden" name="koutsuuhiList[${status.index}].date" value="${info.date}" />
                        </td>
                        <td>
                            <select name="koutsuuhiList[${status.index}].payee" onchange="onPayeeChange(this, '${status.index}')">
                                <c:forEach var="payee" items="${selectedPayees}">
                                    <option value="${payee}" <c:if test="${fn:trim(payee) == fn:trim(item.payee)}">selected</c:if>>${payee}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td><input type="text" name="koutsuuhiList[${status.index}].expenseCategory" value="${item.expenseCategory}" onblur="validateExpenseCategory(this, '${status.index}')" /></td>
                        <td><input type="text" name="koutsuuhiList[${status.index}].amount" value="${item.amount}" onblur="formatAmount(this); validateAmount(this, '${status.index}')" /></td>
                        <td><input type="text" name="koutsuuhiList[${status.index}].memo" value="${item.memo}" onblur="validateMemo(this, '${status.index}')" /></td>
                        <td>${item.departmentName}<input type="hidden" name="koutsuuhiList[${status.index}].departmentName" value="${item.departmentName}" /></td>
                        <td>${item.departmentCode}<input type="hidden" name="koutsuuhiList[${status.index}].departmentCode" value="${item.departmentCode}" /></td>
                        <td><button type="button" onclick="addRowToDate('${info.date}')">＋</button></td>
                    </tr>
                </c:forEach>
            </tbody>
            
        </table>
    </div>
</form>

<!-- モーダル -->
<div id="modalOverlay">
    <div id="outputModal">
        <h3>出力内容確認画面</h3>
        <button onclick="downloadCSV()">CSVファイルを出力</button>
        <button onclick="closeModal()">閉じる</button>
        <div class="modal-table">
            <table class="modal-header-table" border="1">
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
                <tbody id="modalTableBody"></tbody>
            </table>
        </div>
        <div id="emptyFieldWarning"></div>
        <br />
    </div>
</div>

</body>
</html>