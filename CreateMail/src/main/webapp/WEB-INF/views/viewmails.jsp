<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登録履歴</title>
    <link rel="stylesheet" type="text/css" href="/css/viewmails.css">
</head>
<body>

    <h2>登録履歴</h2>

    <!-- ソートボタンを配置 -->
    <div class="sortbutton"> 
        <form action="/viewmails" method="get">
            <button type="submit" name="status" value="sent">送信済み</button>
            <button type="submit" name="status" value="unsent">未送信</button>
            <button type="submit" name="status" value="all">全て表示</button> 
        </form>       
    </div>
    <div class="backlink">
        <a href="/">メールテンプレ作成画面に戻る</a>
    </div>

    <form action="/sendSelectedMails" method="post"> <!-- まとめて送信するフォーム -->
        <table border="1">
            <thead>
                <tr>
                    <th><input type="checkbox" id="selectAll" onclick="selectAllCheckboxes(this)"></th>
                    <th>メールアドレス</th>
                    <th>件名</th>
                    <th>本文</th>
                    <th>送信済み</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="repository" items="${repository}">
                    <tr>
                        <td><input type="checkbox" name="selectedIds" value="${repository.id}"></td>
                        <td>${repository.email}</td>
                        <td>${repository.subject}</td>
                        <td>${repository.message}</td>
                        <td>${repository.sent ? '送信済み' : '未送信'}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table> 
        <div class="sendmailbutton">
            <input type="submit" value="選択したメールを送信">
        </div> 
    </form>

    <script>
        function selectAllCheckboxes(source) {
            const checkboxes = document.querySelectorAll('input[name="selectedIds"]');
            checkboxes.forEach(checkbox => {
                checkbox.checked = source.checked;
            });
        }
    </script>
</body>
</html>


