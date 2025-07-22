
# 利用手順書

この手順書はVSCodeを用いてツールを実行するための手順書となります。
既に以下の前提環境が整っている場合はapplication.propertiesの修正のみ行ってください。

## 前提環境

| ソフトウェア | バージョン | 備考 |
|-----|--------|-----|
| VisualStudioCode |最新版推奨|拡張機能追加必須|
| JDK | 21 | JAVA_HOMEの設定必須 |
| MySQL |任意のバージョン|ローカルDB構築用|
| Maven | 3.9.9 | 拡張機能インストール必須 |
| SpringBoot |3.4.5|拡張機能インストール必須|

## セットアップ（開発環境の準備）

### visual studio codeのインストール

1. CSV簡易生成ツールを実行するために以下のサイトをチェックしながらVSCodeのインストールする

- [VSCodeインストール](https://qiita.com/furu38/items/6776acba6621012ee475)

### Java（JDK）のインストール

1. コマンドプロンプトで「java -version」を入力し、インストールされていなければ以下の作業を行ってください。
2. 公式サイトからJDKをダウンロードする。
3. ダウンロード後にファイルパスの設定をする。

- [JDKインストール～ファイルパスの設定まで](https://simpleonedesign.com/blog/java/install_java/)

### MySQLのインストール～DB作成

1. 経路情報を管理するためにMySQLのインストールを以下のサイトをチェックしながら行ってください。
2. DBを作成する。

- [MySQLのインストール](https://qiita.com/taiyang-ks/items/434495a42ae07f27022c)

#### DB作成手順

##### 自動作成

- connect.batファイルをエクスプローラーでダブルクリックすればDB作成（csv_tool_db）を自動で行います。

##### 手動作成

- MySQL8.0 Command Line Clientを起動し自身で設定したPWでログイン

![ログイン画面](./pic/image27.png)

- 任意のDBを作成

![DB作成画面](./pic/image28.png)

- 作成されたDBを確認

![DB確認画面](./pic/image29.png)

### VSCode拡張機能追加

#### 自動追加

- extension.batファイルをエクスプローラーで開きダブルクリックで一括追加が可能です。

#### 手動追加

- 以下の拡張機能を追加してください

- [ ] 「Maven for Java」

- [ ] 「Spring Boot Extension Pack」

- [ ] 「Markdown All in One」

- [ ] 「Markdown Preview Mermaid Support」

- [ ] 「Markdownlint」

### application.propertiesの修正

1. spring.datasource.url=jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC

- [ ] localhost⇒必要に応じてホスト名やIPの変更が必要
- [ ] 3306⇒MySQLのポートが異なる場合は修正。
- [ ] testdb⇒使用するデータベース名を自分の環境に合わせて修正。

1. spring.datasource.username=root
2. spring.datasource.password=〇〇〇〇〇〇

- [ ] root、〇〇〇〇〇〇: ユーザー名・パスワードは自分のMySQLの設定に合わせて変更。

1. server.port=8082

- [ ] port⇒ポート番号がかぶる可能性があるため変更する（例：8181など）

## 利用手順

### アプリ起動時の操作

事前にMysqlが実行されているか確認しましょう。

- MySQL8.0 Command Line Clientを起動し自身で設定したPWでログインし以下の画面が表示されれば実行状態です。

![ターミナル画面](./pic/image27.png)

#### 自動操作

- ran.batファイルをエクスプローラーでダブルクリックすればアプリを自動起動します。

#### 手動操作

- クリーンインストールのためVSCodeのターミナルにて「./mvnw clean install」を実行

![ターミナル画面](./pic/image30.png)

- VSCodeのターミナルにて「./mvnw spring-boot:run」を入力し実行

![ターミナル画面](./pic/image26.png)

### ツール利用前の操作（MF経費側）

#### ブラウザ表示

アプリの実行まで完了したらブラウザ上で[http://localhost:8181/]のように各自で設定したポート番号を入力してください

#### アプリ利用前の事前準備（経路情報の取得）

①：マネーフォワード経費の申請一覧画面から過去に申請した月の詳細ボタンを押下

![申請一覧画面](./pic/image5.png)

②：明細（ＣＳＶ）ボタンを押下

![経費精算画面](./pic/image6.png)

③：ダウンロード履歴を確認しＣＳＶファイルがインポートされていることを確認

![ダウンロード画面](./pic/image7.png)

### ツール利用時の操作

#### 経路の登録手順

①：ＣＳＶファイルの登録画面へをクリック　※すでにＣＳＶファイルを登録済みの場合は不要。

![CSV編集メニュー画面](./pic/image31.png)

②：ファイルの選択をクリック

![CSVファイルアップロード画面](./pic/image9.png)

③：経路情報の取得操作でダウンロードしたＣＳＶファイルを選択し開くをクリック

![エクスプローラー画面](./pic/image10.png)

④：アップロードボタンを押下

![CSVファイルアップロード画面](./pic/image11.png)

⑤：画面右上の選択経路に経路名が入っており選択年月に当月が入っていることを確認　※経路や年月は選択可能

![CSV編集メニュー画面](./pic/image12.png)

#### CSVファイルの作成手順

①：履歴を表示or新規作成を押下

![CSV編集メニュー画面](./pic/image13.png)

##### 新規作成の場合

②：新規画面を表示　※土日祝のチェックが外れているか確認

![CSV管理表画面（新規）](./pic/image15.png)

③：対象月に履歴がある場合は以下のように画面表示される

![CSV新規作成確認画面（新規）](./pic/image32.png)

##### 履歴表示の場合

②：履歴画面を表示　※土日祝のチェックが外れているか確認

![CSV管理表画面（履歴）](./pic/image14.png)

#### CSV内容編集手順

①：支払先・内容のプルダウンで他の経路に変更が可能。　※他の項目が自動反映されているか確認してください。

![CSV管理表画面](./pic/image16.png)

#### CSV出力手順

①：出力内容確認ボタンを押下

![CSV管理表画面](./pic/image17.png)

②：CSVファイルの出力内容に間違いがないかチェックしCSVファイルを出力ボタンを押下

![モーダル画面](./pic/image18.png)

#### 経路編集手順

①：経路登録画面へをクリック

![経路登録画面](./pic/image33.png)

②：経路の一覧表が表示されるので内容を編集しチェックを入れた状態で更新を押下

![経路登録画面](./pic/image34.png)

### アプリ利用後の操作（CSVインポート作業）

①：ダウンロードフォルダに出力したCSVファイルがあることを確認

![エクスプローラー画面](./pic/image19.png)

②：作成月の編集ボタンを押下

![申請一覧画面](./pic/image20.png)

③：インポートボタンを押下

![経費精算画面](./pic/image21.png)

④：ファイルを選択を押下

![インポート画面](./pic/image22.png)

⑤：①で確認したCSVファイルを選択し開くを押下

![エクスプローラー画面](./pic/image23.png)

⑥：CSVファイルが選択されていることを確認し送信ボタンを押下

![インポート画面](./pic/image24.png)

⑦：経費が正しく入力されていれば完了

![経費精算画面](./pic/image25.png)
