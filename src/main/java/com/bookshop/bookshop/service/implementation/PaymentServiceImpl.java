package com.bookshop.bookshop.service.implementation;

import com.bookshop.bookshop.exception.BadRequestException;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.ApiResponse;
import com.bookshop.bookshop.payload.PaymentRequest;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    public PaymentServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;


    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;


    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }


    @Override
    public ResponseEntity<?> chargePremium(PaymentRequest chargeRequest, UserPrincipal currentUser) throws StripeException {

        String chargeId = createCharge(chargeRequest);

        return validateCharge(chargeId, currentUser);

    }

    @Override
    public ResponseEntity<?> validateUser(UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        if(user.isPremium()) {
            throw new BadRequestException("User already has a premium account");
        } else {
            user.setPremium(true);
            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse(true, "Account set to the premium successfully"));
        }
    }

    @Override
    public ResponseEntity<?> validateCharge(String charge, UserPrincipal currentUser) {
        if(charge != null) {


            User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));


            return validateUser(currentUser);


        } else {
            throw new BadRequestException("Please check the credit card details entered");
        }
    }

    @Override
    public String createCharge(PaymentRequest chargeRequest) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", PaymentRequest.Currency.INR);
        chargeParams.put("source", chargeRequest.getToken().getId());

        Charge charge = Charge.create(chargeParams);
        return charge.getId();
    }

}
