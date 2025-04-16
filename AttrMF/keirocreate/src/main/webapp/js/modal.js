// モーダルを表示
function openModal() {
    toggleModal(true); // モーダルとオーバーレイを表示

    const selectedDates = getSelectedDates(); // チェックされた日付を取得
    const modalBody = document.getElementById('modalTableBody');
    modalBody.innerHTML = ""; // モーダル内のテーブルを初期化

    const mainTableRows = document.querySelectorAll('table tbody tr');
    mainTableRows.forEach(row => {
        const dateText = getCellText(row, 1); // 2列目（index 1）の日付を取得

        // チェックされた日付と一致する行だけ処理
        if (selectedDates.includes(dateText)) {
            const newRow = buildModalRow(row); // 必要な列だけ抽出して新しい行を作成
            modalBody.appendChild(newRow);     // モーダルテーブルに追加
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

// チェックされた日付の値を配列として取得
function getSelectedDates() {
    return Array.from(document.querySelectorAll('input[name="selectedDates"]:checked'))
        .map(cb => cb.value);
}

// 指定された行のセルのテキストを取得
function getCellText(row, index) {
    const cell = row.querySelector(`td:nth-child(${index + 1})`);
    return cell ? cell.textContent.trim() : "";
}

// モーダル用に必要な列だけ抽出する
function buildModalRow(originalRow) {
    const newRow = document.createElement('tr');
    const cells = originalRow.querySelectorAll('td');

    // 抽出する列の順番
    const colOrder = [1, 2, 3, 4, null, null, null, null, 5, 6, 7];

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

    // 最終的に18列以下の場合はtdを追加
    while (newRow.children.length < 18) {
        newRow.appendChild(document.createElement('td'));
    }

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
    const headers = [
        "日付", "支払先・内容", "経費科目", "金額", "自社出席代表者名", "自社出席者人数", 
        "他社出席代表者名", "他社出席者人数", "メモ", "費用負担部名", "費用負担部コード", 
        "プロジェクト名", "税区分", "通貨", "為替レート", "貸方勘定科目", "貸方補助科目", "申請番号"
    ];

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
