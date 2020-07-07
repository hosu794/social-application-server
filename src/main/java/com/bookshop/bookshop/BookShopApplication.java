package com.bookshop.bookshop;

import com.bookshop.bookshop.service.FilesStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.annotation.Resource;


@SpringBootApplication
@EntityScan(basePackageClasses = {
		BookShopApplication.class
})
public class BookShopApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BookShopApplication.class, args);
	}

	@Resource
	FilesStorageService storageService;

	@Override
	public void run(String... args) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}
}
