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

    public void saveCsvToDatabase(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("MS932")))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] data = line.replaceAll("^\"|\"$", "").split("\",\"");

                // MST_KEIROテーブルのデータを取得
                String payeeContent = data[5]; // 支払先・内容
                BigDecimal amountInclusiveTax = parseBigDecimal(data[23]); // 金額（税込）
                MstKeiroEntity mstKeiro = mstkeiroRepository.findByPayeeContentAndAmountInclusiveTax(
                    payeeContent, amountInclusiveTax);

                if (mstKeiro == null) {
                    continue; // マスタデータが存在しない場合はスキップ
                }

                // 重複を避けるため、既に同じデータがワークテーブルに存在するかを確認
                boolean exists = wrkKeiroRepository.existsByPayeeAndAmount(payeeContent, amountInclusiveTax);
                if (exists) {
                    continue; // 既に存在する場合はスキップ
                }                

                // WorkKeiroEntityを作成
                WrkKeiroEntity workKeiro = new WrkKeiroEntity();
                workKeiro.setMstKeiro(mstKeiro); // MST_KEIROとの関連付け
                workKeiro.setPayee(data[5]); // 支払先・内容
                workKeiro.setExpenseCategory(data[6]); // 経費科目
                workKeiro.setAmount(parseBigDecimal(data[23])); // 金額（税込）
                workKeiro.setMemo(data[10]); // メモ
                workKeiro.setDepartmentName(data[18]); // 費用負担部名
                workKeiro.setDepartmentCode(data[17]); // 費用負担部コード

                // ワークテーブルに保存
                try {
                    wrkKeiroRepository.save(workKeiro);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return (value == null || value.isBlank()) ? null : new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}


