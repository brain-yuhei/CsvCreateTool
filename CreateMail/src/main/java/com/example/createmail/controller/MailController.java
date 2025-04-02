package com.example.createmail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.createmail.model.UserMailEntity;
import com.example.createmail.repository.UserMailRepository;
import com.example.createmail.service.CsvService;
import com.example.createmail.service.UserMailService;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;


/**
 * メールのコントローラークラス
 */
@Controller 
public class MailController {

    // 自動的に CsvService のインスタンスが注入される
    @Autowired
    private CsvService csvService; 

    // 自動的に UserMailService のインスタンスが注入される
    @Autowired
    private UserMailService usermailService;

    // 自動的に UserMailRepository のインスタンスが注入される 
    @Autowired 
    private UserMailRepository userMailRepository;

    // 自動的に JavaMailSender のインスタンスが注入される
    @Autowired
    private JavaMailSender javaMailSender;


    /**
     * CSVからメールアドレスを読み込み、メール作成画面を表示する
     * 
     * @param model メールアドレスリストを格納
     * @return メール作成画面（createmail.jsp）
     */
    @GetMapping("/")
    public String hello(Model model) {
        try {
            // CSVからメールアドレスリストを取得
            List<String> emailList = csvService.readAddressesFromCsv();
            model.addAttribute("emailList", emailList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "createmail"; 
    }

    /**
     * メールデータをDBに保存し、一覧画面を表示する
     * 
     * @param address メールアドレス
     * @param subject 件名
     * @param message 本文
     * @return 一覧表示画面へのリダイレクト（viewmails.jsp）
     */
    @PostMapping("/sendMail")
    public String sendMail(@RequestParam("address") String address, 
                           @RequestParam("subject") String subject, 
                           @RequestParam("message") String message) {

        // メールデータをデータベースに保存メソッド呼び出し
        usermailService.saveRepository(address, subject, message);
        return "redirect:/viewmails"; 
    }   

    /**
     * 送信済み・未送信のメールを一覧画面に表示する
     * 
     * @param model 送信済み・未送信のメールリストを格納
     * @param sortBy ソート条件
     * @param status 送信の有無
     * @return 一覧表示画面（viewmails.jsp）
     */
    @GetMapping("/viewmails")
    public String viewRecipients(@RequestParam(value = "sortBy", required = false, defaultValue = "email") String sortBy,
                                 @RequestParam(value = "status", required = false, defaultValue = "all") String status, 
                                 Model model) {
        
        // 送信済みと未送信のメールリストを取得
        List<UserMailEntity> allRepository = usermailService.getAllMails(); // 両方を取得
        // ソート処理
        if ("subject".equals(sortBy)) {
            allRepository.sort(Comparator.comparing(UserMailEntity::getSubject));
        } else if ("date".equals(sortBy)) {
            allRepository.sort(Comparator.comparing(UserMailEntity::getSendDate));
        } else {
            allRepository.sort(Comparator.comparing(UserMailEntity::getEmail));
        }
        
        // "status" によるフィルタリング
        if ("sent".equals(status)) {
            allRepository.removeIf(repository -> !repository.isSent()); // 送信済みのものだけ表示
        } else if ("unsent".equals(status)) {
            allRepository.removeIf(repository -> repository.isSent()); // 未送信のものだけ表示
        }
    
        model.addAttribute("repository", allRepository); // 一つのリストとして渡す
        return "viewmails"; 
    }

    /**
     * 指定されたメールを送信済みに変更し、一覧画面を更新する
     * 
     * @param selectedIds 送信対象のメールID
     * @return 一覧表示画面へのリダイレクト（viewmails.jsp）
     */
    @PostMapping("/sendSelectedMails")
    public String sendSelectedMails(@RequestParam("selectedIds") List<Long> selectedIds) {

        // 選択されたIDのメールを送信済みに変更
        for (Long id : selectedIds) {

            //ここにメールデータを取得する部分を書く
            UserMailEntity repository = userMailRepository.findById(id).orElseThrow();
            try{

                //オブジェクト作る
                MimeMessage msg = javaMailSender.createMimeMessage();
			    MimeMessageHelper helper = new MimeMessageHelper(msg, true);

                helper.setFrom("yuhei23491518@gmail.com");
                helper.setTo(repository.getEmail());
                helper.setSubject(repository.getSubject());
                helper.setText(repository.getMessage());

                //送信する
                javaMailSender.send(msg);
                //成功
                System.out.println("メール送信成功！");

                usermailService.markAsSent(id);
            }catch(MessagingException e){
                //失敗
                System.err.println("メール送信失敗！" + e );
            }
               
        }
        return "redirect:/viewmails"; // 送信後に一覧画面にリダイレクト
    } 

    /**
     * メールアドレスをCSVファイルに追加する
     * 
     * @param email メールアドレス
     * @return メール作成画面へのリダイレクト（createmail.jsp）
     */
    @PostMapping("/sendaddress")
    public String sendAddress(@RequestParam("email") String email) {
        // CSVに書き込む
        csvService.writeToCsv(email);
        return "redirect:/"; 
    }    

}

