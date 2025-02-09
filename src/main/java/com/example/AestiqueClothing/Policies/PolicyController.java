package com.example.AestiqueClothing.Policies;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/policies")
public class PolicyController {
    @GetMapping("/privacy-policy")
    public String getPrivacyPolicy() {
        return "policy/privacy-policy";
    }

    @GetMapping("/terms-conditions")
    public String getTermsAndConditions() {
        return "policy/terms-conditions";
    }

    @GetMapping("/return-policy")
    public String getReturnPolicy() {
        return "policy/return-policy";
    }

    @GetMapping("/faqs")
    public String getFAQsPolicy() {
        return "policy/faqs";
    }
}