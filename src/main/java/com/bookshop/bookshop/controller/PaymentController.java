package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.payload.PaymentRequest;
import com.bookshop.bookshop.security.CurrentUser;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.implementation.PaymentServiceImpl;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    private final PaymentServiceImpl paymentService;


    @PostMapping("/premium")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> chargePremium(@RequestBody PaymentRequest request, @CurrentUser UserPrincipal currentUser) throws StripeException {
        System.out.println(request.getAmount());
        return paymentService.chargePremium(request, currentUser);
    }


    @ExceptionHandler
    public String handleError(StripeException ex) {
        return ex.getMessage();
    }

}
