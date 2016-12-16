package com.markit.pe.positiondata.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EnableJpaAuditing
@Configuration
@ComponentScan
@EntityScan(basePackages = {"com.markit.pe.positiondata", "com.markit.pe.portfoliodata"})
public interface PositionDataRepoConfig {

}
