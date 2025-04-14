package com.example.keirocreate.service;

import com.example.keirocreate.model.UserKeiroEntity;
import com.example.keirocreate.repository.KeiroRepository;
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
    private KeiroRepository keiroRepository;

    /**
     * 
     * @param file
     * @throws IOException
     */
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

                UserKeiroEntity keiro = new UserKeiroEntity();

                // 必要な項目
                keiro.setPayee_content(data[5]);                             // 支払先・内容
                keiro.setExpense_category(data[6]);                          // 経費科目
                keiro.setAmount_inclusive_tax(parseBigDecimal(data[23]));    // 金額（税込）
                keiro.setMemo(data[10]);                                     // メモ
                keiro.setDepartment_code(data[17]);                          // 費用負担部門コード
                keiro.setDepartment_name(data[18]);                          // 費用負担部門名

                try {
                    keiroRepository.save(keiro);
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
            System.err.println("Invalid BigDecimal format: " + value);
            return null;
        }
    }
}

