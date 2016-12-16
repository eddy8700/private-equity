package com.markit.pe.positiondata.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.markit.pe.portfoliodata.util.DateUtility;
import com.markit.pe.portfoliodata.util.DuplicateDomainHandler;
import com.markit.pe.positiondata.domain.FixedSecurity;
import com.markit.pe.positiondata.domain.FloatingSecurity;
import com.markit.pe.positiondata.domain.PESecurityDetails;
import com.markit.pe.positiondata.domain.PESecurityDetailsAudit;
import com.markit.pe.positiondata.repository.PESecurityAuditRepository;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.DiffNode.State;

@Component("securityChangeDetector")
public class PESecurityPropertyChangeDetector implements DuplicateDomainHandler<PESecurityDetails>{

	private static final String TRANSACTION_DATE = "transactionDate";

	private static final String TRANSACTION_PRICE = "transactionPrice";

	private static final String FIID = "fiid";

	private static final String RATING = "rating";

	private static final String DEBT_TYPE = "debtType";

	private static final String SPREAD = "spread";

	private static final String STARTING_PRINCIPAL = "startingPrincipal";

	private static final String CALL_PRICE = "callPrice";

	private static final String YIELD_TO_CALL = "yieldToCall";

	private static final String CALL_DATE = "callDate";

	private static final String CALL_TYPE = "callType";

	private static final String CAP = "cap";

	private static final String FLOOR = "floor";

	private static final String PRINCIPAL_PAYMENT_TYPE = "principalPaymentType";

	private static final String SECURITY_DESCRIPTION = "securityDescription";

	private static final String UNIT_PRICE = "unitPrice";

