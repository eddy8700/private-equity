package com.markit.pe.exception.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * @author Aditya Gupta
 *
 */

@EnableJpaRepositories
@EnableJpaAuditing
@Configuration
@ComponentScan
@EntityScan(basePackages = {"com.markit.pe.exception.domain","com.markit.pe.positiondata", "com.markit.pe.portfoliodata"})
public interface ExceptionManagementRepoConfig {

}
