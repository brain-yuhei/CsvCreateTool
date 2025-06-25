function openModal() {
    toggleModal(true);

    const selectedDates = getSelectedDates(); // ✅ チェックされている日付のみ取得
    const modalBody = document.getElementById('modalTableBody');
    const warningDiv = document.getElementById('emptyFieldWarning');
    modalBody.innerHTML = ""; // モーダルテーブル初期化
    warningDiv.style.display = "none";

    if (selectedDates.length === 0) {
        warningDiv.style.display = "block";
        warningDiv.textContent = "チェックされた行がありません。";
        return;
    }

    const mainTableRows = document.querySelectorAll('#mainTableBody tr');

    mainTableRows.forEach(row => {
        const checkbox = row.querySelector('input[type="checkbox"][name$=".checked"]');
        if (!checkbox || !checkbox.checked) {
            return; // ✅ チェックされていない行はスキップ
        }

        const date = getDateFromRow(row);
        if (selectedDates.includes(date)) {
            const modalRow = buildModalRow(row);
            modalBody.appendChild(modalRow);
        }
    });
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





  
