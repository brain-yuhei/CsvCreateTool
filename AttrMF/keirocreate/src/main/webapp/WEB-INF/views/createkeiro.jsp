<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイル簡易作成ツール</title>
</head>
<body>

    <h2>CSVファイル簡易作成ツール</h2>

    <!-- アップロードフォーム -->
    <form action="/upload" method="post" enctype="multipart/form-data">
        <label for="calendar_Text">年月選択:</label>
        <input type="month" id="calendar_Text" name="selectedMonth" value="${currentTime}" required>
        <label for="file">ファイルを選択:</label>
        <input type="file" id="file" name="file" required>
        <button type="submit">反映</button>
    </form>

    <!-- メッセージ表示 -->
    <div class="messagelabel">
        <c:if test="${not empty message}">
            <p>${message}</p>
        </c:if>
    </div>

    <!-- 出力内容確認ボタン -->
    <div class="checkbutton"> 
        <form action="/viewkeiro" method="get">
            <button type="submit" name="status" value="sent">出力内容確認</button> 
        </form>       
    </div>

    <!-- 表示テーブル -->
    <form action="/SelectedKeiro" method="post"> 
        <table border="1">
            <thead>
                <tr>
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
                        <td>${date}</td>
                        <c:if test="${not empty wrkList}">
                            <c:set var="item" value="${wrkList[0]}" />
                            <td>${item.payee}</td>
                            <td>${item.expenseCategory}</td>
                            <td>${item.amount}</td>
                            <td>${item.memo}</td>
                            <td>${item.departmentName}</td>
                            <td>${item.departmentCode}</td>
                        </c:if>
                        <c:if test="${empty wrkList}">
                            <td colspan="6">データなし</td>
                        </c:if>
                    </tr>
                </c:forEach>
            </tbody>
            
        </table>
    </form>

</body>
</html>
