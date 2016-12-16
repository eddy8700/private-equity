package com.markit.pe.valuationengine.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.markit.pe.portfoliodata.constants.PEConstants;
import com.markit.pe.positiondata.domain.FloatingSecurityMargin;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.pe.valuationengine.api.QAGApi;
import com.markit.pe.valuationengine.constant.ValuationEngineConstants;
import com.markit.pe.valuationengine.exception.ValuationEngineException;
import com.markit.pe.valuationengine.qag.BusinessDayConvQAGEnum;
import com.markit.pe.valuationengine.qag.CouponFrequencyQAGEnum;
import com.markit.pe.valuationengine.qag.DayCountConventionQAGEnum;
import com.markit.pe.valuationengine.qag.QAGService;
import com.markit.qag.analytics.bonds.cashflowschedules.CashFlowUtils;
import com.markit.qag.analytics.bonds.cashflowschedules.accrualperiods.AccrualPeriod;
import com.markit.qag.analytics.bonds.events.EventSequence;
import com.markit.qag.analytics.bonds.events.IInterestPaymentEvent;
import com.markit.qag.analytics.bonds.referencedata.BondDefinition;
import com.markit.qag.analytics.bonds.referencedatacreation.commondatamodel.BondDefinitionDataModel;
import com.markit.qag.analytics.bonds.referencedatacreation.commondatamodel.BondDefinitionDataObject;
import com.markit.qag.analytics_api.bonds.analyticssinglecalls.QuoteType;
import com.markit.qag.analytics_api.bonds.analyticssinglecalls.configuration.AnalyticsCallerName;
import com.markit.qag.analytics_api.bonds.referencedata.commondatamodel.BondDataAttributesNames;
import com.markit.qag.analytics_api.bonds.referencedata.commondatamodel.BondDataTypeNames;
import com.markit.qag.analytics_api.common.datamodel.DataModel;
import com.markit.qag.analytics_api.common.datamodel.ValidatedDataModel;
import com.markit.valuations.common.CalculationException;
import com.markit.valuations.dates.ImmutableDate;

@Component
public final class QagValuationImpl implements QAGApi {
	
	private static final Logger logger = LoggerFactory.getLogger(QagValuationImpl.class);

