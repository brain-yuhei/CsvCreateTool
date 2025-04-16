package com.example.keirocreate.service;

import com.example.keirocreate.model.MstKeiroEntity;
import com.example.keirocreate.model.WrkKeiroEntity;
import com.example.keirocreate.repository.MstKeiroRepository;
import com.example.keirocreate.repository.WrkKeiroRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

@Service
public class CsvService {

    // マスタテーブル用リポジトリ
    @Autowired
    private MstKeiroRepository mstkeiroRepository;

    // ワークテーブル用リポジトリ
    @Autowired
    private WrkKeiroRepository wrkKeiroRepository;

    /**
     * CSVファイルを読み込み、内容をデータベースに保存する処理
     *
     * @param file アップロードされたCSVファイル
     * @throws IOException 
     * @throws CsvValidationException 
     */
    public void saveCsvToDatabase(MultipartFile file) throws IOException, CsvValidationException {
        // CSVを読み込む
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), Charset.forName("MS932")))) {
            String[] data;
            boolean firstLine = true; // 最初の行をTrueにする

            // 1行ずつ読み取り
            while ((data = reader.readNext()) != null) {

                // 最初の行はTrueなので必ずスキップ
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // 必要な情報をCSVの列から取り出す
                String payeeContent = data[5]; // 支払先・内容
                BigDecimal amountInclusiveTax = parseBigDecimal(data[23]); // 金額（税込）

                // マスタテーブルから支払先・金額が一致するデータを探す
                MstKeiroEntity mstKeiro = mstkeiroRepository.findByPayeeContentAndAmountInclusiveTax(payeeContent, amountInclusiveTax);

                // 一致するマスタデータがなければこの行はスキップ
                if (mstKeiro == null) {
                    continue;
                }

                // ワークテーブルにすでに同じデータ（支払先＋金額）がある場合はスキップ
                boolean exists = wrkKeiroRepository.existsByPayeeAndAmount(payeeContent, amountInclusiveTax);
                if (exists) {
                    continue;
                }

                // WrkKeiroEntityのインスタンスを生成し、必要な項目をセット
                WrkKeiroEntity workKeiro = new WrkKeiroEntity();
                workKeiro.setMstKeiro(mstKeiro); // MstKeiro（マスタ）との関連付け
                workKeiro.setPayee(data[5]); // 支払先・内容
                workKeiro.setExpenseCategory(data[6]); // 経費科目（7列目）
                workKeiro.setAmount(parseBigDecimal(data[23])); // 金額（税込）
                workKeiro.setMemo(data[10]); // メモ（11列目）
                workKeiro.setDepartmentName(data[18]); // 費用負担部名（19列目）
                workKeiro.setDepartmentCode(data[17]); // 費用負担部コード（18列目）

                // ワークテーブルに保存（DBにINSERT）
                try {
                    wrkKeiroRepository.save(workKeiro);
                } catch (Exception e) {
                    // 保存に失敗した場合はエラーログを出力してスキップ
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 文字列を BigDecimal に変換するメソッド
     *
     * @param value 文字列
     * @return 変換後の BigDecimal または null
     */
    private BigDecimal parseBigDecimal(String value) {
        try {
            return (value == null || value.isBlank()) ? null : new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}





