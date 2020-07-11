package com.bookshop.bookshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan(basePackageClasses = {
		BookShopApplication.class
})
public class BookShopApplication  {

	public static void main(String[] args) {
		SpringApplication.run(BookShopApplication.class, args);
	}

}
