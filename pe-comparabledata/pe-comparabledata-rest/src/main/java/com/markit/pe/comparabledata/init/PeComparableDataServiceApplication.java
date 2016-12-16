package com.markit.pe.comparabledata.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.markit.pe.comparabledata.impl.ComparableDataServiceConfig;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.markit.pe.comparabledata")
@Import(ComparableDataServiceConfig.class)
@RefreshScope
public class PeComparableDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeComparableDataServiceApplication.class, args);
	}
}
