package com.bookshop.bookshop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String getHello() {
        return "Hello Everyone!";
    }

    @GetMapping("/twiceHello")
    public String getTwiceHello() {
        return "TwiceHello";
    }

    @GetMapping("/twirdHello")
    public String getThirdeHello() {
        return "TwirdHello";
    }

}
