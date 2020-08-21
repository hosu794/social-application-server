package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.User;
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

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Charge.class)
public class PaymentServiceStaticMethodTest {

    UserRepository userRepository;
    PaymentService paymentService;


    Map<String, Object> chargeParams;
    Charge charge = Mockito.mock(Charge.class);
    PaymentRequest paymentRequest;
    User user;
    Instant createdAt;
    UserPrincipal userPrincipal;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Charge.class);
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
    public void should_return_createCharge_method() throws Exception {

        Mockito.when(charge.getId()).thenReturn("23");
        PowerMockito.when(Charge.create(ArgumentMatchers.any(Map.class))).thenReturn(charge);

        Assert.assertEquals(paymentService.createCharge(paymentRequest), charge.getId());

    }


}
