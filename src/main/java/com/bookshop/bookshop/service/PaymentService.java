package com.bookshop.bookshop.service;

import com.bookshop.bookshop.payload.PaymentRequest;
import com.stripe.exception.StripeException;

public interface PaymentService {

    public String charge(PaymentRequest chargeRequest) throws StripeException;

}
