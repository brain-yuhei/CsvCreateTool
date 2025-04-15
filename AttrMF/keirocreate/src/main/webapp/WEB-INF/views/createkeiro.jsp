<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイル簡易作成ツール</title>
    <!-- スタイルシートとJavaScriptファイルを読み込み -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css">
    <script src="${pageContext.request.contextPath}/js/modal.js"></script>
</head>
<body>

    <!-- タイトル表示 -->
    <h2>CSVファイル簡易作成ツール</h2>

    <!-- アップロード -->
    <form action="/upload" method="post" enctype="multipart/form-data">
        <!-- ユーザーが選択する年月 -->
        <label for="calendar_Text">年月選択:</label>
        <input type="month" id="calendar_Text" name="selectedMonth" value="${currentTime}" required>

        <!-- CSVファイルのアップロード -->
        <label for="file">ファイルを選択:</label>
        <input type="file" id="file" name="file" required>

        <!-- フォーム送信ボタン -->
        <button type="submit">反映</button>
    </form>

    <!-- メッセージを表示 -->
    <div class="messagelabel">
        <!-- メッセージがある場合に表示する -->
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
                    <th>日付</th>
                    <th>支払先・内容</th>
                    <th>経費科目</th>
                    <th>金額</th>
                    <th>メモ</th>
                </tr>
            </thead>
            <tbody>
                <!-- 選択した年月の各日付に対して1行ずつ出力 -->
                <c:forEach var="date" items="${dateList}">
                    <tr>
                        <!-- 各行の日付を表示 -->
                        <td>${date}</td>

                        <!-- ワークテーブルにデータがある場合の表示 -->
                        <c:choose>
                            <c:when test="${not empty wrkList}">
                                <!-- ワークテーブルの1件目のみ表示 -->
                                <c:set var="item" value="${wrkList[0]}" />
                                <td>${item.payee}</td>
                                <td>${item.expenseCategory}</td>
                                <td>${item.amount}</td>
                                <td>${item.memo}</td>
                            </c:when>

                            <!-- データが存在しない場合の表示 -->
                            <c:otherwise>
                                <td colspan="4">データなし</td>
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
            <tbody>
                <!-- 各日付ごとに1行出力 -->
                <c:forEach var="date" items="${dateList}">
                    <tr>
                        <td>${date}</td>

                        <c:choose>
                            <c:when test="${not empty wrkList}">
                                <c:set var="item" value="${wrkList[0]}" />
                                <td>${item.payee}</td>
                                <td>${item.expenseCategory}</td>
                                <td>${item.amount}</td>
                                <td></td><td></td><td></td><td></td>
                                <td>${item.memo}</td>
                                <td>${item.departmentName}</td>
                                <td>${item.departmentCode}</td>
                                <td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                            </c:when>

                            <c:otherwise>
                                <td colspan="4">データなし</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- モーダルの操作ボタン -->
        <br/>
        <button onclick="downloadCSV()">出力</button>
        <button onclick="closeModal()">閉じる</button>
    </div>

    <!-- モーダル画面のオーバーレイ -->
    <div id="modalOverlay"></div>

</body>
</html>


