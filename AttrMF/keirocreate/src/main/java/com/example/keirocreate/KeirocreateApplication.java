package com.example.keirocreate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KeirocreateApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeirocreateApplication.class, args);

		//現在時刻のテスト
		//final var now = LocalTime.now();
        //System.out.println(now);
	}

}
