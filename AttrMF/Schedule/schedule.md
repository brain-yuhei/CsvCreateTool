```mermaid
gantt

title マネーフォワード経費の簡略化作業
dateFormat  YY-MM-DD
axisFormat  %m-%d  

section 企画
  企画開始                      : milestone,active,  des1, 2025-04-02,1d
  仕様決定                      : des1-1, after des1, 1d

section 設計
  基本詳細設計書作成             : des2, after des1-1, 4d
    設計完了                    : milestone,

section 開発準備
  環境構築                      : des3, after des2, 1d 
    Java(Version17)            : des3-1, after des2, 1d
    Spring boot                : des3-2, after des2, 1d
    DB(SQLite)                 : des3-3, after des2, 1d
    環境構築完了                : milestone,
    
section フロントエンド開発
  CSV作成画面                   : des4-2, after des3-3, 1d
  フロントエンド開発完了         : milestone,
    
section バックエンド開発  
  CSVからを必要箇所のみDB保存    : des5-1, after des4-2, 1d
  CSV管理表の作成               : des5-2, after des4-2, 1d
  CSVファイル出力機能追加       : des5-3, after des5-2, 1d
  モーダル機能追加              : des5-4, after des5-2, 1d
  バックエンド開発完了          : milestone,

section テスト
  テスト                       : des6, after des5-4, 3d 
  テスト完了                   : milestone,

section リリース 
  リリース                     : milestone, des7, 2025-04-16, 1d

section 休み（非稼働日）
  休日 (土)                    : des8, 2025-04-05, 1d
  休日 (日)                    : des9, 2025-04-06, 1d
  休日 (土)                    : des10, 2025-04-12, 1d
  休日 (日)                    : des11, 2025-04-13, 1d
