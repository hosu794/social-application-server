package com.bookshop.bookshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;


@SpringBootApplication
@EntityScan(basePackageClasses = {
		BookShopApplication.class, Jsr310JpaConverters.class
})

public class BookShopApplication  {

	public static void main(String[] args) {
		SpringApplication.run(BookShopApplication.class, args);
	}

}
