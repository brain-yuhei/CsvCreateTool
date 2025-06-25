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





  
