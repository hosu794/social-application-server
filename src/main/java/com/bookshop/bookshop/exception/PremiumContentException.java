package com.bookshop.bookshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class PremiumContentException extends RuntimeException {

    public PremiumContentException(String message) {super(message);}


    public PremiumContentException(String message, Throwable cause) {
        super(message, cause);
    }


}
