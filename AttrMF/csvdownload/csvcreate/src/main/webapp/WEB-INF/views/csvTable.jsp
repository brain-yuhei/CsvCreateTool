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
    <script src="${pageContext.request.contextPath}/js/modal-data.js"></script>
    <script src="${pageContext.request.contextPath}/js/export.js"></script>
    <script src="${pageContext.request.contextPath}/js/fetch.js"></script>
    <script src="${pageContext.request.contextPath}/js/formatting.js"></script>
    <script src="${pageContext.request.contextPath}/js/row-editor.js"></script>
    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
    <script src="${pageContext.request.contextPath}/js/checkbox.js"></script>
</head>
<body>

<!-- 成功・エラーメッセージ -->
<c:if test="${saveSuccess}">
    <div id="saveSuccessMessage" class="flash-message">データを一時保存しました。</div>
</c:if>
<c:if test="${not empty message}">
    <div class="message">${message}</div>
</c:if>
<c:if test="${not empty errormessage}">
    <div class="error-message">${errormessage}</div>
</c:if>

<!-- ボタンエリア -->
<div class="check_button">
    <button id="checkOutputBtn" type="button" onclick="openModal()">出力内容確認</button>
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
                    <th>
                        <input type="checkbox" id="checkAll" onclick="selectAllCheckboxes(this)" />
                    </th>
                    <th>曜日</th>
                    <th>日付</th>
                    <th>支払先・内容</th>
                    <th>経費科目</th>
                    <th>金額</th>
                    <th>メモ</th>
                    <th>費用負担部門名</th>
                    <th>部門コード</th>
                    <th>経路追加</th>
                </tr>
            </thead>
            <tbody id="mainTableBody">
                <c:set var="rowIndex" value="0" />
                <c:forEach var="info" items="${dateInfoList}">
                    <c:forEach var="item" items="${wrkList}">
                        <c:if test="${item.date == info.date}">
                            <tr id="section-${info.date}">
                                <td>
                                    <input type="checkbox" name="koutsuuhiList[${rowIndex}].checked" value="true" onchange="updateRowBackground(this)"
                                        <c:if test="${item.checked}">checked</c:if> />
                                    <input type="hidden" name="_koutsuuhiList[${rowIndex}].checked" value="off" />
                                </td>
                                <td>${info.dayOfWeek}</td>
                                <td data-type="date">${info.date}
                                    <input type="hidden" name="koutsuuhiList[${rowIndex}].date" value="${info.date}" />
                                </td>
                                <td>
                                    <select name="koutsuuhiList[${rowIndex}].payee" onchange="onPayeeChange(this, '${rowIndex}')">
                                        <c:forEach var="payee" items="${selectedPayees}">
                                            <option value="${payee}" <c:if test="${payee == item.payee}">selected</c:if>>${payee}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <input type="text" name="koutsuuhiList[${rowIndex}].expenseCategory"
                                           value="${item.expenseCategory}"
                                           onblur="validateExpenseCategory(this, '${rowIndex}')" />
                                    <div id="error-expenseCategory-${rowIndex}" class="error-message" style="color:red; display:none;"></div>
                                </td>
                                <td>
                                    <input type="text" name="koutsuuhiList[${rowIndex}].amount"
                                           value="${item.amount}"
                                           onblur="formatAmount(this); validateAmount(this, '${rowIndex}')" />
                                    <div id="error-amount-${rowIndex}" class="error-message" style="color:red; display:none;"></div>
                                </td>
                                <td>
                                    <input type="text" name="koutsuuhiList[${rowIndex}].memo"
                                           value="${item.memo}"
                                           onblur="validateMemo(this, '${rowIndex}')" />
                                    <div id="error-memo-${rowIndex}" class="error-message" style="color:red; display:none;"></div>
                                </td>
                                <td>${item.departmentName}
                                    <input type="hidden" name="koutsuuhiList[${rowIndex}].departmentName" value="${item.departmentName}" />
                                </td>
                                <td>${item.departmentCode}
                                    <input type="hidden" name="koutsuuhiList[${rowIndex}].departmentCode" value="${item.departmentCode}" />
                                </td>
                                <td><button type="button" onclick="addRowToDate('${info.date}')">＋</button></td>
                            </tr>
                            <c:set var="rowIndex" value="${rowIndex + 1}" />
                        </c:if>
                    </c:forEach>
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
