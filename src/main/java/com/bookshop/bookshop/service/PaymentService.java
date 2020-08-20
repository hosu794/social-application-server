package com.bookshop.bookshop.service;

import com.bookshop.bookshop.payload.PaymentRequest;
import com.bookshop.bookshop.security.UserPrincipal;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;

public interface PaymentService {

    public String charge(PaymentRequest chargeRequest) throws StripeException;

    public ResponseEntity<?> chargePremium(PaymentRequest chargeRequest, UserPrincipal currentUser) throws StripeException;

}
