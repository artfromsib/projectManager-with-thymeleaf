package com.ym.projectManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping({ "/", "/main" })

public class MainController {

    @GetMapping
    public String main(Model model) {
        return "main/main";
    }

}
