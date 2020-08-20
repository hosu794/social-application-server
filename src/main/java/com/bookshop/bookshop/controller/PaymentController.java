package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.payload.PaymentRequest;
import com.bookshop.bookshop.service.implementation.PaymentServiceImpl;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    private final PaymentServiceImpl paymentService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> completePayment(@RequestBody PaymentRequest request) throws StripeException {

        System.out.println(request.getAmount());

        String chargeId= paymentService.charge(request);
        return chargeId!=null? new ResponseEntity<String>(chargeId,HttpStatus.OK):
                new ResponseEntity<String>("Please check the credit card details entered", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public String handleError(StripeException ex) {
        return ex.getMessage();
    }

}
