package com.markit.pe.positiondata.validation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.markit.pe.portfoliodata.util.DuplicateDomainHandler;
import com.markit.pe.portfoliodata.validator.SingleValidationError;
import com.markit.pe.portfoliodata.validator.ValidationError;
import com.markit.pe.portfoliodata.validator.Validator;
import com.markit.pe.positiondata.constant.PEDomainValidationConstants;
import com.markit.pe.positiondata.domain.PEClient;

@Component
public class PEClientValidator implements Validator<PEClient, ValidationError<PEClient>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PEClientValidator.class);
	
	@Override
	public String identity() {
		return "PEClientValidator";
	}
	
	
	@Resource(name="clientDuplicateHandler")
	private DuplicateDomainHandler<PEClient> clientDuplicateHandler;
	
	

	@Override
	public List<SingleValidationError<PEClient>> validate(final PEClient peClient) {
		final List<SingleValidationError<PEClient>> validationErrorList = new ArrayList<>();
		
		
		if(peClient.getClientName()==null){
			LOGGER.info("Validating client name");
			final SingleValidationError<PEClient> validationError= new SingleValidationError<>();
			validationError.setErrorCode(PEDomainValidationConstants.PE_CLIENT_NAME_ERROR_CODE);
			  validationError.setErrorDescription(PEDomainValidationConstants.PE_CLIENT_NAME_NOT_PRESENT);
			  validationErrorList.add(validationError);
		} 	
		if(peClient.getClientAbbrName()==null){
			LOGGER.info("Validating client abbrivated name");
			final SingleValidationError<PEClient> validationError= new SingleValidationError<>();
			validationError.setErrorCode(PEDomainValidationConstants.PE_CLIENT_ABBR_NAME_ERROR_CODE);
			  validationError.setErrorDescription(PEDomainValidationConstants.PE_CLIENT_ABBR_NAME_NOT_PRESENT);
			  validationErrorList.add(validationError);
		}
		if(peClient.getClientName()!=null && peClient.getClientName().length()<5){
			LOGGER.info("Validating client name length");
			final SingleValidationError<PEClient> validationError= new SingleValidationError<>();
			validationError.setErrorCode(PEDomainValidationConstants.PE_CLIENT_NAME_LENGTH_ERROR_CODE);
			  validationError.setErrorDescription(PEDomainValidationConstants.PE_CLIENT_NAME_LENGTH_NOT_PRESENT);
			  validationErrorList.add(validationError);
		} 
		if(peClient.getClientAbbrName()!=null && peClient.getClientAbbrName().length()<5){
			LOGGER.info("Validating client abbrivatedd name length");
			final SingleValidationError<PEClient> validationError= new SingleValidationError<>();
			validationError.setErrorCode(PEDomainValidationConstants.PE_CLIENT_NAME_ABBRV_LENGTH_ERROR_CODE);
			  validationError.setErrorDescription(PEDomainValidationConstants.PE_CLIENT_NAME_ABBRV_LENGTH_NOT_PRESENT);
			  validationErrorList.add(validationError);
		} 
		if(peClient.getClientAbbrName() != null && peClient.getClientAbbrName().length()>=5){
			LOGGER.info("Validating client duplicate check");
			final SingleValidationError<PEClient> validationError= new SingleValidationError<>();
			final boolean isExists=clientDuplicateHandler.processDuplicateDomain(peClient);
			if(isExists){
				validationError.setErrorCode(PEDomainValidationConstants.PE_CLIENT_NAME_ABBRV_DUPLICATED_ERROR_CODE);
				  validationError.setErrorDescription(PEDomainValidationConstants.PE_CLIENT_ABBRV_NAME_DUPLICATE);
			}
		}
		return validationErrorList;
	}



	@Override
	public List<String> buildErrorMessage(List<SingleValidationError<PEClient>> validationErrorSet) {
		List<String> messages= new ArrayList<>();
		for (SingleValidationError<PEClient> singleValidationError : validationErrorSet) {
			messages.add(singleValidationError.getErrorMsg());
		}
		return messages;
	}



	@Override
	public PEClient buildForDefaultValues(PEClient t) {
		return null;
	}

}
