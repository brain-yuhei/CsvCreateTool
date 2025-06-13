
function validateAmount(input, index) {
    index = parseInt(index);
    let value = input.value.trim();
    const errorDiv = document.getElementById(`error-amount-${index}`);

    // バリデーション：空チェック
    if (value === "") {
        errorDiv.textContent = "金額を入力してください。";
        errorDiv.style.display = "block";
        return;
    }

    // バリデーション：数値チェック（整数または小数）
    if (!/^\d+(\.\d{0,2})?$/.test(value)) {
        errorDiv.textContent = "金額は正しい数値で入力してください。";
        errorDiv.style.display = "block";
        return;
    }

    // バリデーションOK → エラー非表示
    errorDiv.textContent = "";
    errorDiv.style.display = "none";

    // 小数点以下2桁に整形（例：100 → 100.00）
    const formattedValue = parseFloat(value).toFixed(2);
    input.value = formattedValue;
}


function validateMemo(input, index) {
    index = parseInt(index);
    const value = input.value.trim();
    const errorDiv = document.getElementById(`error-memo-${index}`);

    if (value === "") {
        errorDiv.textContent = "メモを入力してください。";
        errorDiv.style.display = "block";
    } else if (value.length > 30) {
        errorDiv.textContent = "メモは30文字以内で入力してください。";
        errorDiv.style.display = "block";
    } else {
        errorDiv.textContent = "";
        errorDiv.style.display = "none";
    }
}


function validateExpenseCategory(input, index) {
    index = parseInt(index); // ← 必要なら数値に変換
    const value = input.value.trim();
    const errorDiv = document.getElementById(`error-expenseCategory-${index}`);

    if (value === "") {
        errorDiv.textContent = "経費科目を入力してください。";
        errorDiv.style.display = "block";
    } else {
        errorDiv.textContent = "";
        errorDiv.style.display = "none";
    }
}
