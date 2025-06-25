
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