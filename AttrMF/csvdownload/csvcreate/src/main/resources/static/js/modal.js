

// モーダルを表示
function openModal() {
    toggleModal(true); // モーダルとオーバーレイを表示

    // チェックされた日付の値を格納
    const selectedDates = getSelectedDates(); 

    // JSPファイルのモーダルテーブル情報を格納
    const modalBody = document.getElementById('modalTableBody');

    // モーダルテーブルを初期化
    modalBody.innerHTML = ""; 

    // JSPファイルのテーブル要素・テーブル本体・行の情報を格納
    const mainTableRows = document.querySelectorAll('table tbody tr');

    // すべての行をループ処理
    mainTableRows.forEach(row => {

        // 3列目の日付を格納（日付分処理を行うため）
        const dateText = getCellText(row, 2); 

        // チェックされた日付と一致する行だけ処理
        if (selectedDates.includes(dateText)) {
            // 列の生成メソッドを呼び出す
            const newRow = buildModalRow(row); 
            // モーダルテーブルに列を追加
            modalBody.appendChild(newRow); 
        }
    });
}

// モーダルとオーバーレイを非表示にする
function closeModal() {
    toggleModal(false);
}

// モーダルとオーバーレイの表示・非表示を切り替える
function toggleModal(show) {
    document.getElementById('outputModal').style.display = show ? 'block' : 'none';
    document.getElementById('modalOverlay').style.display = show ? 'block' : 'none';
}

// チェックされている日付を取得
function getSelectedDates() {
    return Array.from(document.querySelectorAll('input[name="selectedDates"]:checked'))
        .map(cb => cb.value);
}

// 指定された行のセルのテキストを取得
function getCellText(row, index) {
    const cell = row.querySelector(`td:nth-child(${index + 1})`);
    return cell ? cell.textContent.trim() : "";
}

// 必要な列だけ抽出する
function buildModalRow(originalRow) {
    // JSPファイルの行の情報を格納
    const newRow = document.createElement('tr');
    // JSPファイルの列の情報を格納
    const cells = originalRow.querySelectorAll('td');

    // 抽出する列の順番
    const colOrder = [2, 3, 4, 5, 6, 7, 8];

    colOrder.forEach(index => {
        const newCell = document.createElement('td');
        if (index !== null) {
            const cell = cells[index];
            const input = cell.querySelector('input');
            // セル内に input があればその値を、なければテキストを取得
            newCell.textContent = input ? input.value.trim() : cell.textContent.trim();
        }
        newRow.appendChild(newCell);
    });

    return newRow;
}

// 全選択のチェックボックスに応じて、すべてのチェックボックスをON/OFF
function selectAllCheckboxes() {
    const selectAll = document.getElementById("selectAll").checked;
    document.querySelectorAll('input[name="selectedDates"]').forEach(cb => {
        cb.checked = selectAll;
    });
}

// モーダル内のテーブルデータをCSV形式でダウンロードする
function downloadCSV() {
    // ヘッダー行
    const headers = ["日付", "支払先・内容", "経費科目", "金額", "メモ", "費用負担部門名", "費用負担部門コード", ];

    const selectedDates = getSelectedDates();

    // モーダル内の行を取得し、日付が選択されたものだけ処理
    const rows = Array.from(document.querySelectorAll("#outputModal tbody tr"))
        .filter(tr => selectedDates.includes(tr.querySelector("td").textContent.trim()))
        .map(tr => 
            Array.from(tr.querySelectorAll("td"))
                .map(td => `"${td.textContent.trim().replace(/"/g, '""')}"`) // ダブルクォートをエスケープ
                .join(",")
        );

    // CSV文字列を生成
    const csvContent = [headers.join(","), ...rows].join("\n");
    const blob = new Blob(["\uFEFF" + csvContent], { type: "text/csv;charset=utf-8;" });

    // 仮のリンクを作成してダウンロード実行
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "keihi_output.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}