function openModal() {
    // モーダル表示
    document.getElementById('outputModal').style.display = 'block';
    document.getElementById('modalOverlay').style.display = 'block';

    const modalBody = document.getElementById('modalTableBody');
    modalBody.innerHTML = "";

    const selectedDates = Array.from(document.querySelectorAll('input[name="selectedDates"]:checked'))
        .map(cb => cb.value);

    const mainTableRows = document.querySelectorAll('table tbody tr');

    mainTableRows.forEach(row => {
        const dateCell = row.querySelector('td:nth-child(2)');
        const dateText = dateCell ? dateCell.textContent.trim() : "";

        if (selectedDates.includes(dateText)) {
            const newRow = document.createElement('tr');
            const cells = row.querySelectorAll('td');

            const colOrder = [
                1, // 日付 
                2, // 支払先・内容
                3, // 経費科目
                4, // 金額
                null, null, null, null, // 不要箇所
                5, // メモ
                6, // 負担部名
                7  // 負担部コード
            ];

            colOrder.forEach(orderIndex => {
                const newCell = document.createElement('td');
            
                if (orderIndex !== null) {
                    const cell = cells[orderIndex];
                    const input = cell.querySelector('input');
            
                    if (input) {
                        newCell.textContent = input.value.trim();
                    } else {
                        newCell.textContent = cell.textContent.trim();
                    }
                } 
                // orderIndex が null のときは newCell は空のまま追加される
                newRow.appendChild(newCell);
            });
            

            // ▼ 最終列数（18列）に足りない場合は空セルで埋める
            while (newRow.children.length < 18) {
                newRow.appendChild(document.createElement('td'));
            }

            modalBody.appendChild(newRow);
        }
    });
}




function closeModal() {
    // モーダルの画面をOFFにする
    document.getElementById('outputModal').style.display = 'none';
    // モーダル画面のオーバーレイを非表示
    document.getElementById('modalOverlay').style.display = 'none';
}

function selectAllCheckboxes() {
    // "全選択" チェックボックス
    const selectAllCheckbox = document.getElementById("selectAll");

    // "selectedDates" チェックボックスをすべて取得
    const checkboxes = document.querySelectorAll('input[name="selectedDates"]');

    // 全選択チェックボックスの状態に基づいてチェックボックスの状態を設定
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = selectAllCheckbox.checked;
    });
}


function downloadCSV() {
    const headers = [
        "日付", "支払先・内容", "経費科目", "金額", "自社出席代表者名", "自社出席者人数", 
        "他社出席代表者名", "他社出席者人数", "メモ", "費用負担部名", "費用負担部コード", 
        "プロジェクト名", "税区分", "通貨", "為替レート", "貸方勘定科目", "貸方補助科目", "申請番号"
    ];

    const selectedDates = Array.from(document.querySelectorAll('input[name="selectedDates"]:checked'))
        .map(cb => cb.value); // チェックされた日付の配列

    const rows = [];

    const trs = document.querySelectorAll("#outputModal tbody tr");

    trs.forEach(tr => {
        const dateCell = tr.querySelector("td"); // 最初の <td> は日付
        const dateText = dateCell ? dateCell.textContent.trim() : "";

        if (selectedDates.includes(dateText)) {
            const tds = tr.querySelectorAll("td");
            const row = Array.from(tds).map(td => {
                let text = td.textContent.trim().replace(/"/g, '""');
                return text;
            });
            rows.push(row.join(","));
        }
    });

    const csvContent = [
        headers.join(","),
        ...rows
    ].join("\n");

    const blob = new Blob(["\uFEFF" + csvContent], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = "keihi_output.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}
