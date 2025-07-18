
/**
 * 行追加処理
 * 
 * @param {*} dateStr 日付文字列
 * @returns 
 */
function addRowToDate(dateStr) {
    const targetRow = document.getElementById('section-' + dateStr);
    if (!targetRow) return;

    const newRow = document.createElement('tr');
    const rowIndex = getNextRowIndex(); // name属性に使うインデックス
    const displayIndex = rowIndex + 1;  // 表示用インデックス（1始まり）

    const displayDate = formatDateDisplay(dateStr);

    // 直前の行の支払先を取得
    const lastPayeeSelect = targetRow.querySelector(`select[name^="koutsuuhiList"]`);
    const selectedPayee = lastPayeeSelect ? lastPayeeSelect.value : "";

    newRow.innerHTML = `
        <td>
            <input type="checkbox" name="koutsuuhiList[${rowIndex}].checked" value="true" checked>
            <input type="hidden" name="_koutsuuhiList[${rowIndex}].checked" value="off">
        </td>

        <td data-type="date">${displayDate}
            <input type="hidden" name="koutsuuhiList[${rowIndex}].date" value="${dateStr}" />
            <input type="hidden" name="koutsuuhiList[${rowIndex}].isNewRow" value="true" />
            <input type="hidden" name="koutsuuhiList[${rowIndex}].displayIndex" value="${displayIndex}" />
        </td>

        <td>${getDayOfWeek(dateStr)}</td>

        <td>
            <select name="koutsuuhiList[${rowIndex}].payee" onchange="onPayeeChange(this, '${rowIndex}')">
                ${getPayeeOptions(selectedPayee)}
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

        <td style="display: none;">
            <span id="departmentName-${rowIndex}"></span>
            <input type="hidden" name="koutsuuhiList[${rowIndex}].departmentName" />
        </td>

        <td style="display: none;">
            <span id="departmentCode-${rowIndex}"></span>
            <input type="hidden" name="koutsuuhiList[${rowIndex}].departmentCode" />
        </td>

        <td>
            <button type="button" onclick="addRowToDate('${dateStr}')">＋</button>
        </td>

        <td>
            <button type="button" onclick="markRowAsDeleted(this)">削除</button>
            <input type="hidden" name="koutsuuhiList[${rowIndex}].deleted" value="false" />
        </td>

    `;

    targetRow.parentNode.insertBefore(newRow, targetRow.nextSibling);

    // 追加した行の支払先を取得
    const newPayeeSelect = newRow.querySelector(`select[name="koutsuuhiList[${rowIndex}].payee"]`);

    // 自動反映処理を呼び出す
    onPayeeChange(newPayeeSelect, rowIndex);
}

function markRowAsDeleted(button) {
    const row = button.closest('tr');
    if (!row) return;

    const dateValue = row.querySelector('input[name$=".date"]')?.value;
    if (!dateValue) return;

    // 同じ日付の行がいくつあるか確認（未削除のみ）
    const allRows = document.querySelectorAll(`#mainTableBody tr`);
    const sameDateRows = Array.from(allRows).filter(r => {
        const hiddenDate = r.querySelector('input[name$=".date"]')?.value;
        const deleted = r.querySelector('input[name$=".deleted"]')?.value;
        return hiddenDate === dateValue && deleted === "false" && r.style.display !== "none";
    });

    if (sameDateRows.length <= 1) {
        alert(`最低1日分は必要です。削除できません。`);
        return;
    }

    const deletedInput = row.querySelector('input[name$=".deleted"]');
    if (deletedInput) {
        deletedInput.value = "true";
    }

    row.style.display = "none";
}


function updateDisplayIndexes() {
    const rows = document.querySelectorAll('#mainTableBody tr');
    rows.forEach((row, i) => {
        const index = i + 1;
        const hiddenInput = row.querySelector('input[name$=".displayIndex"]');
        if (hiddenInput) {
            hiddenInput.value = index;
        }
    });
}


function getNextRowIndex() {
    return window.rowIndexCounter++;
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

function getPayeeOptions(selectedPayee = "") {
    const payees = window.selectedPayees || [];
    return payees.map(p => {
        const selected = (p === selectedPayee) ? ' selected' : '';
        return `<option value="${p}"${selected}>${p}</option>`;
    }).join('');
}


document.addEventListener('DOMContentLoaded', () => {
    // JSPからselectedPayeesを受け取る
    window.selectedPayees = Array.from(
        document.querySelectorAll('select[name$=".payee"] option')
    )
    .map(opt => opt.value)
    .filter((v, i, a) => a.indexOf(v) === i);

    window.rowIndexCounter = document.querySelectorAll('#mainTableBody tr').length;

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




