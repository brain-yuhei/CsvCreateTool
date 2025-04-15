package com.example.keirocreate.service;

import com.example.keirocreate.model.MstKeiroEntity;
import com.example.keirocreate.model.WrkKeiroEntity;
import com.example.keirocreate.repository.MstKeiroRepository;
import com.example.keirocreate.repository.WrkKeiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

@Service
public class CsvService {

    @Autowired
    private MstKeiroRepository mstkeiroRepository;

    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;

    /**
     * CSVファイルを読み込み、データベースに保存する
     * 
     * @param file CSVファイル
     * @throws IOException
     */
    public void saveCsvToDatabase(MultipartFile file) throws IOException {
        // CSVファイルを読み込む
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("MS932")))) {
            String line;
            boolean firstLine = true; // 1行目をTrue

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // 1行目がTrueなのでスキップ
                    continue;
                }

                // CSVの1行をパース（先頭・末尾の " を除去 → "," で分割）
                String[] data = line.replaceAll("^\"|\"$", "").split("\",\"");

                // MstKeiroテーブルから該当データを取得
                String payeeContent = data[5]; // 支払先・内容
                BigDecimal amountInclusiveTax = parseBigDecimal(data[23]); // 金額（税込）

                MstKeiroEntity mstKeiro = mstkeiroRepository.findByPayeeContentAndAmountInclusiveTax(
                    payeeContent, amountInclusiveTax
                );

                // マスタに一致するデータがない場合はこの行をスキップ
                if (mstKeiro == null) {
                    continue;
                }

                // 同じ支払先・金額のデータがすでにワークテーブルに存在する場合はスキップ
                boolean exists = wrkKeiroRepository.existsByPayeeAndAmount(payeeContent, amountInclusiveTax);
                if (exists) {
                    continue;
                }

                // WrkKeiroEntityの新しいインスタンスを作成し、必要項目を設定
                WrkKeiroEntity workKeiro = new WrkKeiroEntity();
                workKeiro.setMstKeiro(mstKeiro);                      // MstKeiroとの関連
                workKeiro.setPayee(data[5]);                          // 支払先・内容
                workKeiro.setExpenseCategory(data[6]);                // 経費科目
                workKeiro.setAmount(parseBigDecimal(data[23]));       // 金額（税込）
                workKeiro.setMemo(data[10]);                          // メモ
                workKeiro.setDepartmentName(data[18]);                // 費用負担部名
                workKeiro.setDepartmentCode(data[17]);                // 費用負担部コード

                // ワークテーブルに保存
                try {
                    wrkKeiroRepository.save(workKeiro);
                } catch (Exception e) {
                    // 保存に失敗した場合はエラーログを出力
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 文字列をBigDecimal型に変換するメソッド
     * 
     * @param value 文字列
     * @return BigDecimal型の数値 or null
     */
    private BigDecimal parseBigDecimal(String value) {
        try {
            return (value == null || value.isBlank()) ? null : new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null; 
        }
    }
}



