<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>メールテンプレ作成画面</title>
    <link rel="stylesheet" type="text/css" href="/css/createmail.css">
</head>
<body>

    <h2>アドレス登録</h2>
    <!-- メールアドレス入力フォーム -->
    <form action="sendaddress" method="post">
        <label for="email">メールアドレス:</label>
        <input type="text" id="email" name="email" required>
        <br><br>
        <input type="submit" value="メールアドレスを登録する">
    </form>

    <h2>メールテンプレ作成</h2>
   
    <!-- メール送信データを登録するフォーム -->
    <form action="sendMail" method="post">
        <label for="address">送信先:</label>
        <select name="address" id="address">
            <!-- コントローラーから渡されたメールアドレスリストをJSTLでループ処理して選択肢を作成 -->
            <c:forEach var="email" items="${emailList}">
                <option value="${email}">${email}</option>
            </c:forEach>
        </select>
        <br><br>

        <!-- 件名入力欄 -->
        <label for="subject">件名:</label>
        <input type="text" id="subject" name="subject" required>
        <br><br>

        <!-- 本文入力欄 -->
        <label for="message">本文:</label>
        <textarea id="message" name="message" rows="5" required></textarea>
        <br><br>

        <!-- フォームの送信ボタン（メールデータをDBに登録） -->
        <input type="submit" value="登録">
    </form>
    <br>

    <!-- 送信履歴の一覧画面に移動するボタン -->
    <div class="formaround">
        <a href="/viewmails">
            <button>登録済みテンプレ一覧を確認する</button>
        </a>
    </div>

</body>
</html>



