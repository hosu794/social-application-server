package com.bookshop.bookshop.util;

import com.bookshop.bookshop.exception.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ValidatePageUtilTest {



    @Test()
    void should_validatePageNumberAndSize() {

        Assertions.assertThrows(BadRequestException.class, () -> {
            ValidatePageUtil.validatePageNumberAndSize(-12, 10);
        });

        Assertions.assertThrows(BadRequestException.class, () -> {
            ValidatePageUtil.validatePageNumberAndSize(2, 60);
        });

    }
}