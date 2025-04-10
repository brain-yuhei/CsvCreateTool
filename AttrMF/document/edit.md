
<style>

h2 {
  position: relative;
  padding: 10px 10px 10px 20px;
  font-weight: normal !important;
  background: white;
}

h2::before {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  width: 8px;
  height: 100%;
  background: linear-gradient(to bottom, #888 0%, #000 100%);
  border-radius: 5px 5px 0px;
  opacity: 0.4;
}
h2::after {
    content: "";
    position: absolute;
    left: 0;
    top: 50px;
    width: 100%;
    height: 3px;
    background: linear-gradient(to left, #888 0%,#000 100%);
    opacity: 0.4;
}
h3 {
    font-style: italic;
    text-decoration: underline;
    margin-left: 10px;
}
</style>

# 基本詳細設計書

1. プロジェクト名：CSVファイル自動生成
2. 作成日：YYYY/MM/DD
3. 作成者：橋本悠平

## システム概要

### 目的

CSV編集用のモーダルを表示する

### 主な機能

1. CSVファイル簡易作成ツール画面の編集ボタン押下時にCSV編集用モーダル画面を表示する。

2. 編集されたデータをワークテーブルに保存する。

### 対象ユーザー

同一の経路で出勤することが多い従業員

### 開発環境

1. 言語：Java（version17）
2. フレームワーク：Spring boot
3. DB：SQLite

## 画面設計

### 編集画面

![編集画面](../Picture/image2.png)

## 機能仕様

### データ表示

当画面表示の際にWRK_KEIROから該当日のデータを取得し
表示する

### キャンセルボタン押下

CSVファイル簡易作成ツール画面へ戻る

### 更新ボタン押下

編集データをWRK_KEIROの該当箇所に保存する

## 機能詳細

### データ表示：詳細

当画面表示の際にWRK_KEIROから該当日のデータを取得し
表示する

- [ ] 入力なし：自動入力

- [ ] 出力あり：該当日のWRK_KEIROデータ

#### データ表示：実装方法

- コントローラークラス
  - データ取得処理を呼び出す
- サービスクラス
  - 該当日のWRK_KEIROデータ取得処理を行う

### キャンセルボタン押下：詳細

CSVファイル簡易作成ツール画面へ戻る

- [ ] 入力なし：CSVファイル簡易作成ツール画面に戻る

- [ ] 出力なし：CSVファイル簡易作成ツール画面に戻る

#### キャンセルボタン押下：実装方法

- コントローラークラス
  - CSVファイル簡易作成ツール画面へのリダイレクト処理

### 更新ボタン押下：詳細

編集データをワークテーブルの該当箇所に保存する

- [ ] 入力あり：ユーザーが入力した編集データ

- [ ] 出力あり：該当日の編集データをWRK_KEIROに保存

#### 更新ボタン押下：実装方法

- コントローラークラス
  - データ保存処理を呼び出す
- サービスクラス
  - WRK_KEIROへ該当日のデータ保存処理を行う
