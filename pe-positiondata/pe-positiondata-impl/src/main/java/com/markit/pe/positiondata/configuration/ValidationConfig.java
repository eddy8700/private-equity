package com.markit.pe.positiondata.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.markit.pe.portfoliodata.validator.ValidationError;
import com.markit.pe.portfoliodata.validator.Validator;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.domain.PESecurityDetails;
import com.markit.pe.positiondata.validation.PEClientValidator;
import com.markit.pe.positiondata.validation.PESecurityValidator;

@Configuration
public class ValidationConfig {
	
	@Bean
	public  Validator<PEClient, ValidationError<PEClient>> peClientValidator(){
		Validator<PEClient, ValidationError<PEClient>> validator= new PEClientValidator();
		return validator;
		
	}

	@Bean
	public  Validator<PESecurityDetails, ValidationError<PESecurityDetails>> peSecurityDetailsValidator(){
		Validator<PESecurityDetails, ValidationError<PESecurityDetails>> validator= new PESecurityValidator();
		return validator;
		
	}
	
}
