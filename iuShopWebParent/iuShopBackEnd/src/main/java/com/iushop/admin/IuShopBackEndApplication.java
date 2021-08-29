package com.iushop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.iushop.common.entity","com.iushop.admin.user"})
public class IuShopBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(IuShopBackEndApplication.class, args);
	}

}