	@Autowired
	private PESecurityAuditRepository auditRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PESecurityPropertyChangeDetector.class);
	
	private static final String DAY_COUNT_CONVENTION = "dayCountConvention";
	
	private static final String BUSINESS_DAY_CONVENTION = "businessDayConvention";

	private static final String MATURITY_DATE = "maturityDate";

	private static final String SECTOR = "sector";

	private static final String CLASSIFICATION = "classification";

	private static final String NEXT_PAYMENT_DATE = "nextPaymentDate";

	private static final String FREQUENCY = "frequency";

	private static final String COUPON = "coupon";

	private static final String TYPE = "type";

	private static final String CURRENCY = "currency";

	private static final String ISSUE_DATE = "issueDate";
	
	private static final String REDEMPTION_SCHEDULE = "redemptionSchedule";
	private static final String RESET_MARGIN = "resetMargin";
	
	private static final String MARGIN = "margin";
	
	private static final String COUPON_BENCHMARK = "couponBenchmark";
	
	private static final String COUPON_RESET_FREQUENCY = "couponResetFrequency";

	@Override
	public  void checkForDifferenceInObjects(PESecurityDetails beforepeSecurityDetails, PESecurityDetails afterpeSecurityDetails) {
		DiffNode root = ObjectDifferBuilder.buildDefault().compare(afterpeSecurityDetails, beforepeSecurityDetails);
		final Object baseValue = root.canonicalGet(beforepeSecurityDetails);
		final Object workingValue = root.canonicalGet(afterpeSecurityDetails);
		//SimpleDateFormat formatneeded = new SimpleDateFormat(DATE_FORMAT_NEEDED);
		

		if(!beforepeSecurityDetails.getRedemptionSchedules().equals(afterpeSecurityDetails.getRedemptionSchedules())){
			LOGGER.debug("There is a change in the redemptionSchedule");
			StringBuilder oldRedemption = new StringBuilder();
			for(int i=0;i<beforepeSecurityDetails.getRedemptionSchedules().size();i++){
				if(i==0){
					oldRedemption.append(beforepeSecurityDetails.getRedemptionSchedules().get(i).toString());
				} else {
					oldRedemption.append(" | " + beforepeSecurityDetails.getRedemptionSchedules().get(i).toString());
				}
			}
			StringBuilder newRedemption = new StringBuilder();
			for(int i=0;i<afterpeSecurityDetails.getRedemptionSchedules().size();i++){
				if(i==0){
					newRedemption.append(afterpeSecurityDetails.getRedemptionSchedules().get(i).toString());
				} else {
					newRedemption.append(" | " + afterpeSecurityDetails.getRedemptionSchedules().get(i).toString());
				}
			}
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();			
			audit.setPropertyName(REDEMPTION_SCHEDULE);
			audit.setOldValue(oldRedemption.toString());
			audit.setNewValue(newRedemption.toString());
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);
		} 	
		
		if(beforepeSecurityDetails instanceof FloatingSecurity  && afterpeSecurityDetails instanceof FloatingSecurity){
             FloatingSecurity beforeFloatingSec=(FloatingSecurity) beforepeSecurityDetails;
             FloatingSecurity afterFloatingSec=(FloatingSecurity) afterpeSecurityDetails;
             if(!beforeFloatingSec.getResetMargins().equals(afterFloatingSec.getResetMargins())){
            	 LOGGER.debug("There is a change in the reset margin list");
     			StringBuilder oldMargin = new StringBuilder();
     			for(int i=0;i<beforeFloatingSec.getResetMargins().size();i++){
     				if(i==0){
     					oldMargin.append(beforeFloatingSec.getResetMargins().get(i).toString());
     				} else {
     					oldMargin.append(" | " + beforeFloatingSec.getResetMargins().get(i).toString());
     				}
     			}
     			StringBuilder newMargin = new StringBuilder();
     			for(int i=0;i<afterFloatingSec.getResetMargins().size();i++){
     				if(i==0){
     					newMargin.append(afterFloatingSec.getResetMargins().get(i).toString());
     				} else {
     					newMargin.append(" | " + afterFloatingSec.getResetMargins().get(i).toString());
     				}
     			}
     			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();			
     			audit.setPropertyName(RESET_MARGIN);
     			audit.setOldValue(oldMargin.toString());
     			audit.setNewValue(newMargin.toString());
     			audit.setPeSecurityDetails(afterpeSecurityDetails);
     			audit.setUpdatedAt(new Date());
     			auditRepository.save(audit);
     		
             }

		}
		
		
		if (null != root.getChild(MATURITY_DATE) && root.getChild(MATURITY_DATE).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the maturity date");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final Date oldMaturityDate = ((PESecurityDetails) baseValue).getMaturityDate();
			final Date newMaturityDate = ((PESecurityDetails) workingValue).getMaturityDate();
			audit.setPropertyName(MATURITY_DATE);
			audit.setOldValue(DateUtility.auditDateFormat(oldMaturityDate));
			audit.setNewValue(DateUtility.buildDateFormat(newMaturityDate));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		
		if (null != root.getChild(ISSUE_DATE) && root.getChild(ISSUE_DATE).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the issue date");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final Date oldIssueDate = ((PESecurityDetails) baseValue).getIssueDate();
			final Date newIssueDate = ((PESecurityDetails) workingValue).getIssueDate();
			audit.setPropertyName(ISSUE_DATE);
			audit.setOldValue(DateUtility.auditDateFormat(oldIssueDate));
			audit.setNewValue(DateUtility.buildDateFormat(newIssueDate));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(CURRENCY) && root.getChild(CURRENCY).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the currency");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldCurrency = ((PESecurityDetails) baseValue).getCurrency().toString();
			final String newCurrency = ((PESecurityDetails) workingValue).getCurrency().toString();
			audit.setPropertyName(CURRENCY);
			audit.setOldValue(oldCurrency.toString());
			audit.setNewValue(newCurrency.toString());
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(TYPE) && root.getChild(TYPE).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the type");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((PESecurityDetails) baseValue).getType();
			final String newType = ((PESecurityDetails) workingValue).getType();
			audit.setPropertyName(TYPE);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(COUPON) && root.getChild(COUPON).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the coupon rate");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final BigDecimal oldType = buildBigDecimalValue(((FixedSecurity) baseValue).getCoupon());
			final BigDecimal newType = buildBigDecimalValue(((FixedSecurity) workingValue).getCoupon());
			audit.setPropertyName(COUPON);
			audit.setOldValue(buildStringFromDecimal(oldType));
			audit.setNewValue(buildStringFromDecimal(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(FREQUENCY) && root.getChild(FREQUENCY).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the frequency");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((PESecurityDetails) baseValue).getFrequency();
			final String newType = ((PESecurityDetails) workingValue).getFrequency();
			audit.setPropertyName(FREQUENCY);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}

		if (null != root.getChild(NEXT_PAYMENT_DATE)
				&& root.getChild(NEXT_PAYMENT_DATE).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the next payment date");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final Date oldType = ((PESecurityDetails) baseValue).getNextPaymentDate();
			final Date newType = ((PESecurityDetails) workingValue).getNextPaymentDate();
			audit.setPropertyName(NEXT_PAYMENT_DATE);
			audit.setOldValue(DateUtility.auditDateFormat(oldType));
			audit.setNewValue(DateUtility.buildDateFormat(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}

		if (null != root.getChild(CLASSIFICATION) && root.getChild(CLASSIFICATION).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the classification");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((PESecurityDetails) baseValue).getClassification();
			final String newType = ((PESecurityDetails) workingValue).getClassification();
			audit.setPropertyName(CLASSIFICATION);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(SECTOR) && root.getChild(SECTOR).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the sector");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((PESecurityDetails) baseValue).getSector();
			final String newType = ((PESecurityDetails) workingValue).getSector();
			audit.setPropertyName(SECTOR);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}

		if (null != root.getChild(DAY_COUNT_CONVENTION) && root.getChild(DAY_COUNT_CONVENTION).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the day count convention");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((PESecurityDetails) baseValue).getDayCountConvention();
			final String newType = ((PESecurityDetails) workingValue).getDayCountConvention();
			audit.setPropertyName(DAY_COUNT_CONVENTION);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(BUSINESS_DAY_CONVENTION) && root.getChild(BUSINESS_DAY_CONVENTION).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the business day  convention");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((PESecurityDetails) baseValue).getBusinessDayConvention();
			final String newType = ((PESecurityDetails) workingValue).getBusinessDayConvention();
			audit.setPropertyName(BUSINESS_DAY_CONVENTION);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(UNIT_PRICE) && isStateChanged(root,UNIT_PRICE)) {
			LOGGER.debug("There is a change in the unit Price");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final BigDecimal oldType = buildBigDecimalValue(((PESecurityDetails) baseValue).getUnitPrice());
			final BigDecimal newType = buildBigDecimalValue(((PESecurityDetails) workingValue).getUnitPrice());
			audit.setPropertyName(UNIT_PRICE);
			audit.setOldValue(buildStringFromDecimal(oldType));
			audit.setNewValue(buildStringFromDecimal(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(SECURITY_DESCRIPTION) && isStateChanged(root,SECURITY_DESCRIPTION)) {
			LOGGER.debug("There is a change in the security desc");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((PESecurityDetails) baseValue).getSecurityDescription();
			final String newType = ((PESecurityDetails) workingValue).getSecurityDescription();
			audit.setPropertyName(SECURITY_DESCRIPTION);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		
		if (null != root.getChild(FIID) && isStateChanged(root,FIID)) {
			LOGGER.debug("There is a change in the fiid");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((PESecurityDetails) baseValue).getFiid();
			final String newType = ((PESecurityDetails) workingValue).getFiid();
			audit.setPropertyName(FIID);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		
		if (null != root.getChild(RATING) && isStateChanged(root,RATING)) {
			LOGGER.debug("There is a change in the rating");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((PESecurityDetails) baseValue).getRating();
			final String newType = ((PESecurityDetails) workingValue).getRating();
			audit.setPropertyName(RATING);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(DEBT_TYPE) && isStateChanged(root,DEBT_TYPE)) {
			LOGGER.debug("There is a change in the debt type");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((PESecurityDetails) baseValue).getDebtType();
			final String newType = ((PESecurityDetails) workingValue).getDebtType();
			audit.setPropertyName(DEBT_TYPE);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(SPREAD) && isStateChanged(root,SPREAD)) {
			LOGGER.debug("There is a change in the spread");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final BigDecimal oldType = buildBigDecimalValue(((PESecurityDetails) baseValue).getSpread());
			final BigDecimal newType = buildBigDecimalValue(((PESecurityDetails) workingValue).getSpread());
			audit.setPropertyName(SPREAD);
			audit.setOldValue(buildStringFromDecimal(oldType));
			audit.setNewValue(buildStringFromDecimal(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(CALL_PRICE) && isStateChanged(root,CALL_PRICE)) {
			LOGGER.debug("There is a change in the call price");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final BigDecimal oldType = buildBigDecimalValue(((PESecurityDetails) baseValue).getCallPrice());
			final BigDecimal newType = buildBigDecimalValue(((PESecurityDetails) workingValue).getCallPrice());
			audit.setPropertyName(CALL_PRICE);
			audit.setOldValue(buildStringFromDecimal(oldType));
			audit.setNewValue(buildStringFromDecimal(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(YIELD_TO_CALL) && isStateChanged(root,YIELD_TO_CALL)) {
			LOGGER.debug("There is a change in the yield to call");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final BigDecimal oldType = buildBigDecimalValue(((PESecurityDetails) baseValue).getYieldToCall());
			final BigDecimal newType = buildBigDecimalValue(((PESecurityDetails) workingValue).getYieldToCall());
			audit.setPropertyName(YIELD_TO_CALL);
			audit.setOldValue(buildStringFromDecimal(oldType));
			audit.setNewValue(buildStringFromDecimal(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		
		if (null != root.getChild(CALL_DATE) && isStateChanged(root,CALL_DATE)) {
			LOGGER.debug("There is a change in the call date");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final Date oldType = ((PESecurityDetails) baseValue).getCallDate();
			final Date newType = ((PESecurityDetails) workingValue).getCallDate();
			audit.setPropertyName(CALL_DATE);
			audit.setOldValue(DateUtility.auditDateFormat(oldType));
			audit.setNewValue(DateUtility.buildDateFormat(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		
		if (null != root.getChild(CALL_TYPE) && isStateChanged(root,CALL_TYPE)) {
			LOGGER.debug("There is a change in the call type");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((PESecurityDetails) baseValue).getCallType();
			final String newType = ((PESecurityDetails) workingValue).getCallType();
			audit.setPropertyName(CALL_TYPE);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(CAP) && isStateChanged(root,CAP)) {
			LOGGER.debug("There is a change in the cap");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final BigDecimal oldType = buildBigDecimalValue(((PESecurityDetails) baseValue).getYieldToCall());
			final BigDecimal newType = buildBigDecimalValue(((PESecurityDetails) workingValue).getYieldToCall());
			audit.setPropertyName(CAP);
			audit.setOldValue(buildStringFromDecimal(oldType));
			audit.setNewValue(buildStringFromDecimal(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(FLOOR) && isStateChanged(root,FLOOR)) {
			LOGGER.debug("There is a change in the floor");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final BigDecimal oldType = buildBigDecimalValue(((PESecurityDetails) baseValue).getFloor());
			final BigDecimal newType = buildBigDecimalValue(((PESecurityDetails) workingValue).getFloor());
			audit.setPropertyName(FLOOR);
			audit.setOldValue(buildStringFromDecimal(oldType));
			audit.setNewValue(buildStringFromDecimal(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		
		if (null != root.getChild(PRINCIPAL_PAYMENT_TYPE) && isStateChanged(root,PRINCIPAL_PAYMENT_TYPE)) {
			LOGGER.debug("There is a change in the pricipal payment type");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final BigDecimal oldType = buildBigDecimalValue(((PESecurityDetails) baseValue).getFloor());
			final BigDecimal newType = buildBigDecimalValue(((PESecurityDetails) workingValue).getFloor());
			audit.setPropertyName(PRINCIPAL_PAYMENT_TYPE);
			audit.setOldValue(buildStringFromDecimal(oldType));
			audit.setNewValue(buildStringFromDecimal(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(STARTING_PRINCIPAL) && root.getChild(STARTING_PRINCIPAL).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the pricipal payment type");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final BigDecimal oldType = buildBigDecimalValue(((PESecurityDetails) baseValue).getStartingPrincipal());
			final BigDecimal newType = buildBigDecimalValue(((PESecurityDetails) workingValue).getStartingPrincipal());
			audit.setPropertyName(STARTING_PRINCIPAL);
			audit.setOldValue(buildStringFromDecimal(oldType));
			audit.setNewValue(buildStringFromDecimal(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(TRANSACTION_PRICE) && root.getChild(TRANSACTION_PRICE).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the transaction Price");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final BigDecimal oldType = buildBigDecimalValue(((PESecurityDetails) baseValue).getTransactionPrice());
			final BigDecimal newType = buildBigDecimalValue(((PESecurityDetails) workingValue).getTransactionPrice());
			audit.setPropertyName(TRANSACTION_PRICE);
			audit.setOldValue(buildStringFromDecimal(oldType));
			audit.setNewValue(buildStringFromDecimal(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);

		}
		if (null != root.getChild(TRANSACTION_DATE) && root.getChild(TRANSACTION_DATE).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the transaction date");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final Date oldType = ((PESecurityDetails) baseValue).getTransactionDate();
			final Date newType = ((PESecurityDetails) workingValue).getTransactionDate();
			audit.setPropertyName(TRANSACTION_DATE);
			audit.setOldValue(DateUtility.auditDateFormat(oldType));
			audit.setNewValue(DateUtility.buildDateFormat(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);
		}
		if (null != root.getChild(COUPON_BENCHMARK) && root.getChild(COUPON_BENCHMARK).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the coupon benchmark");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((FloatingSecurity) baseValue).getCouponBenchmark();
			final String newType = ((FloatingSecurity) workingValue).getCouponBenchmark();
			audit.setPropertyName(COUPON_BENCHMARK);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);
		}
		if (null != root.getChild(MARGIN) && root.getChild(MARGIN).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the margin");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final BigDecimal oldType = buildBigDecimalValue(((FloatingSecurity) baseValue).getMargin());
			final BigDecimal newType = buildBigDecimalValue(((FloatingSecurity) workingValue).getMargin());
			audit.setPropertyName(MARGIN);
			audit.setOldValue(buildStringFromDecimal(oldType));
			audit.setNewValue(buildStringFromDecimal(newType));
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);
		}
		if (null != root.getChild(COUPON_RESET_FREQUENCY) && root.getChild(COUPON_RESET_FREQUENCY).getState().equals(State.CHANGED)) {
			LOGGER.debug("There is a change in the coupon reset frequecny");
			PESecurityDetailsAudit audit = new PESecurityDetailsAudit();
			final String oldType = ((FloatingSecurity) baseValue).getCouponResetFrequency();
			final String newType = ((FloatingSecurity) workingValue).getCouponResetFrequency();
			audit.setPropertyName(COUPON_RESET_FREQUENCY);
			audit.setOldValue(oldType);
			audit.setNewValue(newType);
			audit.setPeSecurityDetails(afterpeSecurityDetails);
			audit.setUpdatedAt(new Date());
			auditRepository.save(audit);
		}
	}


	private String buildStringFromDecimal(final BigDecimal oldType) {
		return oldType != null ?oldType.toString():"NULL";
	}


	private BigDecimal buildBigDecimalValue(BigDecimal unitPrice) {
		return unitPrice != null ? unitPrice.setScale(5, RoundingMode.HALF_DOWN):null;
	}


	private boolean isStateChanged(DiffNode root,final String key) {
		return root.getChild(key).getState().equals(State.CHANGED)||root.getChild(key).getState().equals(State.ADDED);
	}
	

	@Override
	public boolean processDuplicateDomain(PESecurityDetails newDomain) {
		return false;
	}

	
}
