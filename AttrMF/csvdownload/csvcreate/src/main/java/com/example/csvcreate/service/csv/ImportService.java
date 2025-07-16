package com.example.csvcreate.service.csv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class ImportService {
    
    @Autowired
    private MstKeiroRepository mstkeiroRepository;

    @Autowired
    private ValueService valueService;

    /**
     * マスタテーブルの生成・保存処理
     * 
     * @param uploadfile CSVファイル
     * @throws IOException
     * @throws CsvValidationException
     */
    public void saveDatabase(MultipartFile uploadfile) throws IOException, CsvValidationException {

        // CSVファイルを読み込む
        try (CSVReader reader = new CSVReader(new InputStreamReader(uploadfile.getInputStream(), Charset.forName("Shift_JIS")))) {
    
            String[] data;
            boolean firstLine = true;

            // CSVファイルを1行づつ読み取り
            while ((data = reader.readNext()) != null) {

                // 最初の行をスキップ
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // 「支払先・内容」と「金額（税込）」を取り出す
                String payee = data[5];
                BigDecimal amount = valueService.parseBigDecimal(data[23]);

                // 小数点以下切り捨て
                if (amount != null) {
                    amount = new BigDecimal(amount.intValue());
                }

                MstKeiroEntity mstKeiro = null;

                // nullや空欄でなければ重複チェック、それ以外はスキップして保存
                if (payee == null || payee.isBlank()) {
                    continue; // 支払先が無効ならスキップ
                }

                mstKeiro = mstkeiroRepository.findByPayeeContent(payee);

                if (mstKeiro != null) {
                    // 金額が異なる場合のみ更新
                    if (mstKeiro.getAmountInclusiveTax() == null || !mstKeiro.getAmountInclusiveTax().equals(amount)) {
                        mstKeiro.setAmountInclusiveTax(amount);
                    }
                
                    // 他の項目は常に上書き（必要に応じて比較してもOK）
                    mstKeiro.setExpense_category(data.length > 6 ? data[6] : null);
                    mstKeiro.setMemo(data.length > 10 ? data[10] : null);
                    mstKeiro.setDepartment_code(data.length > 17 ? data[17] : null);
                    mstKeiro.setDepartment_name(data.length > 18 ? data[18] : null);
                
                } else {
                    // 新規登録
                    mstKeiro = new MstKeiroEntity();
                    mstKeiro.setPayeeContent(payee);
                    mstKeiro.setAmountInclusiveTax(amount);
                    mstKeiro.setExpense_category(data.length > 6 ? data[6] : null);
                    mstKeiro.setMemo(data.length > 10 ? data[10] : null);
                    mstKeiro.setDepartment_code(data.length > 17 ? data[17] : null);
                    mstKeiro.setDepartment_name(data.length > 18 ? data[18] : null);
                }
                

                // マスタテーブルを保存
                mstkeiroRepository.save(mstKeiro);
            }
        }
    }

}
