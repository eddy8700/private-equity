package com.markit.pe.positiondata.validation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.markit.pe.portfoliodata.constants.PEConstants;
import com.markit.pe.portfoliodata.util.DateUtility;
import com.markit.pe.portfoliodata.validator.SingleValidationError;
import com.markit.pe.portfoliodata.validator.ValidationError;
import com.markit.pe.portfoliodata.validator.Validator;
import com.markit.pe.positiondata.constant.PEDomainValidationConstants;
import com.markit.pe.positiondata.domain.FixedSecurity;
import com.markit.pe.positiondata.domain.FloatingSecurity;
import com.markit.pe.positiondata.domain.PESecurityDetails;

@Component
public class PESecurityValidator implements Validator<PESecurityDetails, ValidationError<PESecurityDetails>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PESecurityValidator.class);

	@Override
	public List<SingleValidationError<PESecurityDetails>> validate(final PESecurityDetails peSecurityDetails) {
		final List<SingleValidationError<PESecurityDetails>> validationErrorList = new ArrayList<>();

		if (peSecurityDetails != null) {

			if (peSecurityDetails.getSecurityName() == null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_SECURITY_NAME_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_SECURITY_NAME_NOT_PRESENT);
				validationErrorList.add(validationError);
			}
			if (peSecurityDetails.getCurrency() == null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_CURRENCY_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_CURRENCY_NOT_PRESENT);
				validationErrorList.add(validationError);
			}
			if (peSecurityDetails.getTransactionPrice() == null
					|| peSecurityDetails.getTransactionPrice().equals(BigDecimal.ZERO)) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_TRANSACTION_PRICE_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_TRANSACTION_PRICE_NOT_PRESENT);
				validationErrorList.add(validationError);
			}
			if (peSecurityDetails.getTransactionDate() == null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_TRANSACTION_DATE_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_TRANSACTION_DATE_NOT_PRESENT);
				validationErrorList.add(validationError);
			}

			if (peSecurityDetails.getNextPaymentDate() == null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_NEXT_PAYMENT_DATE_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_NEXT_PAYMENT_DATE_NOT_PRESENT);
				validationErrorList.add(validationError);
			}
			if (peSecurityDetails.getMaturityDate() == null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_MATURITY_DATE_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_MATURITY_DATE_NOT_PRESENT);
				validationErrorList.add(validationError);
			}
			if (peSecurityDetails.getIssueDate() == null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_ISSUE_DATE_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_ISSUE_DATE_NOT_PRESENT);
				validationErrorList.add(validationError);
			}
			if (peSecurityDetails.getClassification() == null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_CLASSIFICATION_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_CLASSIFICATION_NOT_PRESENT);
				validationErrorList.add(validationError);
			}
			if (peSecurityDetails.getSector() == null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_SECTOR_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_SECTOR_NOT_PRESENT);
				validationErrorList.add(validationError);
			}
			
			if (peSecurityDetails.getPrincipalPaymentType() != null && (!peSecurityDetails.getPrincipalPaymentType().equals(PEConstants.PRINCIPAL_PAYMENT_TYPE_BULLET))) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_PPT_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_PPT_NOT_PRESENT);
				validationErrorList.add(validationError);
			}
			if (peSecurityDetails.getPrincipalPaymentType() != null && (!peSecurityDetails.getPrincipalPaymentType().equals(PEConstants.PRINCIPAL_PAYMENT_TYPE_BULLET))) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_PPT_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_PPT_NOT_PRESENT);
				validationErrorList.add(validationError);
			}
			if (peSecurityDetails.getType() == null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				validationError.setErrorCode(PEDomainValidationConstants.PE_TYPE_ERROR_CODE);
				validationError.setErrorDescription(PEDomainValidationConstants.PE_TYPE_NOT_PRESENT);
				validationErrorList.add(validationError);
			}
			
			if (peSecurityDetails.getType() != null && peSecurityDetails instanceof FloatingSecurity) {
				FloatingSecurity floatingSecurity=(FloatingSecurity) peSecurityDetails;
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				if(floatingSecurity.getCouponBenchmark() == null){
					validationError.setErrorCode(PEDomainValidationConstants.PE_TYPE_COUPON_BENCHMARK_CODE);
					validationError.setErrorDescription(PEDomainValidationConstants.PE_COUPON_BENCHMARK_NOT_PRESENT);
					validationErrorList.add(validationError);
					
				}if(floatingSecurity.getCouponResetFrequency()== null){
					validationError.setErrorCode(PEDomainValidationConstants.PE_COUPON_FREQ_ERROR_CODE);
					validationError.setErrorDescription(PEDomainValidationConstants.PE_COUPON_FREQ_NOT_PRESENT);
					validationErrorList.add(validationError);
				}
				if(floatingSecurity.getMargin()==null){
					validationError.setErrorCode(PEDomainValidationConstants.PE_MARGIN_ERROR_CODE);
					validationError.setErrorDescription(PEDomainValidationConstants.PE_MARGIN_NOT_PRESENT);
					validationErrorList.add(validationError);
				}
				
			}
			if (peSecurityDetails.getType() != null && peSecurityDetails instanceof FixedSecurity){
				FixedSecurity fixedSecurity=(FixedSecurity) peSecurityDetails;
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				if(fixedSecurity.getCoupon() == null){
					validationError.setErrorCode(PEDomainValidationConstants.PE_COUPON_ERROR_CODE);
					validationError.setErrorDescription(PEDomainValidationConstants.PE_COUPON_NOT_PRESENT);
					validationErrorList.add(validationError);
			}
				if(fixedSecurity.getFrequency() == null){
					validationError.setErrorCode(PEDomainValidationConstants.PE_FREQUENCY_ERROR_CODE);
					validationError.setErrorDescription(PEDomainValidationConstants.PE_FREQUENCY_NOT_PRESENT);
					validationErrorList.add(validationError);
			}
			}
			
			if (peSecurityDetails.getIssueDate() != null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				LOGGER.info("Check for issue date after current date");
				final boolean isAfter = DateUtility.isAfter(new Date(), peSecurityDetails.getIssueDate());
				if (isAfter) {
					LOGGER.info("issue date after current date");
					validationError
							.setErrorCode(PEDomainValidationConstants.PE_ISSUE_DATE_AFTER_CURRENT_DATE_ERROR_CODE);
					validationError.setErrorDescription(PEDomainValidationConstants.PE_ISSUE_DATE_AFTER_CURRENT_DATE);
					validationErrorList.add(validationError);
				}
			}

			if (peSecurityDetails.getIssueDate() != null && peSecurityDetails.getTransactionDate() != null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				LOGGER.info("Check for issue date after transaction date");
				final boolean isAfter = DateUtility.isAfter(peSecurityDetails.getTransactionDate(),
						peSecurityDetails.getIssueDate());
				if (isAfter) {
					LOGGER.info("issue date after transaction date");
					validationError
							.setErrorCode(PEDomainValidationConstants.PE_ISSUE_DATE_AFTER_TRANSACTION_DATE_ERROR_CODE);
					validationError
							.setErrorDescription(PEDomainValidationConstants.PE_ISSUE_DATE_AFTER_TRANSACTION_DATE);
					validationErrorList.add(validationError);
				}
			}
			if (peSecurityDetails.getIssueDate() != null && peSecurityDetails.getMaturityDate() != null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				LOGGER.info("Check for issue date after maturity date");
				final boolean isAfter = DateUtility.isAfter(peSecurityDetails.getMaturityDate(),
						peSecurityDetails.getIssueDate());
				if (isAfter) {
					LOGGER.info("issue date after maturity date");
					validationError
							.setErrorCode(PEDomainValidationConstants.PE_ISSUE_DATE_AFTER_MATURITY_DATE_ERROR_CODE);
					validationError.setErrorDescription(PEDomainValidationConstants.PE_ISSUE_DATE_AFTER_MATURITY_DATE);
					validationErrorList.add(validationError);
				}
			}
			if (peSecurityDetails.getIssueDate() != null && peSecurityDetails.getNextPaymentDate() != null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				LOGGER.info("Check for issue date after next payment date");
				final boolean isAfter = DateUtility.isAfter(peSecurityDetails.getNextPaymentDate(),
						peSecurityDetails.getIssueDate());
				if (isAfter) {
					LOGGER.info("issue date after next payment date");
					validationError
							.setErrorCode(PEDomainValidationConstants.PE_ISSUE_DATE_AFTER_NEXT_PAYMENT_DATE_ERROR_CODE);
					validationError
							.setErrorDescription(PEDomainValidationConstants.PE_ISSUE_DATE_AFTER_NEXT_PAYMENT_DATE);
					validationErrorList.add(validationError);
				}
			}
			if (peSecurityDetails.getMaturityDate() != null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				LOGGER.info("Check for maturity date before current date");
				final boolean isBefore = DateUtility.isBefore(new Date(), peSecurityDetails.getMaturityDate());
				if (isBefore) {
					LOGGER.info("maturity date before current date");
					validationError
							.setErrorCode(PEDomainValidationConstants.PE_MATURITY_DATE_BEFORE_CURRENT_DATE_ERROR_CODE);
					validationError
							.setErrorDescription(PEDomainValidationConstants.PE_MATURITY_DATE_BEFORE_CURRENT_DATE);
					validationErrorList.add(validationError);
				}
			}

			if (peSecurityDetails.getMaturityDate() != null && peSecurityDetails.getTransactionDate() != null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				LOGGER.info("Check for maturity date before transaction date");
				final boolean isBefore = DateUtility.isBefore(peSecurityDetails.getTransactionDate(),
						peSecurityDetails.getMaturityDate());
				if (isBefore) {
					LOGGER.info("maturity date before transaction date");
					validationError.setErrorCode(
							PEDomainValidationConstants.PE_MATURITY_DATE_BEFORE_TRANSACTION_DATE_ERROR_CODE);
					validationError
							.setErrorDescription(PEDomainValidationConstants.PE_MATURITY_DATE_BEFORE_TRANSACTION_DATE);
					validationErrorList.add(validationError);
				}
			}
			if (peSecurityDetails.getMaturityDate() != null && peSecurityDetails.getNextPaymentDate() != null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				LOGGER.info("Check for maturity date before next payment  date");
				final boolean isBefore = DateUtility.isBefore(peSecurityDetails.getNextPaymentDate(),
						peSecurityDetails.getMaturityDate());
				if (isBefore) {
					LOGGER.info("maturity date before transaction date");
					validationError
							.setErrorCode(PEDomainValidationConstants.PE_MATURITY_DATE_BEFORE_NEXT_DATE_ERROR_CODE);
					validationError.setErrorDescription(PEDomainValidationConstants.PE_MATURITY_DATE_BEFORE_NEXT_DATE);
					validationErrorList.add(validationError);
				}
			}
			if (peSecurityDetails.getTransactionDate() != null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				LOGGER.info("Check for transaction date after current date");
				final boolean isAfter = DateUtility.isAfter(peSecurityDetails.getTransactionDate(), new Date());
				if (isAfter) {
					LOGGER.info("transaction date after current date");
					validationError.setErrorCode(
							PEDomainValidationConstants.PE_TRANSACTION_DATE_AFTER_CURRENT_DATE_ERROR_CODE);
					validationError
							.setErrorDescription(PEDomainValidationConstants.PE_TRANSACTION_DATE_AFTER_CURRENT_DATE);
					validationErrorList.add(validationError);
				}
			}
			if (peSecurityDetails.getTransactionDate() != null && peSecurityDetails.getNextPaymentDate() != null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				LOGGER.info("Check for transaction date after next payment  date");
				final boolean isAfter = DateUtility.isAfter(peSecurityDetails.getTransactionDate(),
						peSecurityDetails.getNextPaymentDate());
				if (isAfter) {
					LOGGER.info("transaction date after next payment date");
					validationError.setErrorCode(
							PEDomainValidationConstants.PE_TRANSACTION_DATE_AFTER_NEXTPAYMENT_DATE_ERROR_CODE);
					validationError.setErrorDescription(
							PEDomainValidationConstants.PE_TRANSACTION_DATE_AFTER_NEXTPAYMENT_DATE);
					validationErrorList.add(validationError);
				}
			}
			if (peSecurityDetails.getNextPaymentDate() != null) {
				final SingleValidationError<PESecurityDetails> validationError = new SingleValidationError<>();
				LOGGER.info("Check for next payment  date before current  date");
				final boolean isBefore = DateUtility.isBefore(new Date(), peSecurityDetails.getNextPaymentDate());
				if (isBefore) {
					LOGGER.info("next payment  date before current  date");
					validationError.setErrorCode(
							PEDomainValidationConstants.PE_NEXTPAYMENT_DATE_BEFORE_CURRENT_DATE_ERROR_CODE);
					validationError
							.setErrorDescription(PEDomainValidationConstants.PE_NEXTPAYMENT_DATE_BEFORE_CURRENT_DATE);
					validationErrorList.add(validationError);
				}
			}
		}

		return validationErrorList;
	}

	@Override
	public String identity() {
		return "PESecurityValidator";
	}

	@Override
	public List<String> buildErrorMessage(final List<SingleValidationError<PESecurityDetails>> validationErrorSet) {
		List<String> messages = new ArrayList<>();
		for (SingleValidationError<PESecurityDetails> singleValidationError : validationErrorSet) {
			messages.add(singleValidationError.getErrorMsg());
		}
		return messages;
	}
	
	public PESecurityDetails buildForDefaultValues(final PESecurityDetails details){
		LOGGER.info("Building default values if any");
		if(details.getStartingPrincipal() == null){
			details.setStartingPrincipal(new BigDecimal(100));
		}
		if(details.getDayCountConvention()==null){
			details.setDayCountConvention("Actual/360");
		}
		if(details.getPrincipalPaymentType()==null){
			details.setPrincipalPaymentType(PEConstants.PRINCIPAL_PAYMENT_TYPE_BULLET);
		}
		if(details.getBusinessDayConvention()==null){
			details.setBusinessDayConvention(PEConstants.BUSINESS_DAY_CONV_DEFAULT);
		}
		return details;
		
	}

}
