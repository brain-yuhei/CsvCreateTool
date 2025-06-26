
function onPayeeChange(selectElement, rowIndex) {
    const selectedPayee = selectElement.value;
    const date = document.querySelector(`[name="koutsuuhiList[${rowIndex}].date"]`).value;

    fetch('/api/updateRowByPayee', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ payee: selectedPayee, date: date })
    })
    .then(response => response.json())
    .then(data => {
        // 金額はそのままセット（小数点2桁処理は削除）
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].expenseCategory"]`).value = data.expense_category || '';
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].amount"]`).value = data.amountInclusiveTax != null ? data.amountInclusiveTax : '';
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].memo"]`).value = data.memo || '';
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].departmentName"]`).value = data.department_name || '';
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].departmentCode"]`).value = data.department_code || '';

        // 表示用の <span> にも反映
        const nameSpan = document.getElementById(`departmentName-${rowIndex}`);
        if (nameSpan) nameSpan.textContent = data.department_name || '';

        const codeSpan = document.getElementById(`departmentCode-${rowIndex}`);
        if (codeSpan) codeSpan.textContent = data.department_code || '';
    })
    .catch(error => {
        console.error('エラーが発生しました:', error);
    });   
}
