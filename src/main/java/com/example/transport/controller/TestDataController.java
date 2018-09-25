package com.example.transport.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class TestDataController {

    @RequestMapping("gantt")
    public String gantt(){
        return "gantt";
    }

    @RequestMapping("supergantt")
    public String supergantt(){
        return "test/gantt";
    }
}
