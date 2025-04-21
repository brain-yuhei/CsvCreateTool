package com.example.csvcreate.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.csvcreate.model.MstKeiroEntity;
import com.example.csvcreate.model.WrkKeiroEntity;
import com.example.csvcreate.repository.MstKeiroRepository;
import com.example.csvcreate.repository.WrkKeiroRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class CsvService {

    @Autowired
    private MstKeiroRepository mstkeiroRepository;

    @Autowired
    private WrkKeiroRepository wrkkeiroRepository;

    /**
     * CSVファイルを読込み、DBに保存処理
     * 
     * @param uploadfile CSVファイル
     * @throws IOException
     * @throws CsvValidationException
     */
    public void saveDatabase(MultipartFile uploadfile) throws IOException, CsvValidationException {

        try (CSVReader reader = new CSVReader(new InputStreamReader(uploadfile.getInputStream(), Charset.forName("MS932")))) {
            String[] data;
            boolean firstLine = true;
    
            // CSVファイルを1行づつ読み取り
            while ((data = reader.readNext()) != null) {

                // 最初の行をスキップ
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
    
                // マスタテーブルから「支払先・内容」と「金額」を取り出す
                String payeeContent = data[5];
                BigDecimal amountInclusiveTax = parseBigDecimal(data[23]);
    
                // マスタテーブルにデータがなければ登録する
                MstKeiroEntity mstKeiro = mstkeiroRepository.findByPayeeContentAndAmountInclusiveTax(payeeContent, amountInclusiveTax);
                if (mstKeiro == null) {
                    mstKeiro = new MstKeiroEntity();
                    mstKeiro.setPayeeContent(payeeContent);
                    mstKeiro.setAmountInclusiveTax(amountInclusiveTax);
    
                    if (data.length > 6) mstKeiro.setExpense_category(data[6]);
                    if (data.length > 10) mstKeiro.setMemo(data[10]);
                    if (data.length > 17) mstKeiro.setDepartment_code(data[17]);
                    if (data.length > 18) mstKeiro.setDepartment_name(data[18]);
    
                    mstkeiroRepository.save(mstKeiro);
                }
    
                // ワークテーブルにすでに存在していなければ登録する
                boolean exists = wrkkeiroRepository.existsByPayeeAndAmount(payeeContent, amountInclusiveTax);
                if (!exists) {
                    WrkKeiroEntity wrkKeiro = new WrkKeiroEntity();
                    wrkKeiro.setMstKeiro(mstKeiro);
                    wrkKeiro.setPayee(payeeContent);
                    wrkKeiro.setExpenseCategory(data.length > 6 ? data[6] : null);
                    wrkKeiro.setAmount(amountInclusiveTax);
                    wrkKeiro.setMemo(data.length > 10 ? data[10] : null);
                    wrkKeiro.setDepartmentCode(data.length > 17 ? data[17] : null);
                    wrkKeiro.setDepartmentName(data.length > 18 ? data[18] : null);
    
                    try {
                        wrkkeiroRepository.save(wrkKeiro);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
