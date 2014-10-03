SimpleProceedingsComposer
=========================
# 実行方法
``resources/proceedings.sample.csv``を``resources/proceedings.csv``にコピーして，プロシーディングスに入れたい論文情報を入力して下さい．

1行目はヘッダ行です．2行目以下にプロシーティングスに掲載したい論文情報を**掲載順**に入れてください．フィールドの値にカンマが含まれる場合はフィールドの値をダブルクォーテーションで囲って下さい．

``fileName``フィールドには，``resources``ディレクトリを起点とした相対パスで論文ファイルへのパスを書いて下さい．``title``フィールドには論文タイトルを，``author``フィールドには著者名を入力して下さい．

以下は``resources/proceedings.csv``のサンプルです．

```csv
fileName,title,author
sample01.pdf,"FooBar","yuu_nkjm_1, yuu_nkjm_2"
sample11.pdf,"HogeHoge","yuu_nkjm_2, yuu_nkjm_3"
sample08.pdf,"FugaFuga","yuu_nkjm_2, yuu_nkjm_4"
```
