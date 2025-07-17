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
    <script src="${pageContext.request.contextPath}/js/formatting.js"defer></script>
    <script src="${pageContext.request.contextPath}/js/checkbox.js"defer></script>
    <script src="${pageContext.request.contextPath}/js/edit.js"defer></script>
</head>

<body>

    <button type="button" onclick="location.href='/'">戻る</button>

    <p>登録経路一覧表</p>

    <c:if test="${not empty message}">
        <div class="message">${message}</div>
    </c:if>

    <c:if test="${not empty errormessage}">
        <div class="error-message">${errormessage}</div>
    </c:if>

    <form id="mstForm" action="/viewMasterTable" method="post" method="get" modelAttribute="formDto">

        <button type="submit">更新</button>
        <button type="button" id="deleteBtn" onclick="submitDelete('${row.id}')">一括削除</button>

        <div class="master-table-container">
            <table class="fixed-header-table" border="1">
                <thead>
                    <tr>
                        <th><input type="checkbox" id="checkAll" onclick="allCheckboxes(this)"></th>
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
                        <tr>
                            <td><input type="checkbox" class="checks" name="mstKeiroList[${status.index}].selected"></td>
                            <td><input type="text" name="mstKeiroList[${status.index}].payeeContent" value="${row.payeeContent}" required></td>
                            <td>
                                <input type="text" class="amount-input" name="mstKeiroList[${status.index}].amountInclusiveTax" value="${row.amountInclusiveTax}" onblur="formatAmount(this); validateAmount(this, '${rowIndex}')" placeholder="交通費を入力してください。" required>
                            </td>
                            <td><input type="text" name="mstKeiroList[${status.index}].memo" value="${row.memo}" required></td>
                            <td><input type="text" name="mstKeiroList[${status.index}].expense_category" value="${row.expense_category}" required></td>
                            <td><input type="text" name="mstKeiroList[${status.index}].department_name" value="${row.department_name}" required></td>
                            <td><input type="text" name="mstKeiroList[${status.index}].department_code" value="${row.department_code}" required></td>
                            <td>
                                <input type="hidden" name="mstKeiroList[${status.index}].id" value="${row.id}" />
                                <button type="button" onclick="submitDelete('${row.id}')">削除</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </form>

    <!--各行の削除機能-->
    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/mstdelete" style="display:none;">
        <input type="hidden" name="id" id="deleteId" />
    </form>

    <!--複数行の一括削除機能-->
    <form id="checkDeleteForm" method="post" action="${pageContext.request.contextPath}/mstdelete/checked" style="display:none;">
    </form>
    

    <p>経路新規追加</p>

    <form id="mstCreateForm" action="/createMasterTable" method="post">

        <button type="submit">登録</button>

        <div class="master-table-container">
            <table class="fixed-header-table" border="1">
                <thead>
                    <tr>
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
                        <td><input type="text" name="payeeContent" placeholder="例：客先業務片道" required></td>
                        <td><input type="text" name="amountInclusiveTax" onblur="formatAmount(this); validateAmount(this, '${rowIndex}')" placeholder="例：380" required></td>
                        <td><input type="text" name="memo" placeholder="例：大阪->名古屋" required></td>
                        <td><input type="text" name="expense_category" placeholder="例：旅費交通費" required></td>
                        <td><input type="text" name="department_name" placeholder="例：大阪支社" required></td>
                        <td><input type="text" name="department_code" placeholder="例：19" required></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </form>


</body>
</html>