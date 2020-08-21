package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.ApiResponse;
import com.bookshop.bookshop.payload.PaymentRequest;
import com.bookshop.bookshop.payload.Token;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.PaymentService;
import com.bookshop.bookshop.service.implementation.PaymentServiceImpl;
import com.stripe.model.Charge;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentServiceTest {

    UserRepository userRepository;
    PaymentService paymentService;

    Map<String, Object> chargeParams;
    PaymentRequest paymentRequest;
    User user;
    Instant createdAt;
    UserPrincipal userPrincipal;



    @Before
    public void setUp() throws Exception {

        paymentService = new PaymentServiceImpl(userRepository);
        userRepository = Mockito.mock(UserRepository.class);
        chargeParams = new HashMap<>();
        paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(23);
        Token token = new Token();
        token.setId("2323232332");
        paymentRequest.setToken(token);
        createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();

        user = new User();
        user.setId(12l);
        user.setUsername("hosu794");
        user.setPassword("password");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("hosu794@gmail.com");
        user.setCreatedAt(createdAt);
        userPrincipal = UserPrincipal.create(user);
        user.setUpdatedAt(createdAt);
        user.setPremium(true);
    }

    @Test
    public void should_return_chargePremium_method() throws Exception {
        ApiResponse apiResponse = new ApiResponse(true, "Account to premium successfully");
        ResponseEntity<?> responseEntity = ResponseEntity.ok(new ApiResponse(true, "Account set to the premium successfully"));

        Mockito.when(paymentService.createCharge(ArgumentMatchers.any(PaymentRequest.class))).thenReturn("23");
//        Mockito.when(paymentService.validateCharge(ArgumentMatchers.anyString(), ArgumentMatchers.any(UserPrincipal.class))).thenReturn(responseEntity);
    }



}
