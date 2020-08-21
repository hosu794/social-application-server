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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.jws.soap.SOAPBinding;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Charge.class)
public class PaymentServiceTest {

    UserRepository userRepository;
    PaymentService paymentService;


    Map<String, Object> chargeParams;
    Charge charge = Mockito.mock(Charge.class);
    PaymentRequest paymentRequest;
    User user;
    Instant createdAt;
    UserPrincipal userPrincipal;
    ApiResponse apiResponse;


    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Charge.class);
        userRepository = Mockito.mock(UserRepository.class);
        paymentService = new PaymentServiceImpl(userRepository);

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
        user.setPremium(false);
        apiResponse = new ApiResponse(true, "Accout set to the premium successfully");
        Mockito.when(charge.getId()).thenReturn("23");
        PowerMockito.when(Charge.create(ArgumentMatchers.any(Map.class))).thenReturn(charge);


        Mockito.when(userRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
    }


    @Test
    public void should_return_createCharge_method() throws Exception {
        Assert.assertEquals(paymentService.createCharge(paymentRequest), charge.getId());
    }

    @Test
    public void should_return_validateUser() throws Exception {
        Assert.assertEquals(paymentService.validateUser(userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void should_return_validateCharge() throws Exception {
        Assert.assertEquals(paymentService.validateCharge(charge.getId(), userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void should_return_chargePremium() throws Exception {
        Assert.assertEquals(paymentService.chargePremium(paymentRequest, userPrincipal).getStatusCode(), HttpStatus.OK);
    }


}
