<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登録経路編集画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mstTableView.css">
    <script src="${pageContext.request.contextPath}/js/delete.js"defer></script>
</head>

<body>

    <p>登録経路一覧表</p>

    <form id="mstForm" action="/viewMasterTable" method="post" method="get" modelAttribute="formDto">

        <button type="submit">更新</button>

        <div class="master-table-container">
            <table class="fixed-header-table" border="1">
                <thead>
                    <tr>
                        <th><input type="checkbox" id="checkAll"></th>
                        <th>経路</th>
                        <th>金額</th>
                        <th>メモ</th>
                        <th>経費科目</th>
                        <th>費用負担部門名</th>
                        <th>費用負担部門コード</th>
                        <th>削除</th>
                    </tr>
                </thead>
                <tbody id="mainTableBody">
                    <c:forEach var="row" items="${mstdataList}" varStatus="status">
                        <input type="hidden" name="mstKeiroList[${status.index}].id" value="${row.id}" />
                        <tr>
                            <td><input type="checkbox" name="mstKeiroList[${status.index}].selected"></td>
                            <td><input type="text" name="mstKeiroList[${status.index}].payeeContent" value="${row.payeeContent}" required></td>
                            <td><input type="text" name="mstKeiroList[${status.index}].amountInclusiveTax" value="${row.amountInclusiveTax}" required></td>
                            <td><input type="text" name="mstKeiroList[${status.index}].memo" value="${row.memo}" required></td>
                            <td><input type="text" name="mstKeiroList[${status.index}].expense_category" value="${row.expense_category}" required></td>
                            <td>${row.department_name}<input type="hidden" name="mstKeiroList[${status.index}].department_name" value="${row.department_name}" required></td>
                            <td>${row.department_code}<input type="hidden" name="mstKeiroList[${status.index}].department_code" value="${row.department_code}" required></td>
                            <td><button type="button">削除</button></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </form>

    <p>経路新規追加</p>

    <form id="mstCreateForm" action="/createMasterTable" method="post">

        <button type="submit">登録</button>

        <div class="master-table-container">
            <table class="fixed-header-table" border="1">
                <thead>
                    <tr>
                        <th><input type="checkbox" id="checkAll"></th>
                        <th>経路</th>
                        <th>金額</th>
                        <th>メモ</th>
                        <th>経費科目</th>
                        <th>費用負担部門名</th>
                        <th>費用負担部門コード</th>
                    </tr>
                </thead>
                <tbody id="mainTableBody">
                    <tr>
                        <td><input type="checkbox" name="selectedRow"></td>
                        <td><input type="text" name="payeeContent" required></td>
                        <td><input type="text" name="amountInclusiveTax" required></td>
                        <td><input type="text" name="memo" required></td>
                        <td><input type="text" name="expense_category" required></td>
                        <td><input type="text" name="department_name" required></td>
                        <td><input type="text" name="department_code" required></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </form>


</body>
</html>