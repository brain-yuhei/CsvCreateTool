package com.example.createmail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * メール作成アプリケーションの起動クラス
 * 
 */
@SpringBootApplication
public class CreatemailApplication {
    /**
     * Spring Bootアプリケーションを起動するためのメインメソッド
     * 
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        // Spring Bootのアプリケーションを起動
        SpringApplication.run(CreatemailApplication.class, args);
    }
}

