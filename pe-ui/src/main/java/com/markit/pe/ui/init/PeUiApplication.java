package com.markit.pe.ui.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.markit.pe.ui")
@EnableFeignClients
@EnableDiscoveryClient
public class PeUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeUiApplication.class, args);
	}
}
