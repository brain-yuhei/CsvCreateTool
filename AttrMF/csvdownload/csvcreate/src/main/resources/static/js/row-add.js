function addRowToDate(dateStr) {
    const targetRow = document.getElementById('section-' + dateStr);
    if (!targetRow) return;

    const newRow = document.createElement('tr');
    const rowIndex = getNextRowIndex(); // 現在の行数を取得（0からの連番）

    newRow.innerHTML = `
        <td>
            <input type="checkbox" name="koutsuuhiList[${rowIndex}].checked" value="true">
            <input type="hidden" name="_koutsuuhiList[${rowIndex}].checked" value="off">
        </td>
        <td>${getDayOfWeek(dateStr)}</td>
        <td data-type="date">${dateStr}
            <input type="hidden" name="koutsuuhiList[${rowIndex}].date" value="${dateStr}" />
        </td>
        <td>
            <select name="koutsuuhiList[${rowIndex}].payee" onchange="onPayeeChange(this, '${rowIndex}')">
                ${getPayeeOptions()}
            </select>
        </td>
        <td>
            <input type="text" name="koutsuuhiList[${rowIndex}].expenseCategory"
                   onblur="validateExpenseCategory(this, '${rowIndex}')">
            <div id="error-expenseCategory-${rowIndex}" class="error-message" style="color:red; display:none;"></div>
        </td>
        <td>
            <input type="text" name="koutsuuhiList[${rowIndex}].amount"
                   onblur="formatAmount(this); validateAmount(this, '${rowIndex}')">
            <div id="error-amount-${rowIndex}" class="error-message" style="color:red; display:none;"></div>
        </td>
        <td>
            <input type="text" name="koutsuuhiList[${rowIndex}].memo"
                   onblur="validateMemo(this, '${rowIndex}')">
            <div id="error-memo-${rowIndex}" class="error-message" style="color:red; display:none;"></div>
        </td>
        <td><input type="hidden" name="koutsuuhiList[${rowIndex}].departmentName" /></td>
        <td><input type="hidden" name="koutsuuhiList[${rowIndex}].departmentCode" /></td>
        <td><button type="button" onclick="removeRow(this)">－</button></td>
    `;

    targetRow.parentNode.insertBefore(newRow, targetRow.nextSibling);
}

function getNextRowIndex() {
    // tr要素数＝現在の入力データ数とする（削除後も番号が詰まる）
    return document.querySelectorAll('#mainTableBody tr').length;
}



function removeRow(button) {
    const row = button.closest('tr');
    if (row) {
        row.remove();
    }
}

function getDayOfWeek(dateStr) {
    const date = new Date(dateStr);
    return ['日','月','火','水','木','金','土'][date.getDay()];
}

function getPayeeOptions() {
    const payees = window.selectedPayees || [];
    return payees.map(p => `<option value="${p}">${p}</option>`).join('');
}

document.addEventListener('DOMContentLoaded', () => {
    // JSPからselectedPayeesを受け取る
    window.selectedPayees = Array.from(
        document.querySelectorAll('select[name$=".payee"] option')
    )
    .map(opt => opt.value)
    .filter((v, i, a) => a.indexOf(v) === i);
});



