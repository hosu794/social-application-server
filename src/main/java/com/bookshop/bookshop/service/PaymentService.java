package com.bookshop.bookshop.service;

import com.bookshop.bookshop.payload.PaymentRequest;
import com.bookshop.bookshop.security.UserPrincipal;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;

public interface PaymentService {



    public ResponseEntity<?> chargePremium(PaymentRequest chargeRequest, UserPrincipal currentUser) throws StripeException;

    public ResponseEntity<?> validateUser(UserPrincipal currentUser);

    public ResponseEntity<?> validateCharge(String charge, UserPrincipal currentUser);

    public String createCharge(PaymentRequest chargeRequest) throws StripeException;

}
