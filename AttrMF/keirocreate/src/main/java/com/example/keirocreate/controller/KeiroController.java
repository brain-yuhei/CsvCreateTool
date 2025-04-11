package com.example.keirocreate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KeiroController {

    @GetMapping("/")
    public String hello() {

        return "createkeiro"; 
    }    
    
}
