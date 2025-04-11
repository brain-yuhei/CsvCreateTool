<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CSVファイル簡易作成ツール</title>
</head>
<body>

    <h2>CSVファイル簡易作成ツール</h2>

    <!-- カレンダーを配置 -->
    <div class ="calendar">
        <label for="calendar_Text">年月選択</label>
        <input type="month" id="calendar_Text" name="calendar_Text">
    </div>

    <!-- ファイル選択を配置 -->
    <div class ="Fileselect">
        <label for="Fileselect_Text">CSVファイル</label>
        <input type="file">
    </div>


    <!-- ボタンを配置 -->
    <div class="checkbutton"> 
        <form action="/viewkeiro" method="get">
            <button type="submit" name="status" value="sent">出力内容確認</button> 
        </form>       
    </div>
    <div class="applybutton"> 
        <form action="/viewkeiro" method="get">
            <button type="submit" name="status" value="all">反映</button> 
        </form>       
    </div>

    <form action="/SelectedKeiro" method="post"> 
        <table border="1">
            <thead>
                <tr>
                    <th>日付</th>
                    <th>チェックボックス</th>
                    <th>編集</th>
                    <th>支払先・内容</th>
                    <th>経費科目</th>
                    <th>金額</th>
                    <th>メモ</th>
                </tr>
            </thead>
        </table> 

    </form>

    <script>

    </script>
</body>
</html>