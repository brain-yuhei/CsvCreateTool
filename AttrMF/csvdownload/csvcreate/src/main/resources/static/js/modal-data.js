
// チェックされている日付を取得
function getSelectedDates() {
    // name属性が koutsuuhiList[0].checked などになっているチェックボックスを取得
    const checkboxes = document.querySelectorAll('input[type="checkbox"][name$=".checked"]:checked');

    return Array.from(checkboxes).map(cb => {
        // 同じ行の中にある「日付」 hidden input から値を取得
        const row = cb.closest('tr');
        const dateInput = row.querySelector('input[name$=".date"]');
        return dateInput ? dateInput.value.trim() : "";
    });
}


// 日付セルのテキストを取得
function getDateFromRow(row) {
    const dateCell = row.querySelector('td[data-type="date"]');
    const hiddenInput = dateCell ? dateCell.querySelector('input[type="hidden"]') : null;
    return hiddenInput ? hiddenInput.value.trim() : "";
}

// 必要な列だけ抽出する
function buildModalRow(originalRow) {
    const newRow = document.createElement('tr');
    const cells = originalRow.querySelectorAll('td');

    const colOrder = [2, 3, 4, 5, 6, 7, 8];

    colOrder.forEach(index => {
        const newCell = document.createElement('td');
        const cell = cells[index];

        // input or select 要素を取得
        const input = cell.querySelector('input');
        const select = cell.querySelector('select');

        let value = "";
        if (select) {
            // 選択中のオプションの表示テキストのみ取得
            value = select.options[select.selectedIndex].text.trim();
        } else if (input) {
            value = input.value.trim();
        } else {
            value = cell.textContent.trim();
        }

        // select や input を含まず、プレーンなテキストだけをセット
        newCell.textContent = value;
        newRow.appendChild(newCell);
    });

    return newRow;
}