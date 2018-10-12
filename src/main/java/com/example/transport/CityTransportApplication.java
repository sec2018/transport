package com.example.transport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@SpringBootApplication
//public class CityTransportApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(CityTransportApplication.class, args);
//	}
//}

//for war
@EnableCaching
@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
public class CityTransportApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(CityTransportApplication.class);
	}
	public static void main( String[] args ){
		SpringApplication.run(CityTransportApplication.class, args);
	}
}