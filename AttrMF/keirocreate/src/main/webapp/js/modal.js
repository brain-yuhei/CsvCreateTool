
function openModal() {
    // モーダルの画面をONにする
    document.getElementById('outputModal').style.display = 'block';
    // モーダル画面のオーバーレイを表示
    document.getElementById('modalOverlay').style.display = 'block';
}

function closeModal() {
    // モーダルの画面をOFFにする
    document.getElementById('outputModal').style.display = 'none';
    // モーダル画面のオーバーレイを非表示
    document.getElementById('modalOverlay').style.display = 'none';
}

function downloadCSV() {
    // CSVの1行目の項目を定義
    const headers = [
        "日付", "支払先・内容", "経費科目", "金額", "自社出席代表者名", "自社出席者人数", 
        "他社出席代表者名", "他社出席者人数", "メモ", "費用負担部名", "費用負担部コード", 
        "プロジェクト名", "税区分", "通貨", "為替レート", "貸方勘定科目", "貸方補助科目", "申請番号"
    ];

    // 2行目以降のデータを格納する配列
    const rows = [];

    // モーダル内のテーブル行をすべて取得
    const trs = document.querySelectorAll("#outputModal tbody tr");

    // 各行ごとに処理
    trs.forEach(tr => {
        // 各セルを取得
        const tds = tr.querySelectorAll("td");

        // セルの中のテキストを取得し、配列に格納
        const row = Array.from(tds).map(td => {
            // セルのテキストをトリムして、ダブルクォートは""にエスケープ（CSV仕様）
            let text = td.textContent.trim().replace(/"/g, '""');
            return text;
        });

        // 1行分をカンマ区切りに変換してrows配列に追加
        rows.push(row.join(","));
    });

    // 最終的なCSV文字列を生成
    const csvContent = [
        headers.join(","), // ヘッダー行
        ...rows            // データ行
    ].join("\n");          // 行は改行コードで区切る

    // Blobオブジェクトを生成
    const blob = new Blob(["\uFEFF" + csvContent], { type: "text/csv;charset=utf-8;" });

    // 一時的なダウンロード用URLを作成
    const url = URL.createObjectURL(blob);

    // 仮のリンク要素を作成して自動的にクリック（＝CSVをダウンロード）
    const link = document.createElement("a");
    link.href = url;
    link.download = "keihi_output.csv"; // ダウンロード時のファイル名
    document.body.appendChild(link);
    link.click();

    // 作成したリンク要素を削除
    document.body.removeChild(link);
}


