
/**
 * 経費科目・金額・メモ・部門情報などを自動で反映する
 *
 * @param {HTMLSelectElement} selectElement - 選択された支払先・内容
 * @param {number} rowIndex - 行番号
 */
function onPayeeChange(selectElement, rowIndex) {
    // 選択された支払先の値を取得
    const selectedPayee = selectElement.value;

    // 該当行の日付を取得
    const date = document.querySelector(`[name="koutsuuhiList[${rowIndex}].date"]`).value;

    // サーバーに支払先と日付を送信し、関連情報を取得
    fetch('/api/updateRowByPayee', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ payee: selectedPayee, date: date })
    })
    .then(response => response.json())
    .then(data => {
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].expenseCategory"]`).value = data.expense_category || '';
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].amount"]`).value = data.amountInclusiveTax != null ? data.amountInclusiveTax : '';
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].memo"]`).value = data.memo || '';
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].departmentName"]`).value = data.department_name || '';
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].departmentCode"]`).value = data.department_code || '';

        // 表示用の <span> にも部門名・コードを反映
        const nameSpan = document.getElementById(`departmentName-${rowIndex}`);
        if (nameSpan) nameSpan.textContent = data.department_name || '';

        const codeSpan = document.getElementById(`departmentCode-${rowIndex}`);
        if (codeSpan) codeSpan.textContent = data.department_code || '';
    })
    .catch(error => {
        console.error('エラーが発生しました:', error);
    });
}

