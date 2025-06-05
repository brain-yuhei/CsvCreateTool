
# 利用手順書

## 前提環境

| ソフトウェア | バージョン | 備考 |
|-----|--------|-----|
| Java | 17 | ー |
| JDK | 21 | JAVA_HOMEの設定必須 |
| Maven | 3.9.9 | ー |
| SpringBoot |ー|ー|

## 起動までの手順

### visual studio codeのインストール

1. CSV簡易生成ツールを実行するために以下のサイトをチェックしながらVSCodeのインストールを行いましょう

- [VSCodeインストール](https://qiita.com/furu38/items/6776acba6621012ee475)

### Java（JDK）のインストール

1. まずはJDKがインストールされているか以下のサイトからチェックしましょう

- [ ]  [JDKバージョン確認](https://qiita.com/musica_gatto/items/0f54d54cf27e33af010a)

1. インストールされていなければ以下の公式サイトからJDKをダウンロードしてください。
2. ダウンロード後にはファイルパスの設定も必要ですので実施しましょう。

- [JDKインストール～ファイルパスの設定まで](https://simpleonedesign.com/blog/java/install_java/)

### VSCode拡張機能追加

- [ ] 「Maven for Java」を追加してください

- [ ] 「Spring Boot Extension Pack」を追加してください

### MySQLのインストール

- [ ] 経路情報を管理するためにMySQLのインストールを以下のサイトをチェックしながら行いましょう

- [MySQLのインストール](https://qiita.com/taiyang-ks/items/434495a42ae07f27022c)

### application.propertiesの修正

1. spring.datasource.url=jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC

- [ ] ・localhost⇒必要に応じてホスト名やIPの変更が必要
- [ ] ・3306⇒MySQLのポートが異なる場合は修正。
- [ ] ・testdb⇒使用するデータベース名を自分の環境に合わせて修正。

1. spring.datasource.username=root
2. spring.datasource.password=yuhei0129H2349

- [ ] ・root、yuhei0129H2349: ユーザー名・パスワードは自分のMySQLの設定に合わせて変更。

## 利用手順

### マネーフォワードクラウド経費側の操作（ツール実行前）

①：マネーフォワード経費の申請一覧画面から過去に申請した月の詳細ボタンを押下

![申請一覧画面](./pic/image5.png)

②：明細（ＣＳＶ）ボタンを押下

![経費精算画面](./pic/image6.png)

③：ダウンロード履歴を確認しＣＳＶファイルがインポートされていることを確認

![ダウンロード画面](./pic/image7.png)

### CSVファイル簡易作成ツール側の操作

④：画面上部、ＣＳＶファイルの登録画面へをクリック　※すでにＣＳＶファイルを登録済みの場合は不要。

![CSV編集メニュー画面](./pic/image8.png)

⑤：ファイルの選択をクリック

![CSVファイルアップロード画面](./pic/image9.png)

⑥：③で確認したＣＳＶファイルを選択し開くをクリック

![エクスプローラー画面](./pic/image10.png)

⑦：アップロードボタンを押下

![CSVファイルアップロード画面](./pic/image11.png)

⑧：画面右上の選択経路に経路名が入っており選択年月に当月が入っていることを確認　※経路や年月は選択可能

![CSV編集メニュー画面](./pic/image12.png)

⑨：履歴を表示or新規作成を押下

![CSV編集メニュー画面](./pic/image13.png)

⑩：履歴画面を表示　※土日祝のチェックが外れているか確認

![CSV管理表画面（履歴）](./pic/image14.png)

⑪：新規画面を表示　※土日祝のチェックが外れているか確認

![CSV管理表画面（新規）](./pic/image15.png)

⑫：支払先・内容のプルダウンで他の経路に変更が可能。　※他の項目が自動反映されているか確認してください。

![CSV管理表画面](./pic/image16.png)

⑬：出力内容確認ボタンを押下

![CSV管理表画面](./pic/image17.png)

⑭：CSVファイルの出力内容に間違いがないかチェックしCSVファイルを出力ボタンを押下

![モーダル画面](./pic/image18.png)

### マネーフォワードクラウド経費側の操作（ツール実行後）

⑮：ダウンロードフォルダに出力したCSVファイルがあることを確認

![エクスプローラー画面](./pic/image19.png)

⑯：作成月の編集ボタンを押下

![申請一覧画面](./pic/image20.png)

⑰：インポートボタンを押下

![経費精算画面](./pic/image21.png)

⑱：ファイルを選択を押下

![インポート画面](./pic/image22.png)

⑲：⑮で確認したCSVファイルを選択し開くを押下

![エクスプローラー画面](./pic/image23.png)

⑳：CSVファイルが選択されていることを確認し送信ボタンを押下

![インポート画面](./pic/image24.png)

㉑：経費が正しく入力されていれば完了

![経費精算画面](./pic/image25.png)
