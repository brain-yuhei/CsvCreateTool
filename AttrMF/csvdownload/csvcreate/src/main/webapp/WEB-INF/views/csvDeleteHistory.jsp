<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVデータ削除確認画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/csvDeleteHistory.css">    
</head>
<body>

<h2>確認</h2>
<p>既に履歴データが存在します。削除して新しく作成してもよろしいですか？</p>

<div class="message-box">
<form method="post" action="/csvDeleteHistory">
    <input type="hidden" name="selectedPayee" value="${selectedPayee}" />
    <input type="hidden" name="selectedMonth" value="${selectedMonth}" />
    <button type="submit">削除して新規作成</button>
</form>

<form method="post" action="/csvHistoryDisplay">
    <input type="hidden" name="selectedPayee" value="${selectedPayee}" />
    <input type="hidden" name="selectedMonth" value="${selectedMonth}" />
    <button type="submit">履歴を表示</button>
</form>

<form method="post" action="/returnFromConfirm">
    <input type="hidden" name="selectedPayee" value="${selectedPayee}" />
    <input type="hidden" name="selectedMonth" value="${selectedMonth}" />
    <button type="submit">キャンセル</button>
</form>

</div>

</body>