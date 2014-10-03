# SimpleProceedingsComposerの利用

## 概要
このプログラムは論文リスト(<a href="https://github.com/yuu-nkjm/SimpleProceedingsComposer/blob/master/resources/proceedings.sample.csv">proceedings.sample.csv</a>)をあたえることで，プロシーディングス(<a href="https://github.com/yuu-nkjm/SimpleProceedingsComposer/blob/master/proceedings-sample.zip">proceedings-sample.zip</a>)を作成するプログラムです．

主な機能は以下です．

* 複数のPDFファイルに掲載順にページ番号を書き込む
* PDFファイルへのリンクを含むHTML形式の目次を作成する．

## 使い方
### 設定ファイルの準備
``resources/proceedings.sample.csv``を``resources/proceedings.csv``にコピーして，プロシーディングスに入れたい論文情報を入力して下さい．

1行目はヘッダ行です．2行目以下にプロシーティングスに掲載したい論文情報を**掲載順**に入れてください．フィールドの値にカンマが含まれる場合はフィールドの値をダブルクォーテーションで囲って下さい．

``fileName``フィールドには，``resources``ディレクトリを起点とした相対パスで論文ファイルへのパスを書いて下さい．``title``フィールドには論文タイトルを，``author``フィールドには著者名を入力して下さい．

以下は``resources/proceedings.csv``のサンプルです．

```
fileName,title,author
sample01.pdf,"FooBar","yuu_nkjm_1, yuu_nkjm_2"
sample11.pdf,"HogeHoge","yuu_nkjm_2, yuu_nkjm_3"
sample08.pdf,"FugaFuga","yuu_nkjm_2, yuu_nkjm_4"
```

### プロシーディングスの作成
``compose.bat``を実行して下さい．``proceedings``ディレクトリ以下に作成日時に応じたディレクトリが作成され，その中に以下の構造でプロシーディングスが作成されます．

```
papres/
  foo.pdf
  bar.pdf
  ...
proceedings.css
index.html
```

``index.html``は，以下の様なものです．

```html
<link rel='stylesheet' href='proceedings.css' />
<div class='paper'>
  <div class='title'><a href='papers/sample01.pdf'>FooBar</a></div>
  <div class='author'>yuu_nkjm_1, yuu_nkjm_2</div>
  <div class='start_page'>1</div>
</div>
<div class='paper'>
  <div class='title'><a href='papers/sample11.pdf'>HogeHoge</a></div>
  <div class='author'>yuu_nkjm_2, yuu_nkjm_3</div>
  <div class='start_page'>3</div>
</div>
<div class='paper'>
  <div class='title'><a href='papers/sample08.pdf'>FugaFuga</a></div>
  <div class='author'>yuu_nkjm_2, yuu_nkjm_4</div>
  <div class='start_page'>7</div>
</div>
```

``proceedings.css``の内容は必要に応じて書きかえて下さい．
