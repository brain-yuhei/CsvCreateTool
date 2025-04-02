```mermaid
gantt

title マネーフォワード経費の簡略化作業
dateFormat  YY-MM-DD
axisFormat  %m-%d  

section 企画
  企画開始                      : milestone,active,  des1, 2025-04-02,1d
  仕様決定                      : des2, after des1, 1d

section 設計
  基本設計書作成                : des3, after des2, 2d
    経路登録画面                : des3, after des2,1d
    CSV作成画面                 : des4, after des3,1d
  詳細設計書作成                : des5, after des4, 2d
    経路登録画面                : des5, after des4,1d
    CSV作成画面                 : des6, after des5, 1d

section 開発準備
  環境構築                      : des7, after des6, 1d 
    Java(Version17)            : des7, after des6, 1d
    Spring boot                : des7, after des6, 1d
    DB(MySQL)                  : des7, after des6, 1d
    
section フロントエンド開発
  経路登録画面                  : des8, after des7, 1d
  CSV作成画面                   : des9, after des8, 1d
    
section バックエンド開発  
  経路情報をDB保存              : des10, after des9, 1d
  経路切替機能追加              : des11, after des10, 1d
  経路反映機能追加              : des12, after des11, 1d
  CSVファイル作成機能追加       : des13, after des12, 1d

section 予備日
  予備                         : des14, after des13, 1d 

section 実装 
  リリース                     : milestone, des15, 2025-04-16, 1d