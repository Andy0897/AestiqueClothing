package com.example.AestiqueClothing.Payments;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    public String chargeCreditCard(String token, double amount) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int)amount);
        chargeParams.put("currency", "bgn");
        chargeParams.put("source", token);
        chargeParams.put("description", "Professional Payment Processing");

        Charge charge = Charge.create(chargeParams);
        return charge.getId();
    }
}

