<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイル簡易作成ツール</title> 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/csvHistory.css"> 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css">
    <script src="${pageContext.request.contextPath}/js/modal.js"defer></script>
    <script src="${pageContext.request.contextPath}/js/modal-data.js"defer></script>
</head>
<body>

    <div class="page_header">
        <h2>過去出力内容一覧</h2>
        <a href="/">前の画面に戻る</a> 
    </div>

    <!--表示する履歴の年月選択-->
    <!--ワークテーブルから年月を取得しプルダウン選択できる-->
    <form method="get" action="/csvHistory">
        <div class="top_right">
            <label for="selectedMonth">表示年月：</label>
            <select name="selectedMonth" id="selectedMonth"required>
                <option value="">-- 選択してください --</option>
                <c:forEach var="month" items="${historyMonth}">
                    <option value="${month}" <c:if test="${month == selectedMonth}">selected</c:if>>${month}</option>      
                </c:forEach>
            </select>
            <button type="submit">表示</button>
        </div>
    </form>
    
    <button id="checkOutputBtn" type="button" onclick="openModal()">出力内容確認</button>

    <!--編集不可のCSVデータ表を表示-->
    <div class="history-table-container">
        <table class="fixed-header-table" border="1">
            <thead>
                <tr>
                    <th>出力対象</th>
                    <th>日付</th>                    
                    <th>曜日</th>
                    <th>支払先・内容</th>
                    <th>経費科目</th>
                    <th>金額</th>
                    <th>メモ</th>
                    <th>部門名</th>
                    <th>部門コード</th>
                </tr>
            </thead>
            <tbody id="mainTableBody">
                <c:forEach var="info" items="${dateInfoList}">
                    <c:forEach var="item" items="${wrkList}">
                        <c:if test="${item.date == info.date}">
                            <tr>
                                <td><input type="checkbox" name="koutsuuhiList[${rowIndex}].checked" value="true" <c:if test="${item.checked}">checked</c:if> /></td>
                                <td>${info.displayDate}</td>
                                <td>${info.dayOfWeek}</td>
                                <td>${item.payee}</td>
                                <td>${item.expenseCategory}</td>
                                <td>${item.amount}</td>
                                <td>${item.memo}</td>
                                <td>${item.departmentName}</td>
                                <td>${item.departmentCode}</td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </c:forEach>
            
            </tbody>
        </table>
    </div>

    <!--モーダル画面-->
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