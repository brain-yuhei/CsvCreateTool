function openModal() {
    toggleModal(true);

    const selectedDates = getSelectedDates();
    const modalBody = document.getElementById('modalTableBody');
    const warningDiv = document.getElementById('emptyFieldWarning');
    let hasEmptyField = false;

    modalBody.innerHTML = "";

    const dataTable = document.querySelector('.scrollable-table-container table');
    const dataRows = dataTable.querySelectorAll('tbody tr');

    dataRows.forEach(row => {
        const dateText = getDateFromRow(row);

        if (selectedDates.includes(dateText)) {
            const newRow = buildModalRow(row);

            Array.from(newRow.children).forEach(cell => {
                if (cell.textContent.trim() === "") {
                    hasEmptyField = true;
                }
            });

            modalBody.appendChild(newRow);
        }
    });

    if (hasEmptyField) {
        warningDiv.textContent = "空欄の箇所があります。出力してもよろしければ出力ボタンを押してください";
        warningDiv.style.display = 'block';
    } else {
        warningDiv.textContent = "";
        warningDiv.style.display = 'none';
    }
}




function submitPayeeForm() {
    const payee = document.getElementById("selectedPayee").value;
    if (payee === "") {
        alert("経路を選択してください。");
        return;
    }
    document.getElementById("filterForm").submit();
}


// モーダルとオーバーレイを非表示にする
function closeModal() {
    document.getElementById('outputModal').style.display = 'none';
    document.getElementById('modalOverlay').style.display = 'none';
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
    .map(tr => 
        Array.from(tr.querySelectorAll("td"))
            .map(td => `"${td.textContent.trim().replace(/"/g, '""')}"`)
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

// モーダルの背景をクリックした時にモーダルを閉じる処理
window.addEventListener('click', function(event) {
    const modal = document.getElementById('outputModal');
    const overlay = document.getElementById('modalOverlay');
    const checkOutputBtn = document.getElementById('checkOutputBtn');

    const isClickInsideModal = modal.contains(event.target);
    const isClickOnButton = checkOutputBtn.contains(event.target);

    if (overlay.style.display === 'block' && !isClickInsideModal && !isClickOnButton) {
        closeModal();
    }
});

// モーダル内部のクリックでは閉じないようにする
document.getElementById('outputModal').addEventListener('click', function(event) {
    event.stopPropagation(); 
});



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
        // 金額を数値に変換し、小数点2桁で表示
        const formattedAmount = data.amountInclusiveTax !== null
        ? (Math.round(data.amountInclusiveTax * 100) / 100).toFixed(2)
        : '';   

        document.querySelector(`[name="koutsuuhiList[${rowIndex}].expenseCategory"]`).value = data.expense_category || '';
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].amount"]`).value = formattedAmount;
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].memo"]`).value = data.memo || '';
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].departmentName"]`).value = data.department_name || '';
        document.querySelector(`[name="koutsuuhiList[${rowIndex}].departmentCode"]`).value = data.department_code || '';        
    })
    .catch(error => {
        console.error('エラーが発生しました:', error);
    });
}

function removeRequired() {
    document.getElementById('selectedPayee').removeAttribute('required');
}

function addRequired() {
    document.getElementById('selectedPayee').setAttribute('required', 'required');
}




  
