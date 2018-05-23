# SimpleProceedingsComposer

## 概要
このプログラムは論文リスト(<a href="https://github.com/yuu-nkjm/SimpleProceedingsComposer/blob/master/sample/proceedings.csv">proceedings.csv</a>)をあたえることで，プロシーディングスを作成するプログラムです．

主な機能は以下です．

* 複数のPDFファイルに掲載順にページ番号を書き込みます．
* PDFファイルへのリンクを含むHTML形式の目次を作成します．

### サンプルの実行
1. ``compose-sample.bat``を実行して下さい．
2. ``tmp/``以下にプロシーディングスが格納されます．

### クイックスタート
1. ``sample/proceedings.csv``をコピーして，プロジェクトのルート(``compose.bat``と同じディレクトリ)に置きます．
2. ``proceedings.csv``の2行目以下にプロシーティングスに掲載したい論文情報を**掲載順**に入れます．
3. ``compose.bat proceedings.csv tmp/``を実行します．``tmp/``以下にプロシーディングスが格納されます．

## 使い方
### 設定ファイルの準備
``sample/proceedings.csv``が設定のサンプルファイルです．これをコピーして，プロシーディングスに入れたい論文情報を入力して下さい．

1行目はヘッダ行です．2行目以下にプロシーティングスに掲載したい論文情報を**掲載順**に入れてください．フィールドの値にカンマが含まれる場合はフィールドの値をダブルクォーテーションで囲って下さい．

``filePath``フィールドには，``compose.bat``を起点とした相対パスまたは絶対パスで論文ファイルへのパスを書いて下さい．``title``フィールドには論文タイトルを，``author``フィールドには著者名を入力して下さい．

以下は``sample/proceedings.csv``の中身です．

```
filePath,title,author
sample/pdf/sample01.pdf,"FooBar","yuu_nkjm_1, yuu_nkjm_2"
sample/pdf/sample11.pdf,"HogeHoge","yuu_nkjm_2, yuu_nkjm_3"
sample/pdf/sample08.pdf,"FugaFuga","yuu_nkjm_2, 中島悠"
```

### プロシーディングスの作成
``compose.bat 論文情報ファイル 出力ディレクトリ``として実行して下さい．例えば，``compose.bat sample/proceedings.csv tmp/``のように実行します．

その結果，作成日時に応じたディレクトリが出力ディレクトリ以下に作成され，その中に以下の構造でプロシーディングスが作成されます．

```
proceedings-2014-10-03_22-24-24/
  papres/
    foo.pdf
    bar.pdf
    ...
  proceedings.css
  index.html
```

生成されるHTML形式の目次``index.html``は，以下の様なものです．

```html
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

生成されたプロシーディングスディレクトリの``index.html``や``proceedings.css``の内容は必要に応じて書きかえて下さい．

## スクリーンショット
<img src="http://i.gyazo.com/e3f2ee36890787cbbc7efc29b83a521f.png">

HTML形式の目次が出来ます．

<img src="http://i.gyazo.com/d53093aa2d84be8d65962916d1c8f9a1.png">

PDFに連番のページ番号が入ります．

