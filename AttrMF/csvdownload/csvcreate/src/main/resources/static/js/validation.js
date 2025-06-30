/**
 * 金額欄のフォーカス移動時エラーチェック
 * 
 * @param {*} input 入力内容
 * @param {*} index 行番号
 * @returns 
 */
function validateAmount(input, index) {
    index = parseInt(index);
    let value = input.value.trim();
    const errorDiv = document.getElementById(`error-amount-${index}`);

    // 空欄のチェックを呼び出す
    errorTextSet(input, errorDiv, "金額を入力してください。");

}

/**
 * メモ欄のフォーカス移動時エラーチェック
 * 
 * @param {*} input 入力内容
 * @param {*} index 行番号
 */
function validateMemo(input, index) {
    index = parseInt(index);
    const value = input.value.trim();
    const errorDiv = document.getElementById(`error-memo-${index}`);

    // 空欄のチェックを呼び出す
    errorTextSet(input, errorDiv, "メモを入力してください。");

    if (value.length > 50) {
        errorDiv.textContent = "メモは50文字以内で入力してください。";
        errorDiv.style.display = "block";
        return;
    } 
}

/**
 * 経費科目欄のフォーカス移動時エラーチェック
 * 
 * @param {*} input 入力内容
 * @param {*} index 行番号
 */
function validateExpenseCategory(input, index) {
    index = parseInt(index); // ← 必要なら数値に変換
    const value = input.value.trim();
    const errorDiv = document.getElementById(`error-expenseCategory-${index}`);

    // 空欄のチェックを呼び出す
    errorTextSet(input, errorDiv, "経費科目を入力してください。");    

}

function submitPayeeForm() {
    const payee = document.getElementById("selectedPayee").value;
    if (payee === "") {
        alert("経路を選択してください。");
        return;
    }
    document.getElementById("filterForm").submit();
}

function removeRequired() {
    document.getElementById('selectedPayee').removeAttribute('required');
}

function addRequired() {
    document.getElementById('selectedPayee').setAttribute('required', 'required');
}

/**
 * 未入力チェック処理
 * 
 * @param {*} input 入力内容
 * @param {*} errorDiv 行情報
 * @param {*} message エラーメッセージ
 */
function errorTextSet(input, errorDiv, message) {

    // 入力内容を変数に格納
    const value = input.value.trim();

    // 未入力チェック
    if (value === "") {
        errorDiv.textContent = message;
        errorDiv.style.display = "block";
        return;
    } else {
        errorDiv.textContent = "";
        errorDiv.style.display = "none";
    }

}