	@Override
	public Object calculateSinkingFactor(BondDefinition bondDefinition, ImmutableDate asOfDate) {
		try {
			return QAGService.concreteCallerObject(AnalyticsCallerName.SinkingFactor.NAME, bondDefinition, asOfDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ValuationEngineException(e.getMessage());
		}
	}

	@Override
	public Object calculateAverageLife(BondDefinition bondDefinition, ImmutableDate asOfDate) {
		try {
			return QAGService.concreteCallerObject(AnalyticsCallerName.AverageLife.NAME, bondDefinition, asOfDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ValuationEngineException(e.getMessage());
		}
	}

	@Override
	public Object calculateDirtyPrice(BondDefinition bondDefinition, ImmutableDate asOfDate, double purchaseValue) {
		try {
			return QAGService.concreteCallerObject(AnalyticsCallerName.DirtyPrice.NAME, bondDefinition, asOfDate,
					purchaseValue, QuoteType.CleanPrice);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ValuationEngineException(e.getMessage());
		}
	}

	@Override
	public Object calculateCleanPrice(BondDefinition bondDefinition, ImmutableDate asOfDate, double purchaseValue) {
		try {
			return QAGService.concreteCallerObject(AnalyticsCallerName.CleanPrice.NAME, bondDefinition, asOfDate,
					purchaseValue, QuoteType.CleanPrice);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ValuationEngineException(e.getMessage());
		}
	}

	@Override
	public Object calculateAccrued(BondDefinition bondDefinition, ImmutableDate asOfDate) {
		try {
			return QAGService.concreteCallerObject(AnalyticsCallerName.Accrued.NAME, bondDefinition, asOfDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ValuationEngineException(e.getMessage());
		}
	}
	@Override
	public Object calculateStraightYTM(BondDefinition bondDefinition, ImmutableDate asOfDate, double purchaseValue) throws Exception {
		try {
			return QAGService.concreteCallerObject(AnalyticsCallerName.StraightYieldToMaturity.NAME, bondDefinition, asOfDate);
		} catch (Exception e) {
			throw new ValuationEngineException(e.getMessage());
		}
		
	}
	@Override
	public Object calculateDirtyPriceUsingStraightYield(BondDefinition bondDefinition, ImmutableDate asOfDate, double yield) throws Exception {
		try {
			return QAGService.concreteCallerObject(AnalyticsCallerName.DirtyPrice.NAME, bondDefinition, asOfDate, yield, QuoteType.StraightYield);
		} catch (Exception e) {
			throw new ValuationEngineException(e.getMessage());
		}
}
	@Override
	public  Object calculateCleanPriceUsingStraightYield(BondDefinition bondDefinition, ImmutableDate asOfDate, double yield) throws Exception {
		QAGService qagService = new QAGService();
		try {
			return qagService.concreteCallerObject(AnalyticsCallerName.CleanPrice.NAME, bondDefinition, asOfDate, yield, QuoteType.StraightYield);
		} catch (Exception e) {
			throw new ValuationEngineException(e.getMessage());
		}
	}
	@Override
	public  Object calculateDirtyPriceUsingAnnualYield(BondDefinition bondDefinition, ImmutableDate asOfDate, double yield) {
		try {
			return QAGService.concreteCallerObject(AnalyticsCallerName.DirtyPrice.NAME, bondDefinition, asOfDate, yield, QuoteType.AnnualYield);
		} catch (Exception e) {
			throw new ValuationEngineException(e.getMessage());
		}
}
	@Override
	public  Object calculateCleanPriceUsingAnnualYield(BondDefinition bondDefinition, ImmutableDate asOfDate, double yield){
		try {
			return QAGService.concreteCallerObject(AnalyticsCallerName.CleanPrice.NAME, bondDefinition, asOfDate, yield, QuoteType.AnnualYield);
		} catch (Exception e) {
			throw new ValuationEngineException(e.getMessage());
		}
}
	@Override
	public  double calculateDiscountFactorUsingStraightYTM(double straightYTM, int couponFreq, double timeToPayment){
		return Math.pow(1+straightYTM/couponFreq, -timeToPayment*couponFreq);
	}
	
	@Override
	public  double calculateDiscountFactorUsingAnnualYTM(double annualYTM, int couponFreq, double timeToPayment){
		return Math.pow(1+annualYTM, -timeToPayment);
	}
	@Override
	public  ImmutableDate getAccrualPeriodStartDate(EventSequence eventSequence, ImmutableDate evaluationDate) {
		IInterestPaymentEvent prevInterestEvent;
		try {
			prevInterestEvent = CashFlowUtils.findPreviousInterestEvent(eventSequence, evaluationDate);
		} catch (CalculationException e) {
			throw new ValuationEngineException(e.getMessage());
		}
		AccrualPeriod accrualPeriod = prevInterestEvent.getAccrualPeriod();
		if(null != accrualPeriod){
			return accrualPeriod.getPeriodStartDate();
		}
		return null;
	}
	@Override
	public  BondDefinition getBondDefination(PESecurityInfoDTO peSecurityInfoDTO){

		BondDefinitionDataModel bondDefinitionDataModel = new BondDefinitionDataModel();
		DataModel model=null;
		if(peSecurityInfoDTO.getType().equalsIgnoreCase("Fixed")){
			logger.info("Preparing QAG data model for Fixed Coupon");
			model = prepareDataModelFixedCPN(peSecurityInfoDTO);
		} else if(peSecurityInfoDTO.getType().equalsIgnoreCase("Floating")){
			logger.info("Preparing QAG data model for Floaters");
			model = prepareDataModelFRN(peSecurityInfoDTO);
		}				

		ValidatedDataModel<BondDefinitionDataObject> validatedDataModel = bondDefinitionDataModel.getValidator().validate(model); //plausibility checks on provided reference data
		if (!validatedDataModel.isValidated())
		{
			for (String problem : validatedDataModel.getProblems())
			{
				logger.error("Problem occured while preparing QAG Data Model");
				throw new ValuationEngineException(problem);
			}
		}

		BondDefinitionDataObject bondDefinitionDataObject = bondDefinitionDataModel.getBuilder().build(validatedDataModel);

		BondDefinition bondDefinition = bondDefinitionDataObject.getBondDefinition(peSecurityInfoDTO.getSecurityId());
		return bondDefinition;
	}
	
	private DataModel prepareDataModelFRN(PESecurityInfoDTO peSecurityInfoDTO) {


		DataModel.Builder dataModelBuilder = new DataModel.Builder();

		String securityId = peSecurityInfoDTO.getSecurityId();

		dataModelBuilder.addDate(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_DATES, BondDataAttributesNames.ISSUE_DATE), new ImmutableDate(peSecurityInfoDTO.getIssueDate()));
		dataModelBuilder.addDate(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_DATES, BondDataAttributesNames.FIRST_SETTLEMENT_DATE), new ImmutableDate(peSecurityInfoDTO.getIssueDate()));
		dataModelBuilder.addDate(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_DATES, BondDataAttributesNames.MATURITY_DATE), new ImmutableDate(peSecurityInfoDTO.getMaturityDate()));

		dataModelBuilder.addString(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_AMOUNT, BondDataAttributesNames.NOTIONAL_CURRENCY), peSecurityInfoDTO.getCurrency().toString());
		dataModelBuilder.addNumber(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_AMOUNT, BondDataAttributesNames.NOTIONAL_AMOUNT), ValuationEngineConstants.NOTIONAL_AMOUNT_QAG);
		dataModelBuilder.addNumber(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_AMOUNT, BondDataAttributesNames.NOTIONAL_FACE), ValuationEngineConstants.NOTIONAL_FACE_QAG); 

		dataModelBuilder.addDate(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_BASIC_COUPON_DATES, BondDataAttributesNames.INTEREST_ACCRUAL_DATE), new ImmutableDate(peSecurityInfoDTO.getIssueDate()));        
		if(null != peSecurityInfoDTO.getNextPaymentDate()){
			dataModelBuilder.addDate(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_BASIC_COUPON_DATES, BondDataAttributesNames.FIRST_COUPON_DATE), new ImmutableDate(peSecurityInfoDTO.getNextPaymentDate())); 
		}

		dataModelBuilder.addString(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.COUPON_FREQUENCY), CouponFrequencyQAGEnum.getCouponFrequencyEnum(peSecurityInfoDTO.getFrequency()).getFreqStr());
		if(StringUtils.isBlank(peSecurityInfoDTO.getDayCountConvention())){
			dataModelBuilder.addString(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.DAYCOUNT_DESC), DayCountConventionQAGEnum.getDayCountConvEnum(ValuationEngineConstants.DEFAULT_DAY_COUNT_CONV).getStd());
		} else {
			dataModelBuilder.addString(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.DAYCOUNT_DESC), DayCountConventionQAGEnum.getDayCountConvEnum(peSecurityInfoDTO.getDayCountConvention()).getStd());
		}
		dataModelBuilder.addBoolean(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.NO_LEAP_YEAR), false);
		if(DayCountConventionQAGEnum.getDayCountConvEnum(peSecurityInfoDTO.getDayCountConvention()).equals(DayCountConventionQAGEnum.DCC_NL_365)){
			dataModelBuilder.addBoolean(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.NO_LEAP_YEAR), true);
		}
		
		if(StringUtils.isNotBlank(peSecurityInfoDTO.getBusinessDayConvention())){
			dataModelBuilder.addString(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.IBOXX_BUSINESS_DAY_CONVENTIONS), BusinessDayConvQAGEnum.getBusinessDayConvEnum(peSecurityInfoDTO.getBusinessDayConvention()).getStd());
		}
		
		if(BusinessDayConvQAGEnum.getBusinessDayConvEnum(peSecurityInfoDTO.getBusinessDayConvention()).equals(BusinessDayConvQAGEnum.End_of_month)){
			dataModelBuilder.addIntegerCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_ACCRUAL_SCHEDULE, BondDataAttributesNames.ROLL_DAY_OF_MONTH_LIST), ImmutableList.of(31));
		}
		
		if(CollectionUtils.isNotEmpty(peSecurityInfoDTO.getHolidayCodes())){
			dataModelBuilder.addStringCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.HOLIDAY_CODES), ImmutableList.copyOf(peSecurityInfoDTO.getHolidayCodes()));
		}
		
		//if(null != peSecurity.getRollDayOfMonth()){
			//dataModelBuilder.addInteger(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_ACCRUAL_SCHEDULE, BondDataAttributesNames.ROLL_DAY_OF_MONTH), 31);  // TODO Remove hard coding 
		//}        

		//Repayments

		if(CollectionUtils.isNotEmpty(peSecurityInfoDTO.getRedemptionDateList()) && CollectionUtils.isNotEmpty(peSecurityInfoDTO.getRedemptionAmountList())){
			logger.info("Repayment Schedule provided");
			List<ImmutableDate> redemptionDateList = new ArrayList<ImmutableDate>();
			for(Date date : peSecurityInfoDTO.getRedemptionDateList()){
				redemptionDateList.add(new ImmutableDate(date));
			}

			dataModelBuilder.addDateCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_REDEMPTION_SCHEDULE, BondDataAttributesNames.REDEMPTION_DATE_LIST), ImmutableList.copyOf(redemptionDateList));
			dataModelBuilder.addNumberCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_REDEMPTION_SCHEDULE, BondDataAttributesNames.REDEMPTION_AMOUNT_LIST), ImmutableList.copyOf(peSecurityInfoDTO.getRedemptionAmountList()));

		}		
		
		List<Integer> frmRateMaturityList = new ArrayList<Integer>();
		List<String> frmRateMaturityUnitList = new ArrayList<String>();
		List<String> frmRateCurrencyList = new ArrayList<String>();
		List<String> frmRateTypeList = new ArrayList<String>();
		
		List<ImmutableDate> marginStartDateList = new ArrayList<ImmutableDate>();
		marginStartDateList.add(new ImmutableDate(peSecurityInfoDTO.getIssueDate()));
		frmRateMaturityList.add(CouponFrequencyQAGEnum.getCouponFrequencyEnum(peSecurityInfoDTO.getFrequency()).getPrefix());
		frmRateMaturityUnitList.add(CouponFrequencyQAGEnum.getCouponFrequencyEnum(peSecurityInfoDTO.getFrequency()).getSuffix());
		frmRateCurrencyList.add(peSecurityInfoDTO.getCurrency().toString());
		frmRateTypeList.add(peSecurityInfoDTO.getCouponBenchmark());
		
		List<Double> marginList = new ArrayList<Double>();
		marginList.add(peSecurityInfoDTO.getMargin().doubleValue()*100d);
		
		if(CollectionUtils.isNotEmpty(peSecurityInfoDTO.getFloatingSecurityMargins())){
			for(FloatingSecurityMargin margin : peSecurityInfoDTO.getFloatingSecurityMargins()){
				marginStartDateList.add(new ImmutableDate(margin.getResetDate()));
				frmRateMaturityList.add(CouponFrequencyQAGEnum.getCouponFrequencyEnum(peSecurityInfoDTO.getFrequency()).getPrefix());
				frmRateMaturityUnitList.add(CouponFrequencyQAGEnum.getCouponFrequencyEnum(peSecurityInfoDTO.getFrequency()).getSuffix());
				frmRateCurrencyList.add(peSecurityInfoDTO.getCurrency().toString());
				frmRateTypeList.add(peSecurityInfoDTO.getCouponBenchmark());
				marginList.add(margin.getNewMargin().doubleValue()*100d);
			}
		}
		
        dataModelBuilder.addDateCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_DEFINITION_VALID_FROM_LIST), ImmutableList.copyOf(marginStartDateList)); //margin st dates
        //dataModelBuilder.addStringCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_RATE_TYPE_LIST), ImmutableList.of(peSecurityInfoDTO.getCouponBenchmark()));//reference/base type
        dataModelBuilder.addStringCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_RATE_TYPE_LIST), ImmutableList.copyOf(frmRateTypeList));//reference/base type
        //dataModelBuilder.addStringCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_RATE_CURRENCY_LIST), ImmutableList.of(peSecurityInfoDTO.getCurrency().toString()));
        dataModelBuilder.addStringCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_RATE_CURRENCY_LIST), ImmutableList.copyOf(frmRateCurrencyList));
        //dataModelBuilder.addIntegerCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_RATE_MATURITY_LIST), ImmutableList.of(CouponFrequencyQAGEnum.getCouponFrequencyEnum(peSecurityInfoDTO.getFrequency()).getPrefix())); //number of the months/years/days // assuming coupon freq
        dataModelBuilder.addIntegerCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_RATE_MATURITY_LIST), ImmutableList.copyOf(frmRateMaturityList)); //number of the months/years/days // assuming coupon freq
        //dataModelBuilder.addStringCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_RATE_MATURITY_UNIT_LIST), ImmutableList.of(CouponFrequencyQAGEnum.getCouponFrequencyEnum(peSecurityInfoDTO.getFrequency()).getSuffix())); //fixing unit eg M, W
        dataModelBuilder.addStringCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_RATE_MATURITY_UNIT_LIST), ImmutableList.copyOf(frmRateMaturityUnitList)); //fixing unit eg M, W
        dataModelBuilder.addNumberCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_QUOTED_MARGIN_LIST), ImmutableList.copyOf(marginList)); //eg 0.16% = 16d, 1.2% = 120d
        //dataModelBuilder.addIntegerCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_DAY_FIXING_LEG), ImmutableList.of(2)); //look back to 2 business days
        dataModelBuilder.addStringCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FRN_SCHEDULE, BondDataAttributesNames.FRN_HOLIDAY_CODE_LIST), ImmutableList.copyOf(peSecurityInfoDTO.getHolidayCodes())); //will be actual days when empty

        
		return dataModelBuilder.build();
	}
	

	private static DataModel prepareDataModelFixedCPN(PESecurityInfoDTO peSecurityInfoDTO) {

		DataModel.Builder dataModelBuilder = new DataModel.Builder();

		String securityId = peSecurityInfoDTO.getSecurityId();

		dataModelBuilder.addDate(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_DATES, BondDataAttributesNames.ISSUE_DATE), new ImmutableDate(peSecurityInfoDTO.getIssueDate()));
		dataModelBuilder.addDate(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_DATES, BondDataAttributesNames.FIRST_SETTLEMENT_DATE), new ImmutableDate(peSecurityInfoDTO.getIssueDate()));
		dataModelBuilder.addDate(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_DATES, BondDataAttributesNames.MATURITY_DATE), new ImmutableDate(peSecurityInfoDTO.getMaturityDate()));

		dataModelBuilder.addString(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_AMOUNT, BondDataAttributesNames.NOTIONAL_CURRENCY), peSecurityInfoDTO.getCurrency().toString());
		dataModelBuilder.addNumber(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_AMOUNT, BondDataAttributesNames.NOTIONAL_AMOUNT), ValuationEngineConstants.NOTIONAL_AMOUNT_QAG);
		dataModelBuilder.addNumber(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_PRINCIPAL_AMOUNT, BondDataAttributesNames.NOTIONAL_FACE), ValuationEngineConstants.NOTIONAL_FACE_QAG); 

		dataModelBuilder.addDateCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FIXED_CPN_SCHEDULE, BondDataAttributesNames.COUPON_VALID_FROM_LIST), ImmutableList.of(new ImmutableDate(peSecurityInfoDTO.getIssueDate())));
		dataModelBuilder.addNumberCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_FIXED_CPN_SCHEDULE, BondDataAttributesNames.COUPON_LIST), ImmutableList.of(peSecurityInfoDTO.getCoupon().doubleValue()));

		dataModelBuilder.addDate(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_BASIC_COUPON_DATES, BondDataAttributesNames.INTEREST_ACCRUAL_DATE), new ImmutableDate(peSecurityInfoDTO.getIssueDate()));        
		if(null != peSecurityInfoDTO.getNextPaymentDate()){
			dataModelBuilder.addDate(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_BASIC_COUPON_DATES, BondDataAttributesNames.FIRST_COUPON_DATE), new ImmutableDate(peSecurityInfoDTO.getNextPaymentDate())); 
		}

		dataModelBuilder.addString(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.COUPON_FREQUENCY), CouponFrequencyQAGEnum.getCouponFrequencyEnum(peSecurityInfoDTO.getFrequency()).getFreqStr());
		if(StringUtils.isBlank(peSecurityInfoDTO.getDayCountConvention())){
			dataModelBuilder.addString(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.DAYCOUNT_DESC), DayCountConventionQAGEnum.getDayCountConvEnum(ValuationEngineConstants.DEFAULT_DAY_COUNT_CONV).getStd());
		} else {
			dataModelBuilder.addString(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.DAYCOUNT_DESC), DayCountConventionQAGEnum.getDayCountConvEnum(peSecurityInfoDTO.getDayCountConvention()).getStd());
		}
		dataModelBuilder.addBoolean(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.NO_LEAP_YEAR), false);
		if(DayCountConventionQAGEnum.getDayCountConvEnum(peSecurityInfoDTO.getDayCountConvention()).equals(DayCountConventionQAGEnum.DCC_NL_365)){
			dataModelBuilder.addBoolean(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.NO_LEAP_YEAR), true);
		}
		
		if(StringUtils.isNotBlank(peSecurityInfoDTO.getBusinessDayConvention())){
			dataModelBuilder.addString(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.IBOXX_BUSINESS_DAY_CONVENTIONS), BusinessDayConvQAGEnum.getBusinessDayConvEnum(peSecurityInfoDTO.getBusinessDayConvention()).getStd());
		}
		
		if(BusinessDayConvQAGEnum.getBusinessDayConvEnum(peSecurityInfoDTO.getBusinessDayConvention()).equals(BusinessDayConvQAGEnum.End_of_month)){
			dataModelBuilder.addIntegerCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_ACCRUAL_SCHEDULE, BondDataAttributesNames.ROLL_DAY_OF_MONTH_LIST), ImmutableList.of(31));
		}
		
		if(CollectionUtils.isNotEmpty(peSecurityInfoDTO.getHolidayCodes())){
			dataModelBuilder.addStringCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_COMPOUNDING_DEF, BondDataAttributesNames.HOLIDAY_CODES), ImmutableList.copyOf(peSecurityInfoDTO.getHolidayCodes()));
		}
		
		//if(null != peSecurity.getRollDayOfMonth()){
			//dataModelBuilder.addInteger(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_ACCRUAL_SCHEDULE, BondDataAttributesNames.ROLL_DAY_OF_MONTH), 31);  // TODO Remove hard coding 
		//}        

		//Repayments

		if(CollectionUtils.isNotEmpty(peSecurityInfoDTO.getRedemptionDateList()) && CollectionUtils.isNotEmpty(peSecurityInfoDTO.getRedemptionAmountList())){
			logger.info("Repayment Schedule provided");
			List<ImmutableDate> redemptionDateList = new ArrayList<ImmutableDate>();
			for(Date date : peSecurityInfoDTO.getRedemptionDateList()){
				redemptionDateList.add(new ImmutableDate(date));
			}

			dataModelBuilder.addDateCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_REDEMPTION_SCHEDULE, BondDataAttributesNames.REDEMPTION_DATE_LIST), ImmutableList.copyOf(redemptionDateList));
			dataModelBuilder.addNumberCollection(DataModel.DataKey.of(securityId, BondDataTypeNames.BOND_REDEMPTION_SCHEDULE, BondDataAttributesNames.REDEMPTION_AMOUNT_LIST), ImmutableList.copyOf(peSecurityInfoDTO.getRedemptionAmountList()));

		}

		return dataModelBuilder.build();
	}
	
	public static ImmutableDate convertToImmutableDate(String dateStr){
		SimpleDateFormat formatter = new SimpleDateFormat(PEConstants.PE_DEFAULT_DATE_FORMAT);
		Date date = null;
		try {
			date = formatter.parse(dateStr);
		} catch (ParseException e) {
			throw new ValuationEngineException(e.getMessage());
		}
		return new ImmutableDate(date);
	}
	
	public static String convertFromImmutableDate(ImmutableDate dateID){
		SimpleDateFormat formatter = new SimpleDateFormat(PEConstants.PE_DEFAULT_DATE_FORMAT);
		String dateStr = null;
		dateStr = formatter.format(dateID.getDate());
		return dateStr;
	}
	@Override
	public  Object calculateAnnualYTM(BondDefinition bondDefinition, ImmutableDate asOfDate, double purchaseValue) {
		try {
			return QAGService.concreteCallerObject(AnalyticsCallerName.AnnualYieldToMaturity.NAME, bondDefinition, asOfDate, purchaseValue, QuoteType.CleanPrice);
		} catch (Exception e) {
			throw new ValuationEngineException(e.getMessage());
		}
		
	}
	
}