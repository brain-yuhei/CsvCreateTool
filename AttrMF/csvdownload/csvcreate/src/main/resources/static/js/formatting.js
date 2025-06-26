
/**
 * 金額欄欄のフォーカス移動時変換機能
 * 
 * @param {*} input 
 */
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

    // 数値として無効なら空にする、有効ならそのまま
    const number = parseFloat(numericValue);
    if (!isNaN(number)) {
        input.value = numericValue; // 整形せずそのまま代入
    } else {
        input.value = ''; // 無効な場合は空にする
    }
}
