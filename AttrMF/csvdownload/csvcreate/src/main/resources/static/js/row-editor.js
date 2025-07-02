
/**
 * 行追加処理
 * 
 * @param {*} dateStr 日付文字列
 * @returns 
 */
function addRowToDate(dateStr) {
    // 指定日付の行を取得
    const targetRow = document.getElementById('section-' + dateStr);

    // 対象無しの場合は処理終了
    if (!targetRow) return; 

    // 新しい行の要素を作成
    const newRow = document.createElement('tr');

    // 現在の行数の値を取得
    const rowIndex = getNextRowIndex();

    // 表示用に日付を整形
    const displayDate = formatDateDisplay(dateStr);

    // tr要素にHTMLを挿入
    newRow.innerHTML = `
        <!-- チェックボックス（行選択用） -->
        <td>
            <input type="checkbox" name="koutsuuhiList[${rowIndex}].checked" value="true" checked>
            <input type="hidden" name="_koutsuuhiList[${rowIndex}].checked" value="off">
        </td>

        <!-- 日付表示（送信用） -->
        <td data-type="date">${displayDate}
            <input type="hidden" name="koutsuuhiList[${rowIndex}].date" value="${dateStr}" />
        </td>

        <!-- 曜日表示 -->
        <td>${getDayOfWeek(dateStr)}</td>

        <!-- 支払先のセレクトボックス（onchangeで部門自動取得） -->
        <td>
            <select name="koutsuuhiList[${rowIndex}].payee" onchange="onPayeeChange(this, '${rowIndex}')">
                ${getPayeeOptions()}
            </select>
        </td>

        <!-- 経費科目入力 -->
        <td>
            <input type="text" name="koutsuuhiList[${rowIndex}].expenseCategory"
                   onblur="validateExpenseCategory(this, '${rowIndex}')">
            <div id="error-expenseCategory-${rowIndex}" class="error-message" style="color:red; display:none;"></div>
        </td>

        <!-- 金額入力 -->
        <td>
            <input type="text" name="koutsuuhiList[${rowIndex}].amount"
                   onblur="formatAmount(this); validateAmount(this, '${rowIndex}')">
            <div id="error-amount-${rowIndex}" class="error-message" style="color:red; display:none;"></div>
        </td>

        <!-- メモ欄 -->
        <td>
            <input type="text" name="koutsuuhiList[${rowIndex}].memo"
                   onblur="validateMemo(this, '${rowIndex}')">
            <div id="error-memo-${rowIndex}" class="error-message" style="color:red; display:none;"></div>
        </td>

        <!-- 部門名 -->
        <td style="display: none;">
            <span id="departmentName-${rowIndex}"></span>
            <input type="hidden" name="koutsuuhiList[${rowIndex}].departmentName" />
        </td>

        <!-- 部門コード -->
        <td style="display: none;">
            <span id="departmentCode-${rowIndex}"></span>
            <input type="hidden" name="koutsuuhiList[${rowIndex}].departmentCode" />
        </td>

        <!-- 削除ボタン -->
        <td>
            <button type="button" onclick="removeRow(this)">－</button>
        </td>

        <!-- 追加行フラグ -->
        <input type="hidden" name="koutsuuhiList[${rowIndex}].isNewRow" value="true" />
        <input type="hidden" name="koutsuuhiList[${rowIndex}].deleted" value="false" />
    `;

    // 追加先の行の直後に新しい行を挿入
    targetRow.parentNode.insertBefore(newRow, targetRow.nextSibling);
}



function markRowAsDeleted(button) {
    const row = button.closest('tr');
    if (!row) return;
    
    const deletedInput = row.querySelector('input[name$=".deleted"]');
    if (deletedInput) {
        deletedInput.value = "true";
    }

    row.style.display = "none"; // 視覚的に非表示に
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

/**
 * 日付の成形
 * 
 * @param {*} dateStr 
 * @returns 
 */
function formatDateDisplay(dateStr) {
    const date = new Date(dateStr);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return `${month}/${day}`; 
}




