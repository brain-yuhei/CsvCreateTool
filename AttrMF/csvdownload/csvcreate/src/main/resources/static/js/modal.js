function openModal() {
    toggleModal(true); // モーダルとオーバーレイを表示
}

function closeModal() {
    toggleModal(false);
}

function toggleModal(show) {
    document.getElementById('outputModal').style.display = show ? 'block' : 'none';
    document.getElementById('modalOverlay').style.display = show ? 'block' : 'none';
}

function downloadCSV() {
    const headers = ["日付", "支払先・内容", "経費科目", "金額", "メモ", "費用負担部門名", "費用負担部門コード"];

    const rows = Array.from(document.querySelectorAll("#outputModal tbody tr"))
        .map(tr => 
            Array.from(tr.querySelectorAll("td"))
                .map(td => `"${td.textContent.trim().replace(/"/g, '""')}"`)
                .join(",")
        );

    const csvContent = [headers.join(","), ...rows].join("\n");
    const blob = new Blob(["\uFEFF" + csvContent], { type: "text/csv;charset=utf-8;" });

    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "keihi_output.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}
