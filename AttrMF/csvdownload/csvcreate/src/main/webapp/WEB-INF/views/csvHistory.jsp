<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイル簡易作成ツール</title> 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/csvHistory.css"> 
</head>
<body>

    <div class="page_header">
        <h2>過去内容出力画面</h2>
        <a href="/saveWorkTable" class="history-button">編集画面に戻る</a>
    </div>

    <!--表示する履歴の年月選択-->
    <!--ワークテーブルから年月を取得しプルダウン選択できる-->
    <form method="get" action="/csvHistory">
        <label for="selectedMonth">表示年月：</label>
        <select name="selectedMonth" id="selectedMonth">
            <c:forEach var="month" items="${historyMonth}">
                <option value="${month}">${month}</option>
            </c:forEach>
        </select>
        <button type="submit">表示</button>
    </form>
    

    <!--編集不可のCSVデータ表を表示-->
    <table>
        <thead>
            <tr>
                <th>日付</th>                    
                <th>曜日</th>
                <th>支払先・内容</th>
                <th>経費科目</th>
                <th>金額</th>
                <th>メモ</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="info" items="${dateInfoList}">
                <c:forEach var="item" items="${wrkList}">
                    <c:if test="${item.date == info.date}">
                        
                    </c:if>
                </c:forEach>
            </c:forEach>
        </tbody>
    </table>

    <!--モーダル画面-->


</body>
</html>