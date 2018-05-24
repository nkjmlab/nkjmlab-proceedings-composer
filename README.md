# SimpleProceedingsComposer

## 概要
このプログラムは論文リスト(<a href="https://github.com/yuu-nkjm/SimpleProceedingsComposer/blob/master/sample-proceedings-src">proceedings.csv</a>)をあたえることで，プロシーディングスを作成するプログラムです．

主な機能は以下です．

* 複数のPDFファイルに掲載順にページ番号を書き込みます．
* PDFファイルへのリンクを含むHTML形式の目次を作成します．

## サンプルの実行
1. ``compose-sample.bat``を実行して下さい．
2. ``sample-proceedings/``以下にプロシーディングスが格納されます．

## クイックスタート
1. ``sample-proceedings-src``をコピーして(e.g. proceedings-src)，プロジェクトのルート(``compose.bat``と同じディレクトリ)に置きます．
2. ``proceedings-src/toc.csv``の2行目以下にプロシーティングスに掲載したい論文情報を**掲載順**に入れます．
3. ``compose.bat proceedings-src/ proceedings/``を実行します．``proceedings/``以下にプロシーディングスが格納されます．

## 実行
コマンドの形式は``compose.bat proceedings-src proceedings``です．第1引数はプロシーディングス・リソースへのパスを，第2引数は出力先を表します．

**出力先にあるデータは上書きされることに注意して下さい．**


## 使い方
### プロシーディングス・リソースの概要
プロシーディングス・リソース以下のファイルは，``toc.csv``を除き全て出力先にコピーされます．``toc.csv``で指定したPDFファイルには，連番のページ番号が書き込まれてコピーされます．``index.html``ファイルには，目次情報が書き込まれまれてコピーされます．同名ファイルがあった場合は上書きされます．

``sample-proceedings-src``がプロシーディングス・リソースのサンプルです．これをコピーして，使って下さい．

#### toc.csv
プロシーディングスに入れたい論文情報を入力して下さい．

1行目はヘッダ行です．2行目以下にプロシーティングスに掲載したい論文情報を**掲載順**に入れてください．フィールドの値にカンマが含まれる場合はフィールドの値をダブルクォーテーションで囲って下さい．

``filePame``フィールドには，第一引数で指定した``proceedings-src``を起点とした相対パスで論文ファイルへのパスを書いて下さい．``title``フィールドには論文タイトルを，``author``フィールドには著者名を入力して下さい．

以下は``toc.csv``のサンプル中身です．

```
filePath,title,author
papers/sample01.pdf,"FooBar","yuu_nkjm_1, yuu_nkjm_2"
papers/sample11.pdf,"HogeHoge","yuu_nkjm_2, yuu_nkjm_3"
papers/sample08.pdf,"FugaFuga","yuu_nkjm_2, 中島悠"
```

#### index.html
目次が挿入されるファイルです．``id``属性の値が``toc``の要素に目次データが挿入されます．

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


## スクリーンショット
<img src="http://i.gyazo.com/e3f2ee36890787cbbc7efc29b83a521f.png">

HTML形式の目次が出来ます．

<img src="http://i.gyazo.com/d53093aa2d84be8d65962916d1c8f9a1.png">

PDFに連番のページ番号が入ります．

