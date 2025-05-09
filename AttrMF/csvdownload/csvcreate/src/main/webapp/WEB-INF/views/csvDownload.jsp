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
        <button type="button" onclick="submitPayeeForm()">CSV管理表生成</button>
    </form>
    
</div>

<c:if test="${not empty message}">
    <div class="error-message">${message}</div>
</c:if>

<div class="check_button">
    <button id="checkOutputBtn" onclick="openModal()">出力内容確認</button>
</div>   

<!-- CSV管理表 保存ボタン付きフォーム -->
<form id="csvForm" action="/saveWorkTable" method="post">
    <!--<input type="submit" value="一時保存" />-->
    <div class="scrollable-table-container">
        <table class="fixed-header-table" border="1">
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
                        <td data-type="date">
                            ${info.date}
                            <input type="hidden" name="koutsuuhiList[${status.index}].date" value="${info.date}" />
                        </td>

                        <c:set var="item" value="${wrkList[status.index]}" />
                        <td>
                            <select name="koutsuuhiList[${status.index}].payeeContent">
                                <c:forEach var="payee" items="${selectedPayees}">
                                    <option value="${payee}" <c:if test="${payee == item.payeeContent}">selected</c:if>>${payee}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td><input type="text" name="koutsuuhiList[${status.index}].expense_category" value="${item.expense_category}"></td>
                        <td><input type="text" name="koutsuuhiList[${status.index}].amountInclusiveTax" value="${item.amountInclusiveTax}"></td>
                        <td><input type="text" name="koutsuuhiList[${status.index}].memo" value="${item.memo}"></td>
                        <td><input type="text" name="koutsuuhiList[${status.index}].department_name" value="${item.department_name}" readonly></td>
                        <td><input type="text" name="koutsuuhiList[${status.index}].department_code" value="${item.department_code}" readonly></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</form>

<!-- ダウンロード内容確認画面 -->
<div id="modalOverlay">
    <div id="outputModal">
      <h3>出力内容確認</h3>
  
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
          <tbody id="modalTableBody">
            <!-- JavaScriptで挿入 -->
          </tbody>
        </table>
      </div>
  
      <br />
      <button onclick="downloadCSV()">出力</button>
      <button onclick="closeModal()">閉じる</button>
    </div>
  </div>

</body>
</html>

