package com.example.AestiqueClothing.Policies;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/policies")
public class PolicyController {
    @GetMapping("/privacy")
    public String privacyPolicy() {
        return "policy/privacy-policy";
    }

    @GetMapping("/terms")
    public String termsOfUse() {
        return "terms-of-use";
    }
}