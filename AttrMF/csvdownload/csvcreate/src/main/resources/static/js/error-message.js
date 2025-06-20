
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

function formatAmount(input) {
    let rawValue = input.value;

    // 全角数字 → 半角に変換（例："２３４．５" → "234.5"）
    rawValue = rawValue.replace(/[０-９．]/g, s => 
        String.fromCharCode(s.charCodeAt(0) - 65248)
    );

    // カンマや記号などの無効文字削除（数字とピリオドだけ残す）
    let numericValue = rawValue.replace(/[^0-9.]/g, '');

    // ピリオドが複数ある場合、最初の1つだけを使う
    const parts = numericValue.split('.');
    if (parts.length > 2) {
        numericValue = parts[0] + '.' + parts[1];
    }

    // 数値に変換
    const number = parseFloat(numericValue);
    if (!isNaN(number)) {
        // 整形して再代入（小数点2桁）
        input.value = number.toFixed(2);
    } else {
        input.value = ''; // 無効な場合は空にする
    }
}


