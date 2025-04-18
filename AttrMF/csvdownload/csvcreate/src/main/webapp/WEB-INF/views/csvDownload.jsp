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

    <!--CSVファイル登録画面用のボタン-->
    <div class = "upload_button">
        <a href = "">
            <button type = "button">CSVファイル登録</button>
        </a>
    </div>

    <!--年月選択欄-->
    <div class = "serect_Month">
        <form>
            <input>
        </form>
    </div>

    <!--メッセージ表示欄-->
    <div class = "messagelabel">

    </div>

    <!--ダウンロード内容確認モーダルを開くボタン-->
    <div class = "check_button">
        <button type = "button">ダウンロード内容確認</button>
    </div>

    <!--ダウンロード内容編集表-->
    <form>
        <table border="1">
            <thesd>
                <tr>
                <th><input type = "checkbox"></th>
                <th>曜日</th>
                <th>日付</th>
                <th>支払先・内容</th>
                <th>経費科目</th>
                <th>金額</th>
                <th>メモ</th>
                <th>費用負担部門</th>
                <th>費用負担部門コード</th>
                </tr>
            </thesd>
            <tbody>
                <tr>
                    <td>チェックボックス</td>
                    <td>曜日</td>
                    <td>日付</td>
                    <td>支払先・内容</td>
                    <td>経費科目</td>
                    <td>金額</td>
                    <td>メモ</td>
                    <td>費用負担部門</td>
                    <td>費用負担部門コード</td>
                </tr>
            </tbody>
        </table>
    </form>

    <!--ダウンロード内容確認モーダル画面-->
    <div>
        <h3>ダウンロード内容確認画面</h3>
        <table border="1">
            <thead>
                <tr>
                    <th>曜日</th>
                    <th>日付</th>
                    <th>支払先・内容</th>
                    <th>経費科目</th>
                    <th>金額</th>
                    <th>メモ</th>
                    <th>費用負担部門</th>
                    <th>費用負担部門コード</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
        <button>ダウンロード</button>
        <button>閉じる</button>
    </div>
    <!--モーダル画面のオーバーレイ-->
    <div></div>
</body>
</html>