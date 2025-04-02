package com.example.createmail.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvService {


    /**
     * CSVファイル "emails.csv" からメールアドレスを読み取るメソッド（読み取り専用）
     */
    public List<String> readAddressesFromCsv() throws IOException {
        ClassPathResource resource = new ClassPathResource("emails.csv");

        //CSVファイルを読み込む
        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            //CSVファイルの全データをリストとして取得する
            List<String[]> records = csvReader.readAll();
            return records.stream()
                    .map(record -> record[0]) // 最初の列（メールアドレス）を取得
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * メールアドレスをCSVファイルに書き込む
     * 
     * @param email メールアドレス
     */
    public void writeToCsv(String email) {
        try {
            // CSVファイルにアクセス
            File file = new File("src/main/resources/emails.csv");

            // 追記モードでファイルを開く
            try (FileWriter fileWriter = new FileWriter(file, true);
                 CSVWriter csvWriter = new CSVWriter(fileWriter)) {

                // メールアドレスをCSVに追加
                String[] data = {email};
                csvWriter.writeNext(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





