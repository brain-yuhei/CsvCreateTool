# マネーフォワード経費の作業

## 概要

MF勤怠（交通費）のCSV作成を簡略化したい

## 要件

CSVファイルを作成したい（MFテンプレに沿ったもの）

## ユーザーの操作（10分短縮）

※ユーザーが各々、今回作成したプログラムをデプロイする前提

### マネーフォワード経費

1. 経費精算画面で利用している経路のCSVファイルをエクスポート


### CSV作成画面

1. 日付：YYMMの選択のみ切替えてもらう
2. 経路選択：どの経路で申請するかを切り替えてもらう
3. checkbox1：チェックをいれて反映ボタンクリックすると自身の情報がチェックされた日付に入力される
4. checkbox2：チェックをいれてアウトボタンをクリックするとCSVファイルを作成してくれる

### 動作

1. マネーフォワード経費の経路情報をエクスポートしてもらう
2. CSV作成画面に経路のCSVファイルを選択する部分を作成
3. CSV作成画面でY軸が日付でX軸がCSV項目の表を作成
4. 表の上部に経路切替ができる部分を表示する
5. DBからユーザーの経路情報を取得することで経路の切り替えができるようにする
6. チェックされた日付のCSV項目に対して選択された経路の情報を反映させる
7. アウト用のチェックをされた日付のCSV項目をアウトボタン押下でCSVファイルを作成する

### 開発期間

一旦、2週間で完成できるように作業予定。

## 環境

1. 言語：Java.ver17
2. フレーム：Spring boot\
3. DB：SQLite

### CSV作成画面イメージ

| 日付 | CSV反映 | CSV作成 | 支払先 | 経路科目 | 金額 |
| :---: | :---: | :---: | :---: | :---: | :---: |
| 04/01 | ☑ | ☑ | JR東日本  | 電車代  | 160  |
| 04/02 | ☑ | ☑ | JR東日本  | 電車代  | 160  |

## コーディングについて

※以下、試作中なので気にしないでください(o*。_。)oペコッ

src
  main
    java
      com
        example
          keihitemplate
            controller
              KeihiController.java>>>各動作の実行
            model
              UserKeihiEntity.java>>>DB管理
            repository
              UserKeihiRepository.java>>>DBから値を取得
            service
              CsvService.java>>>csvファイルの書き出し
          keihitemplateApplication.java
    resource
      application.properties
      keihi.csv
pom.xml      
