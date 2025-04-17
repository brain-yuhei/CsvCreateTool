<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイル簡易作成ツール</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Modal.css">
    <script src="${pageContext.request.contextPath}/js/modal.js"></script>
</head>
<body>

    <h2>CSVファイル簡易作成ツール</h2>

    <div class="form_group">
        <!-- 年月選択 -->
        <form id="monthForm" action="/changeMonth" method="post">
            <label for="calendar_Text">年月選択:</label>
            <input type="month" id="calendar_Text" name="selectedMonth" value="${currentTime}" required
                   onchange="document.getElementById('monthForm').submit();">
        </form>    

        <form action="/upload" method="post" enctype="multipart/form-data">
            <!-- CSVファイルのアップロード -->
            <label for="file">ファイルを選択:</label>
            <input type="file" id="file" name="file" required>
            <input type="hidden" name="selectedMonth" value="${currentTime}">
            <!-- フォーム送信ボタン -->
            <button type="submit">反映</button>
        </form>
    </div>
    <!-- メッセージを表示 -->
    <div class="messagelabel">
        <c:if test="${not empty message}">
            <p>${message}</p>
        </c:if>
    </div>

    <!-- 出力内容確認モーダルを開くボタン -->
    <div class="checkbutton">
        <button type="button" onclick="openModal()">出力内容確認</button>
    </div>

    <!-- CSV管理表 -->
    <form action="/SelectedKeiro" method="post">
        <table border="1">
            <thead>
                <tr>
                    <th><input type="checkbox" id="selectAll" onclick="selectAllCheckboxes(this)"></th>
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
                <c:forEach var="date" items="${dateList}">
                    <tr>
                        <td>
                            <input type="checkbox" name="selectedDates" value="${date}" checked>
                        </td>
                        <td>${date}</td>

                        <c:choose>
                            <c:when test="${not empty wrkList}">
                                <c:forEach var="item" items="${wrkList}">
                                    <td><input type="text" name="payee" value="${item.payee}"></td>
                                    <td><input type="text" name="expenseCategory" value="${item.expenseCategory}"></td>
                                    <td><input type="text" name="amount" value="${item.amount}"></td>
                                    <td><input type="text" name="memo" value="${item.memo}"></td>
                                    <td><input type="text" name="departmentName" value="${item.departmentName}"></td>
                                    <td><input type="text" name="departmentCode" value="${item.departmentCode}"></td>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <td colspan="6">データなし</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </form>

    <!-- モーダル画面 -->
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



