/**
 * 
 */
package com.markit.pe.valuationengine.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.gs.collections.api.list.ImmutableList;
import com.markit.framework.common.MarkitException;
import com.markit.pe.comparabledata.domain.EVBData;
import com.markit.pe.comparabledata.request.EvbDataRefreshRequest;
import com.markit.pe.exception.domain.PEException;
import com.markit.pe.portfoliodata.Currency;
import com.markit.pe.portfoliodata.PEComparableInfo;
import com.markit.pe.portfoliodata.cache.CacheClient;
import com.markit.pe.portfoliodata.constants.PEConstants;
import com.markit.pe.positiondata.domain.FloatingSecurityMargin;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.domain.PEPortfolio;
import com.markit.pe.positiondata.domain.RedemptionSchedule;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.pe.valuationengine.api.PortfolioValuationDao;
import com.markit.pe.valuationengine.api.QAGApi;
import com.markit.pe.valuationengine.api.ValuationEngineService;
import com.markit.pe.valuationengine.constant.PortfolioValuationConstants;
import com.markit.pe.valuationengine.domain.PECalibrationInfo;
import com.markit.pe.valuationengine.domain.PECashFlowInfo;
import com.markit.pe.valuationengine.domain.PECustomizeCompRequest;
import com.markit.pe.valuationengine.domain.PEExceptionProducer;
import com.markit.pe.valuationengine.domain.PEValuationInfo;
import com.markit.pe.valuationengine.domain.PortfolioValuationUploadStatus;
import com.markit.pe.valuationengine.domain.PortfolioValuationUploadStatus.PortfolioValuationStatus;
import com.markit.pe.valuationengine.exception.ValuationEngineException;
import com.markit.pe.valuationengine.interservice.api.PEInterServiceTemplate;
import com.markit.pe.valuationengine.qag.CouponFrequencyQAGEnum;
import com.markit.pe.valuationengine.repository.FloatingSecurityMarginRepository;
import com.markit.pe.valuationengine.repository.PEPortfolioRepository;
import com.markit.pe.valuationengine.repository.PEPortfolioValuationUploadStatusRepository;
import com.markit.pe.valuationengine.repository.RedemptionScheduleRepository;
import com.markit.pe.valuationengine.request.PEValuationRequest;
import com.markit.qag.analytics.bonds.evaluations.CashflowEvaluationContext;
import com.markit.qag.analytics.bonds.evaluations.payment.EvaluatedPayment;
import com.markit.qag.analytics.bonds.evaluations.payment.EvaluatedPaymentSchedules;
import com.markit.qag.analytics.bonds.evaluations.payment.PaidCashEvaluator;
import com.markit.qag.analytics.bonds.events.EventSequence;
import com.markit.qag.analytics.bonds.events.EventsGenerator;
import com.markit.qag.analytics.bonds.marketdata.ReferenceRatesHolderLoader;
import com.markit.qag.analytics.bonds.marketdata.transfer.MarketDataUtils;
import com.markit.qag.analytics.bonds.referencedata.BondDefinition;
import com.markit.qag.analytics.bonds.referencedatacreation.HolidayDataLoader;
import com.markit.qag.analytics_api.bonds.marketdata.MarketDataContainer;
import com.markit.qag.analytics_api.bonds.marketdata.transfer.IMarketDataTransferIdentifier;
import com.markit.valuations.common.CalculationException;
import com.markit.valuations.dates.ImmutableDate;

/**
 * @author mahesh.agarwal
 *
 */
@Service
public class ValuationEngineServiceImpl implements ValuationEngineService {

	@Autowired
	private CurrencyHolidayCodeQAGMap currencyHolidayCodeQAGMap;
	
	public ValuationEngineServiceImpl() {

		logger.info("Initializing Valuation Service ... ");
		try {
			/*Resource resource = resourceLoader.getResource("classpath:holidays.txt");			
			HolidayDataLoader.loadHolidayDataFromTextFile(resource.getURL().getPath());*/
			//Other way
			logger.info("Loading Holiday data ... ");
			HolidayDataLoader.loadHolidayDataFromTextFile(ValuationEngineService.class.getResource("/holidays.txt").getPath());
			ReferenceRatesHolderLoader.loadRatesFromTextFile(ValuationEngineService.class.getResource("/ReferenceRates.csv").getPath(), false);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception occured : ", e);
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("Exception occured : ", e);
			e.printStackTrace();
		}
	
	}
	
	@Autowired
	private PEExceptionProducer peExceptionProducer;

	private static final Logger logger = LoggerFactory.getLogger(ValuationEngineService.class);

	@Autowired
	private ValuationDALImpl valuationDalImpl;
	
	@Autowired
	private PEInterServiceTemplate interServiceTemplate;
	
	@Autowired
	private PEPortfolioValuationUploadStatusRepository pePortfolioValuationUploadStatusRepository;
	
	@Autowired
	private PEPortfolioRepository pePortfolioRepository;//TODO
	
	@Autowired
	private QAGApi qagApi;
	
	@Autowired
	private CacheClient<String, Object> inMemoryCacheClient;
	
	@Autowired
	private RedemptionScheduleRepository redemptionScheduleRepository;
	
	@Autowired
	private FloatingSecurityMarginRepository floatingSecurityMarginRepository;
	
	@Autowired
	private PortfolioValuationDao portfolioValuationDao;

	/**
	 * @param evbData
	 * @return
	 * @throws ArithmeticException
	 */
	private BigDecimal calculateAvgCompMidYtm(List<EVBData> evbData) throws ArithmeticException{
		try{
			BigDecimal avgCompMidYtm = new BigDecimal(0);

			for(EVBData evbD : evbData){
				if(null!=evbD.getMidYTM()){
					avgCompMidYtm = avgCompMidYtm.add(evbD.getMidYTM());
				}			
			}

			avgCompMidYtm = avgCompMidYtm.divide(new BigDecimal(evbData.size()), 5, RoundingMode.HALF_UP);
			return avgCompMidYtm;
		} catch (ArithmeticException aE){
			logger.error("ArithmeticException while processing : "+aE);
		}		

		return new BigDecimal(0);
	}

	/**
	 * @param compList
	 * @param valDate
	 * @return
	 */
	private List<EVBData> getLatestCompsDetails(EvbDataRefreshRequest evbDataRefreshRequest){
	    return interServiceTemplate.performInterServiceCallForLatestCompsDetails(evbDataRefreshRequest);
	}

	/**
	 * @param peSecurityInfoDTO
	 * @return
	 */
	private List<EVBData> getSystemGeneratedComps(PESecurityInfoDTO peSecurityInfoDTO){

	   return interServiceTemplate.performInterServiceCallForSystemGeneratedComps(peSecurityInfoDTO);
	}

	/**
	 * @param peSecurityInfoDTO
	 * @param valDate
	 *//*
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void performValuation(PESecurityInfoDTO peSecurityInfoDTO, Date valDate) {
		try{
			if(null != peSecurityInfoDTO){
				if(StringUtils.isBlank(peSecurityInfoDTO.getActiveCalVersion())){
					performInitialCalibration(peSecurityInfoDTO);
					peSecurityInfoDTO.setActiveCalVersion(String.valueOf(new Integer(1)));
					performNextValuation(peSecurityInfoDTO, valDate);
				} else {
					performNextValuation(peSecurityInfoDTO, valDate);
				}
			} else {
				throw new ValuationEngineException("Unexpected error");
			}
		} catch (Exception ex){
			logger.error(ex.getMessage());
			ex.printStackTrace();
			//TODO handle here persist in db 
		}
		
	}*/
	
	/**
	 * @param peSecurityInfoDTO
	 */
	@Override
	public void performInitialCalibration (PESecurityInfoDTO peSecurityInfoDTO,Long portfolioValId){
		try{
			/*if(!peSecurityInfoDTO.getType().equalsIgnoreCase("Fixed"))
			{
				throw new ValuationEngineException("Not Supported");
			}*/
			Date asOfDate = peSecurityInfoDTO.getTransactionDate();
			ImmutableDate asOfDateID = new ImmutableDate(asOfDate);
			
			BondDefinition bondDefinition = getBondDefination(peSecurityInfoDTO, asOfDateID);
					
			logger.info("Starting First Calibration ..... ");

			Object sinkingFactor = qagApi.calculateSinkingFactor(bondDefinition, asOfDateID);		
			Object accrued = qagApi.calculateAccrued(bondDefinition, asOfDateID);
			double currentPrincipal = peSecurityInfoDTO.getStartingPrincipal().doubleValue()*(double)sinkingFactor;			
			logger.info("Calculating YTM from QAG liberaries ..... ");
			double purchaseValuePer = (100 * peSecurityInfoDTO.getTransactionPrice().doubleValue() / currentPrincipal);
			Object annualYTM = qagApi.calculateAnnualYTM(bondDefinition, asOfDateID, purchaseValuePer);

			peSecurityInfoDTO.setYtmTransient(doubleToBigDecimalConverter((Double)annualYTM*100));

			Object averageLife = qagApi.calculateAverageLife(bondDefinition, asOfDateID);
			peSecurityInfoDTO.setAverageLife(doubleToBigDecimalConverter((Double) averageLife));
			
			List<EVBData> evbList = new ArrayList<EVBData>();
			
			logger.info("Fetching User Customized Comparables .....");
			logger.info("channelId : "+peSecurityInfoDTO.getChannelId());
			logger.info("securityVersion : "+peSecurityInfoDTO.getSecurityVersion());
			boolean customizedCompsFound = false;
			boolean systemGenComps = false;
			List<String> comps = new ArrayList<String>();
			comps = valuationDalImpl.getCompISINsByChannelIdAndIsNotCal(peSecurityInfoDTO.getChannelId());
			
			if(!comps.isEmpty()){
				customizedCompsFound = true;
				logger.info("Found customized Comparables ... ");
				logger.info("Getting the latest market data for Comparables for Date "+asOfDate);
				EvbDataRefreshRequest evbDataRefreshRequest = new EvbDataRefreshRequest();
				//evbDataRefreshRequest.setType("ReCalibration");
				evbDataRefreshRequest.setAsOfDate(asOfDate);
				evbDataRefreshRequest.setCompList(comps);
				//evbDataRefreshRequest.setChannelId(peSecurityInfoDTO.getChannelId());
				//evbDataRefreshRequest.setParentChannelId(peSecurityInfoDTO.getParentChannelId());
				//evbDataRefreshRequest.setActiveCalVersion(peSecurityInfoDTO.getActiveCalVersion());
				
				evbList = getLatestCompsDetails(evbDataRefreshRequest);
				//evbList = getLatestCompsDetails(comps, peSecurityInfoDTO.getTransactionDate());
			} else {
				logger.info("No User Customized Comparables found ... ");
				logger.info("Fetching System generated Comparables for Date "+asOfDate);
				evbList = getSystemGeneratedComps(peSecurityInfoDTO);
				systemGenComps = true;
			}	
//			
//			logger.info("Fetching System generated Comparables for Date "+asOfDate);
//			List<EVBData> evbList = getSystemGeneratedComps(peSecurityInfoDTO);	

			if(CollectionUtils.isEmpty(evbList)){
				valuationDalImpl.markInitialCalibrationFailed(peSecurityInfoDTO.getChannelId());
				throw new ValuationEngineException("No market comparables found for Date :"+asOfDate+", using given security attributes and system pre-defined rules. Kindly add comparables or refine security attributes.");
			}
			logger.info("Found Comparables from EVB, count : "+evbList.size());
			
			logger.info("Calculating Comp YTM ..... ");
			double compYTM = calculateAvgCompMidYtm(evbList).doubleValue();

			logger.info("Calculating Day1 Spread ..... ");
			double day1Spread = (double)annualYTM*100-compYTM;

			logger.info("Ended First Calibration ..... ");

			logger.info("Starting Valuation ..... ");

			double accruedInterestValueD = currentPrincipal*(((double) accrued)/100);
			double accruedInterestPerD = (double) accrued;
			
			// Should be incorrect
			Object dirtyPricePer_1 = qagApi.calculateDirtyPrice(bondDefinition, asOfDateID, peSecurityInfoDTO.getTransactionPrice().doubleValue());
			Object cleanPricePer_1 = qagApi.calculateCleanPrice(bondDefinition, asOfDateID, peSecurityInfoDTO.getTransactionPrice().doubleValue());
			// this is simplified
			Object dirtyPricePer_2 = peSecurityInfoDTO.getTransactionPrice().doubleValue() + accruedInterestValueD;
			Object cleanPricePer_2 = peSecurityInfoDTO.getTransactionPrice().doubleValue();
			// Correct one from QAG
			Object dirtyPricePer = qagApi.calculateDirtyPrice(bondDefinition, asOfDateID, purchaseValuePer);
			Object cleanPricePer = qagApi.calculateCleanPrice(bondDefinition, asOfDateID, purchaseValuePer);

			double dirtyPriceValueD = currentPrincipal*(((double) dirtyPricePer)/100);
			double cleanPriceValueD = currentPrincipal*(((double) cleanPricePer)/100);
			double dirtyPricePerD = (double) dirtyPricePer;
			double cleanPricePerD = (double) cleanPricePer;

			logger.info("Ended Valuation ..... ");

			logger.info("Starting Cashflow calculation ..... ");
			List<PECashFlowInfo> cashFlows = calculateCashFlows((double)annualYTM, currentPrincipal, peSecurityInfoDTO, bondDefinition, asOfDateID);

			logger.info("Ended Cashflow calculation ..... ");

			logger.info("Persisting First Calibration ..... ");
			PECalibrationInfo calibrationInfo = prepareCalibrationInfo(peSecurityInfoDTO, day1Spread);
			
			calibrationInfo.setCalVersion(String.valueOf(new Integer(1)));
			long calibrationId= valuationDalImpl.persistCalibration(calibrationInfo);

			logger.info("Persisting First Valuation ..... ");
			BigDecimal day1SpreadD = doubleToBigDecimalConverter(day1Spread);
			
			PEValuationInfo valuationInfo = prepareValuationInfo(peSecurityInfoDTO, asOfDate, (double)annualYTM*100, compYTM,
					accruedInterestValueD, accruedInterestPerD, dirtyPriceValueD, cleanPriceValueD, dirtyPricePerD,
					cleanPricePerD, calibrationId, day1SpreadD);
			
			valuationInfo.setValVersion(String.valueOf(new Integer(1)));
			long valuationId = valuationDalImpl.persistValuation(valuationInfo);

			logger.info("Persisting Cashflows..... ");
			enrichCashflows(cashFlows, valuationInfo, valuationId);
			valuationDalImpl.persistCashFlows(cashFlows);

			logger.info("Persisting Comparables ..... ");
			List<PEComparableInfo> comparableInfos = new ArrayList<PEComparableInfo>();

			evbDataToComparableInfoMapper(peSecurityInfoDTO, evbList, calibrationId, valuationId, comparableInfos, systemGenComps);

			if(customizedCompsFound){
				logger.info("Removing customized set of comparables .... ");
				valuationDalImpl.removeCustomizedComps(peSecurityInfoDTO.getChannelId());
				/*logger.info("Performing updates ..... ");
				valuationDalImpl.updateComparableInfo(comparableInfos, peSecurityInfoDTO.getChannelId());*/
			} else {
				/*logger.info("Performing inserts ..... ");
				valuationDalImpl.persistComparableInfo(comparableInfos);*/
			}
			
			logger.info("Inserting set of Comparables with latest data ..... ");			
			valuationDalImpl.persistComparableInfo(comparableInfos);
			logger.info("==============================================================================");
		} catch(ValuationEngineException vEEx){
			logger.error(vEEx.getCustomMessage());
			logger.error(ExceptionUtils.getStackTrace(vEEx));
			PEException exceptionManagement= new PEException();
			exceptionManagement.setSecurityId(peSecurityInfoDTO.getSecurityId());
			exceptionManagement.setStartTime(new Date());
			exceptionManagement.setActive(true);
			exceptionManagement.setExceptionMessage(vEEx.getCustomMessage());
			exceptionManagement.setFiSecId(peSecurityInfoDTO.getFiSecId());
			exceptionManagement.setPortfolioValuationId(portfolioValId);
			//exceptionManagement.setFileName("");
			exceptionManagement.setPortfolioId(peSecurityInfoDTO.getPortfolioId());
			exceptionManagement.setProcessName("PortfolioValuation");
			Message<PEException> message=MessageBuilder.withPayload(exceptionManagement).build();
			logger.info("Publising message on "+peExceptionProducer.demo().toString());
			logger.info("Exception Message : "+message.toString());
			peExceptionProducer.demo().send(message);
		} catch(Exception ex){
			logger.error(ex.getMessage());
			logger.error(ExceptionUtils.getStackTrace(ex));
		}		
	}
	
	public BigDecimal doubleToBigDecimalConverter(final Double doubleVal){
		return new BigDecimal(doubleVal).setScale( 5, RoundingMode.HALF_UP);
	}

	/**
	 * @param cashFlows
	 * @param valuationInfo
	 * @param valuationId
	 */
	private void enrichCashflows(List<PECashFlowInfo> cashFlows, PEValuationInfo valuationInfo, long valuationId) {
		for(PECashFlowInfo cashFlow : cashFlows){
			cashFlow.setValuationId(valuationId);
			cashFlow.setValDate(valuationInfo.getValDate());
			if(valuationInfo.getValDate().compareTo(cashFlow.getAccrualStartDate()) >= 0 
					&& valuationInfo.getValDate().compareTo(cashFlow.getPaymentDate()) <= 0){
				cashFlow.setAccruedInterest(valuationInfo.getAccruedInterest());
			}			
		}
	}

	
	
	/**
	 * @param peSecurityInfoDTO
	 * @param valDate
	 */
	@Override
	public void performNextValuation (PESecurityInfoDTO peSecurityInfoDTO, Date valDate,Long portfolioValId) {
		
		try{
			/*if(!peSecurityInfoDTO.getType().equalsIgnoreCase("Fixed"))
			{
				throw new ValuationEngineException("Not Supported");
			}*/
			Date asOfDate = valDate;
			ImmutableDate asOfDateID = new ImmutableDate(asOfDate);
			
			BondDefinition bondDefinition = getBondDefination(peSecurityInfoDTO, asOfDateID);
			
			logger.info("==============================================================================");
			logger.info("Starting Next Valuation ..... ");
			logger.info("Fetching Comparables ..... ");
			
			List<String> comps = new ArrayList<String>();
			logger.info("parentChannelId : "+peSecurityInfoDTO.getParentChannelId());
			logger.info("activeCalVersion : "+peSecurityInfoDTO.getActiveCalVersion());
			comps = valuationDalImpl.getCompISINsByParentChannelIdAndCalVersion(peSecurityInfoDTO.getParentChannelId(), 
					peSecurityInfoDTO.getActiveCalVersion());

			logger.info("Found Comparables , count : "+comps.size());

			if(comps.isEmpty()){
				throw new ValuationEngineException("No comparables found from previous Calibration, can't perform Valuation");
			}
			
			List<EVBData> evbList = new ArrayList<EVBData>();

			/*logger.info("Fetching latest market data for Comparables for Date "+valDate);		*/
			
			EvbDataRefreshRequest evbDataRefreshRequest = new EvbDataRefreshRequest();
			//evbDataRefreshRequest.setType("Valuation");
			evbDataRefreshRequest.setAsOfDate(valDate);
			evbDataRefreshRequest.setCompList(comps);
			//evbDataRefreshRequest.setParentChannelId(peSecurityInfoDTO.getParentChannelId());
			//evbDataRefreshRequest.setActiveCalVersion(peSecurityInfoDTO.getActiveCalVersion());
			evbList = getLatestCompsDetails(evbDataRefreshRequest);
			
			if(CollectionUtils.isEmpty(evbList)){
				throw new ValuationEngineException("No market comparables found for Date :"+valDate+", can't perform Valuation");
			}
			
			logger.info("Found Comparables market data from EVB , count : "+evbList.size());

			logger.info("Calculating Comp YTM ..... ");
			double compYTM = calculateAvgCompMidYtm(evbList).doubleValue();

			// get day 1 spread based on cal
			/*BigDecimal day1Spread = valuationDalImpl.getDay1SpreadByParentChannelIdAndCalVersion(peSecurityInfoDTO.getParentChannelId(), 
																								peSecurityInfoDTO.getActiveCalVersion());
			 */
			logger.info("Get Active Calibration details ..... ");
			PECalibrationInfo calibrationInfo = valuationDalImpl.getCalByParentChannelIdAndCalVersion(peSecurityInfoDTO.getParentChannelId(), 
					peSecurityInfoDTO.getActiveCalVersion());
			logger.info("Using Day1 Spread from Active Calibration ..... ");
			BigDecimal day1Spread = calibrationInfo.getDay1Spread();
			long calibrationId = calibrationInfo.getId();
			logger.info("Calculating YTM based on Day1 Spread and Comp YTM ..... ");
			double annualYTM = compYTM + day1Spread.doubleValue();

			logger.info("Starting Valuation ..... ");

			Object sinkingFactor = qagApi.calculateSinkingFactor(bondDefinition, asOfDateID);		
			Object accrued = qagApi.calculateAccrued(bondDefinition, asOfDateID);

			double currentPrincipal = peSecurityInfoDTO.getStartingPrincipal().doubleValue()*(double)sinkingFactor;					

			Object dirtyPricePer = qagApi.calculateDirtyPriceUsingAnnualYield(bondDefinition, asOfDateID, annualYTM/100);
			Object cleanPricePer = qagApi.calculateCleanPriceUsingAnnualYield(bondDefinition, asOfDateID, annualYTM/100);

			double dirtyPriceValueD = currentPrincipal*(((double) dirtyPricePer)/100);
			double cleanPriceValueD = currentPrincipal*(((double) cleanPricePer)/100);
			double dirtyPricePerD = (double) dirtyPricePer;
			double cleanPricePerD = (double) cleanPricePer;

			double accruedInterestValueD = currentPrincipal*(((double) accrued)/100);
			double accruedInterestPerD = (double) accrued;

			logger.info("Ended Valuation ..... ");

			logger.info("Starting Cashflow calculation ..... ");
			List<PECashFlowInfo> cashFlows = calculateCashFlows(annualYTM/100, currentPrincipal, peSecurityInfoDTO, bondDefinition, asOfDateID);

			logger.info("Ended Cashflow calculation ..... ");

			logger.info("Persisting Next Valuation ..... ");			
			
			PEValuationInfo valuationInfo = prepareValuationInfo(peSecurityInfoDTO, asOfDate, annualYTM, compYTM,
					accruedInterestValueD, accruedInterestPerD, dirtyPriceValueD, cleanPriceValueD, dirtyPricePerD,
					cleanPricePerD, calibrationId, day1Spread);
			
			long valuationId = valuationDalImpl.persistNextValuation(valuationInfo);

			logger.info("Persisting Cashflows..... ");
			enrichCashflows(cashFlows, valuationInfo, valuationId);
			valuationDalImpl.persistCashFlows(cashFlows);

			logger.info("Persisting Comparables ..... ");
			List<PEComparableInfo> comparableInfos = new ArrayList<PEComparableInfo>();

			evbDataToComparableInfoMapper(peSecurityInfoDTO, evbList, calibrationId, valuationId, comparableInfos, false);

			valuationDalImpl.persistComparableInfo(comparableInfos);
			logger.info("==============================================================================");
		} catch(ValuationEngineException vEEx){
			logger.error(vEEx.getCustomMessage());
			logger.error(ExceptionUtils.getStackTrace(vEEx));
			PEException exceptionManagement= new PEException();
			exceptionManagement.setSecurityId(peSecurityInfoDTO.getSecurityId());
			exceptionManagement.setStartTime(new Date());
			exceptionManagement.setActive(true);
			exceptionManagement.setExceptionMessage(vEEx.getCustomMessage());
			exceptionManagement.setPortfolioValuationId(portfolioValId);
			exceptionManagement.setFiSecId(peSecurityInfoDTO.getFiSecId());
			//exceptionManagement.setFileName("");
			exceptionManagement.setPortfolioId(peSecurityInfoDTO.getPortfolioId());
			exceptionManagement.setProcessName("PortfolioValuation");
			Message<PEException> message=MessageBuilder.withPayload(exceptionManagement).build();
			logger.info("Publising message on "+peExceptionProducer.demo().toString());
			logger.info("Exception Message : "+message.toString());
			peExceptionProducer.demo().send(message);
		} catch(Exception ex){
			logger.error(ex.getMessage());
			ex.printStackTrace();
			//TODO handle here persist in db 
		}
	}
	
	
	
	/**
	 * @param peValuationRequest
	 * @throws Exception
	 */
	@Override
	public void performValuation(PEValuationRequest peValuationRequest) {
		
		/*if(!peValuationRequest.getPeSecurityInfoDTO().getType().equalsIgnoreCase("Fixed"))
		{
			throw new ValuationEngineException("Not Supported");
		}*/
		if(null == peValuationRequest 
				|| null == peValuationRequest.getPeValuationInput() 
				|| null == peValuationRequest.getPeSecurityInfoDTO()
				){
			// TODO throw
			throw new ValuationEngineException("Bad valuation request");
		}

		String valType = peValuationRequest.getPeValuationInput().getType();

		PESecurityInfoDTO peSecurityInfoDTO = peValuationRequest.getPeSecurityInfoDTO();
		
		logger.info("==============================================================================");
		logger.info("REQUEST TYPE : "+valType);
		
		if(valType.equalsIgnoreCase("Calibration")){
			
			//TODO VALIDATE for Calibration request

			logger.info("Starting First Calibration ..... ");
			
			Date asOfDate = peSecurityInfoDTO.getTransactionDate();
			ImmutableDate asOfDateID = new ImmutableDate(asOfDate);
			
			BondDefinition bondDefinition = getBondDefination(peSecurityInfoDTO, asOfDateID);

			Object sinkingFactor = qagApi.calculateSinkingFactor(bondDefinition, asOfDateID);		
			Object accrued = qagApi.calculateAccrued(bondDefinition, asOfDateID);

			double currentPrincipal = peSecurityInfoDTO.getStartingPrincipal().doubleValue()*(double)sinkingFactor;
			
			logger.info("Calculating YTM from QAG liberaries ..... ");
			double purchaseValuePer = (100 * peSecurityInfoDTO.getTransactionPrice().doubleValue() / currentPrincipal);
			Object annualYTM = qagApi.calculateAnnualYTM(bondDefinition, asOfDateID, purchaseValuePer);

			peSecurityInfoDTO.setYtmTransient(doubleToBigDecimalConverter((Double)annualYTM*100));

			Object averageLife = qagApi.calculateAverageLife(bondDefinition, asOfDateID);
			peSecurityInfoDTO.setAverageLife(doubleToBigDecimalConverter((Double)averageLife));
			
			List<EVBData> evbList = new ArrayList<EVBData>();
			
			logger.info("Fetching User Customized Comparables .....");
			logger.info("channelId : "+peSecurityInfoDTO.getChannelId());
			logger.info("securityVersion : "+peSecurityInfoDTO.getSecurityVersion());
			boolean customizedCompsFound = false;
			boolean systemGenComps = false;
			List<String> comps = new ArrayList<String>();
			comps = valuationDalImpl.getCompISINsByChannelIdAndIsNotCal(peSecurityInfoDTO.getChannelId());
			
			if(!comps.isEmpty()){
				customizedCompsFound = true;
				logger.info("Found customized Comparables ... ");
				logger.info("Getting the latest market data for Comparables for Date "+asOfDate);
				EvbDataRefreshRequest evbDataRefreshRequest = new EvbDataRefreshRequest();
				//evbDataRefreshRequest.setType("ReCalibration");
				evbDataRefreshRequest.setAsOfDate(asOfDate);
				evbDataRefreshRequest.setCompList(comps);
				//evbDataRefreshRequest.setChannelId(peSecurityInfoDTO.getChannelId());
				//evbDataRefreshRequest.setParentChannelId(peSecurityInfoDTO.getParentChannelId());
				//evbDataRefreshRequest.setActiveCalVersion(peSecurityInfoDTO.getActiveCalVersion());
				
				evbList = getLatestCompsDetails(evbDataRefreshRequest);
				//evbList = getLatestCompsDetails(comps, peSecurityInfoDTO.getTransactionDate());
			} else {
				logger.info("No User Customized Comparables found ... ");
				logger.info("Fetching System generated Comparables for Date "+asOfDate);
				evbList = getSystemGeneratedComps(peSecurityInfoDTO);
				systemGenComps = true;
			}			
			
			/*logger.info("Fetching System generated Comparables for Date "+asOfDate);
			List<EVBData> evbList = getSystemGeneratedComps(peSecurityInfoDTO);*/			
						
			if(CollectionUtils.isEmpty(evbList)){
				valuationDalImpl.markInitialCalibrationFailed(peSecurityInfoDTO.getChannelId());
				throw new ValuationEngineException("No market comparables found for Date :"+asOfDate+", using given security attributes and system pre-defined rules. Kindly add comparables or refine security attributes.");
			}
			logger.info("Found Comparables from EVB, count : "+evbList.size());
			
			logger.info("Calculating Comp YTM ..... ");
			double compYTM = calculateAvgCompMidYtm(evbList).doubleValue();

			logger.info("Calculating Day1 Spread ..... ");
			double day1Spread = (double)annualYTM*100-compYTM;

			logger.info("Ended First Calibration ..... ");

			logger.info("Starting Valuation ..... ");

			double accruedInterestValueD = currentPrincipal*(((double) accrued)/100);
			double accruedInterestPerD = (double) accrued;
			
			// Should be incorrect
			Object dirtyPricePer_1 = qagApi.calculateDirtyPrice(bondDefinition, asOfDateID, peSecurityInfoDTO.getTransactionPrice().doubleValue());
			Object cleanPricePer_1 = qagApi.calculateCleanPrice(bondDefinition, asOfDateID, peSecurityInfoDTO.getTransactionPrice().doubleValue());
			// this is simplified
			Object dirtyPricePer_2 = peSecurityInfoDTO.getTransactionPrice().doubleValue() + accruedInterestValueD;
			Object cleanPricePer_2 = peSecurityInfoDTO.getTransactionPrice().doubleValue();
			// Correct one from QAG
			Object dirtyPricePer = qagApi.calculateDirtyPrice(bondDefinition, asOfDateID, purchaseValuePer);
			Object cleanPricePer = qagApi.calculateCleanPrice(bondDefinition, asOfDateID, purchaseValuePer);

			double dirtyPriceValueD = currentPrincipal*(((double) dirtyPricePer)/100);
			double cleanPriceValueD = currentPrincipal*(((double) cleanPricePer)/100);
			double dirtyPricePerD = (double) dirtyPricePer;
			double cleanPricePerD = (double) cleanPricePer;

			

			logger.info("Ended Valuation ..... ");

			logger.info("Starting Cashflow calculation ..... ");
			List<PECashFlowInfo> cashFlows = calculateCashFlows((double)annualYTM, currentPrincipal, peSecurityInfoDTO, bondDefinition, asOfDateID);

			logger.info("Ended Cashflow calculation ..... ");

			logger.info("Persisting First Calibration ..... ");
			
			PECalibrationInfo calibrationInfo = prepareCalibrationInfo(peSecurityInfoDTO, day1Spread);
			
			calibrationInfo.setCalVersion(String.valueOf(new Integer(1)));		
			long calibrationId= valuationDalImpl.persistCalibration(calibrationInfo);

			logger.info("Persisting First Valuation ..... ");
			BigDecimal day1SpreadD = doubleToBigDecimalConverter(day1Spread);
			
			

//			valuationInfo.setStartingPrincipalOfNextPaidCF(peSecurityInfoDTO.getStartingPrincipal().doubleValue());
//			valuationInfo.setStartingPrincipalOfNextFutureCF(currentPrincipal);
			
			PEValuationInfo valuationInfo = prepareValuationInfo(peSecurityInfoDTO, asOfDate, (double)annualYTM*100, compYTM,
					accruedInterestValueD, accruedInterestPerD, dirtyPriceValueD, cleanPriceValueD, dirtyPricePerD,
					cleanPricePerD, calibrationId, day1SpreadD);
			
			valuationInfo.setValVersion(String.valueOf(new Integer(1)));
			long valuationId = valuationDalImpl.persistValuation(valuationInfo);

			logger.info("Persisting Cashflows..... ");
			enrichCashflows(cashFlows, valuationInfo, valuationId);
			valuationDalImpl.persistCashFlows(cashFlows);

			logger.info("Persisting Comparables ..... ");
			List<PEComparableInfo> comparableInfos = new ArrayList<PEComparableInfo>();

			evbDataToComparableInfoMapper(peSecurityInfoDTO, evbList, calibrationId, valuationId, comparableInfos, systemGenComps);
			
			if(customizedCompsFound){
				logger.info("Removing customized set of comparables .... ");
				valuationDalImpl.removeCustomizedComps(peSecurityInfoDTO.getChannelId());
				/*logger.info("Performing updates ..... ");
				valuationDalImpl.updateComparableInfo(comparableInfos, peSecurityInfoDTO.getChannelId());*/
			} else {
				/*logger.info("Performing inserts ..... ");
				valuationDalImpl.persistComparableInfo(comparableInfos);*/
			}
			
			logger.info("Inserting set of Comparables with latest data ..... ");
			valuationDalImpl.persistComparableInfo(comparableInfos);
			logger.info("==============================================================================");

		} else if(valType.equalsIgnoreCase("ReCalibration")){			

			logger.info("Starting Re-Calibration ..... ");
			Date asOfDate = peSecurityInfoDTO.getTransactionDate();
			ImmutableDate asOfDateID = new ImmutableDate(asOfDate);
			
			BondDefinition bondDefinition = getBondDefination(peSecurityInfoDTO, asOfDateID);

			//boolean customizedCompsFound = valuationDalImpl.checkForCustomizedCompsByChannelId(peSecurityInfoDTO.getChannelId());
			
			logger.info("Fetching User Customized Comparables .....");
			logger.info("channelId : "+peSecurityInfoDTO.getChannelId());
			logger.info("securityVersion : "+peSecurityInfoDTO.getSecurityVersion());
			boolean customizedCompsFound = false;
			List<String> comps = new ArrayList<String>();
			comps = valuationDalImpl.getCompISINsByChannelIdAndIsNotCal(peSecurityInfoDTO.getChannelId());

			if(!comps.isEmpty()){
				customizedCompsFound = true;
			} else {
				logger.info("No Customized Comparables found ..... ");
				logger.info("Fetching Comparables from Active Calibration..... ");
				logger.info("parentChannelId : "+peSecurityInfoDTO.getParentChannelId());
				logger.info("activeCalVersion : "+peSecurityInfoDTO.getActiveCalVersion());
				comps = valuationDalImpl.getCompISINsByParentChannelIdAndCalVersion(peSecurityInfoDTO.getParentChannelId(), 
						peSecurityInfoDTO.getActiveCalVersion());

			}
			logger.info("Found Comparables , count : "+comps.size());
			
			if(comps.isEmpty()){
				throw new ValuationEngineException("No comparables found from previous Calibration, can't perform Calibration");
			}
			
			List<EVBData> evbList = new ArrayList<EVBData>();

			logger.info("Fetching Comparables ... ");
			
			EvbDataRefreshRequest evbDataRefreshRequest = new EvbDataRefreshRequest();
			//evbDataRefreshRequest.setType("ReCalibration");
			evbDataRefreshRequest.setAsOfDate(asOfDate);
			evbDataRefreshRequest.setCompList(comps);
			//evbDataRefreshRequest.setChannelId(peSecurityInfoDTO.getChannelId());
			//evbDataRefreshRequest.setParentChannelId(peSecurityInfoDTO.getParentChannelId());
			//evbDataRefreshRequest.setActiveCalVersion(peSecurityInfoDTO.getActiveCalVersion());
			
			evbList = getLatestCompsDetails(evbDataRefreshRequest);
			//evbList = getLatestCompsDetails(comps, peSecurityInfoDTO.getTransactionDate());

			if(CollectionUtils.isEmpty(evbList)){
				throw new ValuationEngineException("No market comparables found for Date :"+asOfDate+", using given security attributes and system pre-defined rules. Kindly add comparables or refine security attributes.");
			}
			
			logger.info("Found Comparables market data from EVB , count : "+evbList.size());
			
			logger.info("Calculating Comp YTM ..... ");
			double compYTM = calculateAvgCompMidYtm(evbList).doubleValue();

			Object sinkingFactor = qagApi.calculateSinkingFactor(bondDefinition, asOfDateID);		
			Object accrued = qagApi.calculateAccrued(bondDefinition, asOfDateID);

			double currentPrincipal = peSecurityInfoDTO.getStartingPrincipal().doubleValue()*(double)sinkingFactor;	
			
			logger.info("Calculating YTM from QAG liberaries ..... ");
			double purchaseValuePer = (100 * peSecurityInfoDTO.getTransactionPrice().doubleValue() / currentPrincipal);
			Object annualYTM = qagApi.calculateAnnualYTM(bondDefinition, asOfDateID, purchaseValuePer);
			
			logger.info("Calculating Day1 Spread ..... ");
			double day1Spread = (double)annualYTM*100-compYTM;

						

			logger.info("Ended Re-Calibration ..... ");

			logger.info("Starting Valuation ..... ");

			

			double accruedInterestValueD = currentPrincipal*(((double) accrued)/100);
			double accruedInterestPerD = (double) accrued;
			
			// Should be incorrect
			Object dirtyPricePer_1 = qagApi.calculateDirtyPrice(bondDefinition, asOfDateID, peSecurityInfoDTO.getTransactionPrice().doubleValue());
			Object cleanPricePer_1 = qagApi.calculateCleanPrice(bondDefinition, asOfDateID, peSecurityInfoDTO.getTransactionPrice().doubleValue());
			// this is simplified
			Object dirtyPricePer_2 = peSecurityInfoDTO.getTransactionPrice().doubleValue() + accruedInterestValueD;
			Object cleanPricePer_2 = peSecurityInfoDTO.getTransactionPrice().doubleValue();
			// Correct one from QAG
			Object dirtyPricePer = qagApi.calculateDirtyPrice(bondDefinition, asOfDateID, purchaseValuePer);
			Object cleanPricePer = qagApi.calculateCleanPrice(bondDefinition, asOfDateID, purchaseValuePer);

			double dirtyPriceValueD = currentPrincipal*(((double) dirtyPricePer)/100);
			double cleanPriceValueD = currentPrincipal*(((double) cleanPricePer)/100);
			double dirtyPricePerD = (double) dirtyPricePer;
			double cleanPricePerD = (double) cleanPricePer;

			
			logger.info("Ended Valuation ..... ");

			logger.info("Starting Cashflow calculation ..... ");
			List<PECashFlowInfo> cashFlows = calculateCashFlows((double)annualYTM, currentPrincipal, peSecurityInfoDTO, bondDefinition, asOfDateID);

			logger.info("Ended Cashflow calculation ..... ");

			logger.info("Reset last Active Calibration ..... ");
			valuationDalImpl.resetActiveCalibration(peSecurityInfoDTO.getParentChannelId());

			logger.info("Persisting Re-Calibration ..... ");
			PECalibrationInfo calibrationInfo = prepareCalibrationInfo(peSecurityInfoDTO, day1Spread);
			
			long calibrationId = valuationDalImpl.persistReCalibration(calibrationInfo);

			logger.info("Persisting First Valuation ..... ");
			BigDecimal day1SpreadD = doubleToBigDecimalConverter(day1Spread);
			
			PEValuationInfo valuationInfo = prepareValuationInfo(peSecurityInfoDTO, asOfDate, (double)annualYTM*100, compYTM,
					accruedInterestValueD, accruedInterestPerD, dirtyPriceValueD, cleanPriceValueD, dirtyPricePerD,
					cleanPricePerD, calibrationId, day1SpreadD);
			
			valuationInfo.setValVersion(String.valueOf(new Integer(1)));
			long valuationId = valuationDalImpl.persistValuation(valuationInfo);

			logger.info("Persisting Cashflows..... ");
			enrichCashflows(cashFlows, valuationInfo, valuationId);
			valuationDalImpl.persistCashFlows(cashFlows);

			logger.info("Persisting Comparables ..... ");
			List<PEComparableInfo> comparableInfos = new ArrayList<PEComparableInfo>();

			evbDataToComparableInfoMapper(peSecurityInfoDTO, evbList, calibrationId, valuationId, comparableInfos, false);

			if(customizedCompsFound){
				logger.info("Removing customized set of comparables .... ");
				valuationDalImpl.removeCustomizedComps(peSecurityInfoDTO.getChannelId());
				/*logger.info("Performing updates ..... ");
				valuationDalImpl.updateComparableInfo(comparableInfos, peSecurityInfoDTO.getChannelId());*/
			} else {
				/*logger.info("Performing inserts ..... ");
				valuationDalImpl.persistComparableInfo(comparableInfos);*/
			}				
			
			logger.info("Inserting set of Comparables with latest data ..... ");
			valuationDalImpl.persistComparableInfo(comparableInfos);
			
			logger.info("==============================================================================");
			
		} else if(valType.equalsIgnoreCase("Valuation")){
			
			Date asOfDate = peValuationRequest.getPeValuationInput().getValDate();
			ImmutableDate asOfDateID = new ImmutableDate(asOfDate);
			
			BondDefinition bondDefinition = getBondDefination(peSecurityInfoDTO, asOfDateID);
			
			logger.info("Starting Next Valuation ..... ");
			logger.info("Fetching Comparables from Active Calibration..... ");
			List<String> comps = new ArrayList<String>();
			logger.info("parentChannelId : "+peSecurityInfoDTO.getParentChannelId());
			logger.info("activeCalVersion : "+peSecurityInfoDTO.getActiveCalVersion());
			if(StringUtils.isBlank(peSecurityInfoDTO.getActiveCalVersion())){
				throw new ValuationEngineException("Perform calibration before doing valuations.");
			}
			comps = valuationDalImpl.getCompISINsByParentChannelIdAndCalVersion(peSecurityInfoDTO.getParentChannelId(), 
					peSecurityInfoDTO.getActiveCalVersion());

			logger.info("Found Comparables , count : "+comps.size());

			if(comps.isEmpty()){
				throw new ValuationEngineException("No comparables found from previous Calibration, can't perform Valuation");
			}
			
			List<EVBData> evbList = new ArrayList<EVBData>();

			logger.info("Fetching Comparables ... ");
			
			EvbDataRefreshRequest evbDataRefreshRequest = new EvbDataRefreshRequest();
			//evbDataRefreshRequest.setType("Valuation");
			evbDataRefreshRequest.setAsOfDate(peValuationRequest.getPeValuationInput().getValDate());
			evbDataRefreshRequest.setCompList(comps);
			//evbDataRefreshRequest.setParentChannelId(peSecurityInfoDTO.getParentChannelId());
			//evbDataRefreshRequest.setActiveCalVersion(peSecurityInfoDTO.getActiveCalVersion());
			
			evbList = getLatestCompsDetails(evbDataRefreshRequest);
			//evbList = getLatestCompsDetails(comps, peValuationRequest.getPeValuationInput().getValDate());
			
			if(CollectionUtils.isEmpty(evbList)){
				throw new ValuationEngineException("No market comparables found for Date :"+peValuationRequest.getPeValuationInput().getValDate()+", can't perform Valuation");
			}
			
			logger.info("Found Comparables market data from EVB , count : "+evbList.size());

			logger.info("Calculating Comp YTM ..... ");
			double compYTM = calculateAvgCompMidYtm(evbList).doubleValue();

			// get day 1 spread based on cal
			/*BigDecimal day1Spread = valuationDalImpl.getDay1SpreadByParentChannelIdAndCalVersion(peSecurityInfoDTO.getParentChannelId(), 
																								peSecurityInfoDTO.getActiveCalVersion());
			 */
			logger.info("Get Active Calibration details ..... ");
			PECalibrationInfo calibrationInfo = valuationDalImpl.getCalByParentChannelIdAndCalVersion(peSecurityInfoDTO.getParentChannelId(), 
					peSecurityInfoDTO.getActiveCalVersion());
			logger.info("Using Day1 Spread from Active Calibration ..... ");
			BigDecimal day1Spread = calibrationInfo.getDay1Spread();
			long calibrationId = calibrationInfo.getId();
			
			logger.info("Calculating YTM based on Day1 Spread and Comp YTM ..... ");
			double annualYTM = compYTM + day1Spread.doubleValue();

			logger.info("Starting Valuation ..... ");

			Object sinkingFactor = qagApi.calculateSinkingFactor(bondDefinition, asOfDateID);		
			Object accrued = qagApi.calculateAccrued(bondDefinition, asOfDateID);

			double currentPrincipal = peSecurityInfoDTO.getStartingPrincipal().doubleValue()*(double)sinkingFactor;					

			Object dirtyPricePer = qagApi.calculateDirtyPriceUsingAnnualYield(bondDefinition, asOfDateID, annualYTM/100);
			Object cleanPricePer = qagApi.calculateCleanPriceUsingAnnualYield(bondDefinition, asOfDateID, annualYTM/100);

			double dirtyPriceValueD = currentPrincipal*(((double) dirtyPricePer)/100);
			double cleanPriceValueD = currentPrincipal*(((double) cleanPricePer)/100);
			double dirtyPricePerD = (double) dirtyPricePer;
			double cleanPricePerD = (double) cleanPricePer;

			double accruedInterestValueD = currentPrincipal*(((double) accrued)/100);
			double accruedInterestPerD = (double) accrued;

			
			logger.info("Ended Valuation ..... ");

			logger.info("Starting Cashflow calculation ..... ");
			List<PECashFlowInfo> cashFlows = calculateCashFlows(annualYTM/100, currentPrincipal, peSecurityInfoDTO, bondDefinition, asOfDateID);

			logger.info("Ended Cashflow calculation ..... ");

			logger.info("Persisting Next Valuation ..... ");			
			
			PEValuationInfo valuationInfo = prepareValuationInfo(peSecurityInfoDTO, asOfDate, annualYTM, compYTM,
					accruedInterestValueD, accruedInterestPerD, dirtyPriceValueD, cleanPriceValueD, dirtyPricePerD,
					cleanPricePerD, calibrationId, day1Spread);			
			
			long valuationId = valuationDalImpl.persistNextValuation(valuationInfo);

			logger.info("Persisting Cashflows..... ");
			enrichCashflows(cashFlows, valuationInfo, valuationId);
			valuationDalImpl.persistCashFlows(cashFlows);

			logger.info("Persisting Comparables ..... ");
			List<PEComparableInfo> comparableInfos = new ArrayList<PEComparableInfo>();

			evbDataToComparableInfoMapper(peSecurityInfoDTO, evbList, calibrationId, valuationId, comparableInfos, false);

			valuationDalImpl.persistComparableInfo(comparableInfos);
			logger.info("==============================================================================");
		} else {
			throw new ValuationEngineException("Unknown request");
		}
}


	/**
	 * @param peSecurityInfoDTO
	 * @param bondDefinition
	 * @param asOfDateID
	 * @param valuationInfo
	 * @return
	 */
	private List<PECashFlowInfo> calculateCashFlows(double annualYtm, double currentPrincipal, PESecurityInfoDTO peSecurityInfoDTO, BondDefinition bondDefinition,
			ImmutableDate asOfDateID) {
		EventSequence eventSequence;
		try{
			eventSequence = EventsGenerator.inputEventSequence(bondDefinition);
		} catch(MarkitException e){
			throw new ValuationEngineException(e.getMessage());
		}
		
		MarketDataContainer container = getQAGMarketDataContainer(asOfDateID, eventSequence);
        
		CashflowEvaluationContext cashflowEvaluationContext = CashflowEvaluationContext.inputCashflowEvaluationContext
				(asOfDateID, asOfDateID, container);

		List<PECashFlowInfo> futureCashFlows = calculateFutureCashFlows(annualYtm, currentPrincipal, peSecurityInfoDTO, bondDefinition, eventSequence, cashflowEvaluationContext);
		List<PECashFlowInfo> paidCashFlows = calculatePaidCashFlows(peSecurityInfoDTO, bondDefinition, eventSequence, cashflowEvaluationContext);

		List<PECashFlowInfo> cashFlows = new ArrayList<PECashFlowInfo>();
		cashFlows.addAll(paidCashFlows);
		cashFlows.addAll(futureCashFlows);
		return cashFlows;
	}

	/**
	 * @param asOfDateID
	 * @param eventSequence
	 */
	private MarketDataContainer getQAGMarketDataContainer(ImmutableDate asOfDateID, EventSequence eventSequence) {
		List<IMarketDataTransferIdentifier> marketDataIdentifiers = eventSequence.getMarketDataDescription(asOfDateID);
        
        if(!marketDataIdentifiers.isEmpty()){
        	return MarketDataUtils.fillRequestedMarketData(marketDataIdentifiers);
        } else {
        	return MarketDataContainer.createEmptyMarketDataContainer();
        }
	}
	
	/**
	 * @param peSecurityInfoDTO
	 * @param day1Spread
	 * @return
	 */
	private PECalibrationInfo prepareCalibrationInfo(PESecurityInfoDTO peSecurityInfoDTO, double day1Spread) {
		PECalibrationInfo calibrationInfo = new PECalibrationInfo();
		calibrationInfo.setChannelId(peSecurityInfoDTO.getChannelId());
		calibrationInfo.setParentChannelId(peSecurityInfoDTO.getParentChannelId());
		calibrationInfo.setTransactionPrice(peSecurityInfoDTO.getTransactionPrice());
		calibrationInfo.setDay1Spread(doubleToBigDecimalConverter(day1Spread));	
		calibrationInfo.setCalDate(peSecurityInfoDTO.getTransactionDate());
		calibrationInfo.setIsActive(new Integer(1));
		return calibrationInfo;
	}
	
	/**
	 * @param peSecurityInfoDTO
	 * @param asOfDate
	 * @param annualYTM
	 * @param compYTM
	 * @param accruedInterestValueD
	 * @param accruedInterestPerD
	 * @param dirtyPriceValueD
	 * @param cleanPriceValueD
	 * @param dirtyPricePerD
	 * @param cleanPricePerD
	 * @param calibrationId
	 * @param day1SpreadD
	 * @return
	 */
	private PEValuationInfo prepareValuationInfo(PESecurityInfoDTO peSecurityInfoDTO, Date asOfDate, Double annualYTM,
			double compYTM, double accruedInterestValueD, double accruedInterestPerD, double dirtyPriceValueD,
			double cleanPriceValueD, double dirtyPricePerD, double cleanPricePerD, long calibrationId,
			BigDecimal day1SpreadD) {
		PEValuationInfo valuationInfo = new PEValuationInfo();			

		valuationInfo.setBenchmarkYtm(doubleToBigDecimalConverter(compYTM));
		valuationInfo.setYtm(doubleToBigDecimalConverter(annualYTM));
		valuationInfo.setDay1Spread(day1SpreadD);
		
		valuationInfo.setChannelId(peSecurityInfoDTO.getChannelId());

		valuationInfo.setCalculatedDirty(doubleToBigDecimalConverter(dirtyPriceValueD));
		valuationInfo.setCleanValue(doubleToBigDecimalConverter(cleanPriceValueD));
		valuationInfo.setDirtyPrice(doubleToBigDecimalConverter(dirtyPricePerD));
		valuationInfo.setCleanPrice(doubleToBigDecimalConverter(cleanPricePerD));				

		valuationInfo.setAccruedInterest(doubleToBigDecimalConverter(accruedInterestValueD));
		valuationInfo.setAccruedInterestRatio(doubleToBigDecimalConverter(accruedInterestPerD));

//			valuationInfo.setStartingPrincipalOfNextPaidCF(peSecurityInfoDTO.getStartingPrincipal().doubleValue());
//			valuationInfo.setStartingPrincipalOfNextFutureCF(currentPrincipal);
		valuationInfo.setValDate(asOfDate);
		valuationInfo.setCalibrationId(calibrationId);
		return valuationInfo;
	}

	/**
	 * @param peSecurityInfoDTO
	 * @param evbList
	 * @param calibrationId
	 * @param valuationId
	 * @param comparableInfos
	 */
	private void evbDataToComparableInfoMapper(PESecurityInfoDTO peSecurityInfoDTO, List<EVBData> evbList,
			long calibrationId, long valuationId, List<PEComparableInfo> comparableInfos, boolean systemGenComps) {
		for(EVBData evbData : evbList){
			PEComparableInfo comparableInfo = new PEComparableInfo();

			//comparableInfo.setCalVersion(calibrationInfo.getCalVersion());			
			comparableInfo.setChannelId(peSecurityInfoDTO.getChannelId());
			comparableInfo.setParentChannelId(peSecurityInfoDTO.getParentChannelId());
			//comparableInfo.setValVersion(valuationInfo.getValVersion());

			comparableInfo.setIssuer(evbData.getShortName());
			comparableInfo.setClassification(evbData.getClassification());
			comparableInfo.setCompSecId(evbData.getIsin());
			comparableInfo.setCoupon(evbData.getCoupon());
			comparableInfo.setCurrency(Currency.valueOf(evbData.getCurrency()));
			comparableInfo.setEvbFileDate(evbData.getDate());
			comparableInfo.setMaturityDate(evbData.getMaturityDate());
			comparableInfo.setMidPrice(evbData.getMidPrice());
			comparableInfo.setMidYTM(evbData.getMidYTM());
			comparableInfo.setRegion(evbData.getRegion());
			comparableInfo.setSector(evbData.getSectorLevel5());			

			comparableInfo.setCalibrationId(calibrationId);
			comparableInfo.setValuationId(valuationId);

			if(systemGenComps){
				comparableInfo.setSysGen(new Integer(1));
			}			
			comparableInfos.add(comparableInfo);
		}
	}
	
	/**
	 * @param peSecurityInfoDTO 
	 * @param valuationInfo 
	 * @param bondDefinition
	 * @param eventSequence 
	 * @param cashflowEvaluationContext 
	 * @return
	 */
	private List<PECashFlowInfo> calculateFutureCashFlows(double annualYtm, 
			double currentPrincipal, 
			PESecurityInfoDTO peSecurityInfoDTO, 
			BondDefinition bondDefinition, 
			EventSequence eventSequence, 
			CashflowEvaluationContext cashflowEvaluationContext) {
		double startingPrincipalOfNextFutureCF = currentPrincipal;
		double sumPvOfAllCFs = 0;
		ImmutableList<EvaluatedPayment> evaluatedPayments = calculateEvaluatedPayments(bondDefinition, eventSequence,
				cashflowEvaluationContext);
        
        List<PECashFlowInfo> futureCashFlows = new ArrayList<PECashFlowInfo>();
        
        for(EvaluatedPayment evaluatedPayment : evaluatedPayments){
        	PECashFlowInfo cashFlow = new PECashFlowInfo();
        	ImmutableDate accrualPeriodStartDate = qagApi.getAccrualPeriodStartDate(eventSequence, evaluatedPayment.getPaymentDate());
        	if(null != accrualPeriodStartDate){
        		cashFlow.setAccrualStartDate(accrualPeriodStartDate.getDate());
        	}        	
        	cashFlow.setStartingPrincipal(doubleToBigDecimalConverter(startingPrincipalOfNextFutureCF));
        	cashFlow.setPaymentDate(evaluatedPayment.getPaymentDate().getDate());
        	cashFlow.setPeriodicInterest(BigDecimal.valueOf((evaluatedPayment.getInterestAmount()/100)*peSecurityInfoDTO.getStartingPrincipal().doubleValue()).setScale( 5, RoundingMode.HALF_UP));
        	cashFlow.setInterestDue(doubleToBigDecimalConverter(evaluatedPayment.getInterestAmount()));
        	cashFlow.setPrincipalPayment(BigDecimal.valueOf((evaluatedPayment.getRedemptionAmount()/100)*peSecurityInfoDTO.getStartingPrincipal().doubleValue()).setScale( 5, RoundingMode.HALF_UP));
        	cashFlow.setYears(BigDecimal.valueOf(evaluatedPayment.getTimeToPayment()).setScale( 5, RoundingMode.HALF_UP));
        	
        	BigDecimal endingPrincipal = BigDecimal.valueOf(cashFlow.getStartingPrincipal().doubleValue() - (evaluatedPayment.getRedemptionAmount()/100)*peSecurityInfoDTO.getStartingPrincipal().doubleValue()).setScale( 5, RoundingMode.HALF_UP);
        	
        	cashFlow.setEndingPrincipal(endingPrincipal);
        	
        	//cashFlow.setDiscountFactor(calculateDiscountFactor_byStraightYTM(peValuation.getStraightYTM()/100, peSecurity.getCouponFreqInt(), evaluatedPayment.getTimeToPayment()));
        	
        	double discountFactor = qagApi.calculateDiscountFactorUsingAnnualYTM(annualYtm, CouponFrequencyQAGEnum.getCouponFrequencyEnum(peSecurityInfoDTO.getFrequency()).getNoCpns(), evaluatedPayment.getTimeToPayment());
        	cashFlow.setDiscountFactor(BigDecimal.valueOf(discountFactor).setScale( 5, RoundingMode.HALF_UP));
        	
        	double pvOfCf = cashFlow.getDiscountFactor().doubleValue()*((evaluatedPayment.getPaymentAmount()/100)*peSecurityInfoDTO.getStartingPrincipal().doubleValue());
        	
        	cashFlow.setPresentValueOfCf(BigDecimal.valueOf(pvOfCf).setScale( 5, RoundingMode.HALF_UP));
        	sumPvOfAllCFs = sumPvOfAllCFs + cashFlow.getPresentValueOfCf().doubleValue();
        	//valuationInfo.setSumPvOfAllCFs(valuationInfo.getSumPvOfAllCFs()+cashFlow.getPresentValueOfCf().doubleValue());
        	
        	startingPrincipalOfNextFutureCF = cashFlow.getEndingPrincipal().doubleValue();
        	//valuationInfo.setStartingPrincipalOfNextFutureCF(cashFlow.getEndingPrincipal().doubleValue());        	
        	int daysUntilCf = (int) Math.round(360*evaluatedPayment.getTimeToPayment());
        	cashFlow.setDaysUntilCf(daysUntilCf);
        	//cashFlow.setDaysUntilCf((int) (360*evaluatedPayment.getTimeToPayment()));
        	//cashFlow.setCouponRate(positionInfo.getCoupon().doubleValue());
        	
        	//cashFlow.setPeValuationInfo(valuationInfo);
        	//cashFlow.setValDate(valuationInfo.getValuationDate());
        	futureCashFlows.add(cashFlow);
        }
        
        
        return futureCashFlows;
	}

	/**
	 * @param bondDefinition
	 * @param eventSequence
	 * @param cashflowEvaluationContext
	 * @return
	 */
	private ImmutableList<EvaluatedPayment> calculateEvaluatedPayments(BondDefinition bondDefinition,
			EventSequence eventSequence, CashflowEvaluationContext cashflowEvaluationContext) {
		EvaluatedPaymentSchedules paymentSchedules;
		try {
			paymentSchedules = EvaluatedPaymentSchedules.inputEvaluatedPaymentSchedules
					(eventSequence, cashflowEvaluationContext, bondDefinition.getPrincipalFeature().getYieldConventionsHolder().getDayCountConventions());
		} catch (CalculationException e) {
			throw new ValuationEngineException(e.getMessage());
		}
        ImmutableList<EvaluatedPayment> evaluatedPayments = (ImmutableList<EvaluatedPayment>) paymentSchedules.getEvaluatedPayments();
		return evaluatedPayments;
	}
	
	/**
	 * @param peSecurityInfoDTO 
	 * @param valuationInfo 
	 * @param bondDefinition
	 * @param eventSequence 
	 * @param cashflowEvaluationContext 
	 * @return
	 */
	private List<PECashFlowInfo> calculatePaidCashFlows(PESecurityInfoDTO peSecurityInfoDTO,
			BondDefinition bondDefinition, 
			EventSequence eventSequence, 
			CashflowEvaluationContext cashflowEvaluationContext) {
		double startingPrincipalOfNextPaidCF = peSecurityInfoDTO.getStartingPrincipal().doubleValue();
		List<EvaluatedPayment> evaluatedPaidPayments = calculateEvaluatedPaidPayments(eventSequence,
				cashflowEvaluationContext);
                
        List<PECashFlowInfo> paidCashFlows = new ArrayList<PECashFlowInfo>();
        
        for(EvaluatedPayment evaluatedPayment : evaluatedPaidPayments){
        	PECashFlowInfo cashFlow = new PECashFlowInfo();
        	ImmutableDate accrualPeriodStartDate = qagApi.getAccrualPeriodStartDate(eventSequence, evaluatedPayment.getPaymentDate());
        	if(null != accrualPeriodStartDate){
        		cashFlow.setAccrualStartDate(accrualPeriodStartDate.getDate());
        	}         	
        	cashFlow.setStartingPrincipal(BigDecimal.valueOf(startingPrincipalOfNextPaidCF).setScale( 5, RoundingMode.HALF_UP));
        	cashFlow.setPaymentDate(evaluatedPayment.getPaymentDate().getDate());
        	cashFlow.setPeriodicInterest(BigDecimal.valueOf((evaluatedPayment.getInterestAmount()/100)*peSecurityInfoDTO.getStartingPrincipal().doubleValue()).setScale( 5, RoundingMode.HALF_UP));
        	cashFlow.setInterestDue(BigDecimal.valueOf(evaluatedPayment.getInterestAmount()).setScale( 5, RoundingMode.HALF_UP));
        	cashFlow.setPrincipalPayment(BigDecimal.valueOf((evaluatedPayment.getRedemptionAmount()/100)*peSecurityInfoDTO.getStartingPrincipal().doubleValue()).setScale( 5, RoundingMode.HALF_UP));
        	cashFlow.setYears(BigDecimal.valueOf(evaluatedPayment.getTimeToPayment()).setScale( 5, RoundingMode.HALF_UP));
        	
        	BigDecimal endingPrincipal = BigDecimal.valueOf(cashFlow.getStartingPrincipal().doubleValue() - (evaluatedPayment.getRedemptionAmount()/100)*peSecurityInfoDTO.getStartingPrincipal().doubleValue()).setScale( 5, RoundingMode.HALF_UP);
        	        	
        	cashFlow.setEndingPrincipal(endingPrincipal);
        	
        	int daysUntilCf = (int) Math.round(360*evaluatedPayment.getTimeToPayment());
        	cashFlow.setDaysUntilCf(daysUntilCf);
        	//cashFlow.setDaysUntilCf((int) (360*evaluatedPayment.getTimeToPayment()));
        	//cashFlow.setCouponRate(positionInfo.getCoupon().doubleValue());
        	startingPrincipalOfNextPaidCF = cashFlow.getEndingPrincipal().doubleValue();
        	//valuationInfo.setStartingPrincipalOfNextPaidCF(cashFlow.getEndingPrincipal().doubleValue());
        	
        	//cashFlow.setPeValuationInfo(valuationInfo);
        	//cashFlow.setValDate(valuationInfo.getValuationDate());
        	paidCashFlows.add(cashFlow);
        }
        
        
        return paidCashFlows;
	}

	/**
	 * @param eventSequence
	 * @param cashflowEvaluationContext
	 * @return
	 */
	private List<EvaluatedPayment> calculateEvaluatedPaidPayments(EventSequence eventSequence,
			CashflowEvaluationContext cashflowEvaluationContext) {
		PaidCashEvaluator paidCashEvaluator = PaidCashEvaluator.inputPaidCashEvaluator(eventSequence);
		List<EvaluatedPayment> evaluatedPaidPayments;
		try {
			evaluatedPaidPayments =  paidCashEvaluator.calculatePaidCashFlows(cashflowEvaluationContext);
		} catch (CalculationException e) {
			throw new ValuationEngineException(e.getMessage());
		}
		return evaluatedPaidPayments;
	}
	
	/**
	 * @param peSecurityInfoDTO
	 * @param asOfDateID
	 * @return
	 */
	private BondDefinition getBondDefination(PESecurityInfoDTO peSecurityInfoDTO, ImmutableDate asOfDateID){
		if(null != currencyHolidayCodeQAGMap && null != currencyHolidayCodeQAGMap.getNamespace() &&
				StringUtils.isNotBlank(peSecurityInfoDTO.getCurrency().toString()) &&
				StringUtils.isNotBlank(currencyHolidayCodeQAGMap.getNamespace().get(peSecurityInfoDTO.getCurrency().toString()))){
			String codesStr = currencyHolidayCodeQAGMap.getNamespace().get(peSecurityInfoDTO.getCurrency().toString());
			List<String> codesList = Arrays.asList(codesStr.split("\\s*,\\s*"));
			peSecurityInfoDTO.setHolidayCodes(codesList);
		}
		
		if(null != peSecurityInfoDTO.getPrincipalPaymentType() && 
				peSecurityInfoDTO.getPrincipalPaymentType().equals(PEConstants.PRINCIPAL_PAYMENT_TYPE_SINKING_FUND)){
			if(CollectionUtils.isNotEmpty(peSecurityInfoDTO.getRedemptionSchedules())){
				BondDefinition bondDefinitionInitial = qagApi.getBondDefination(peSecurityInfoDTO);
				
				EventSequence eventSequence = EventsGenerator.inputEventSequence(bondDefinitionInitial);
				
				MarketDataContainer container = getQAGMarketDataContainer(asOfDateID, eventSequence);
				
				CashflowEvaluationContext cashflowEvaluationContext = CashflowEvaluationContext.inputCashflowEvaluationContext
						(asOfDateID, asOfDateID, container);
				
				List<EvaluatedPayment> evaluatedPayments = new ArrayList<EvaluatedPayment>();
				evaluatedPayments.addAll((Collection<? extends EvaluatedPayment>) calculateEvaluatedPayments(bondDefinitionInitial, eventSequence, cashflowEvaluationContext));
				evaluatedPayments.addAll(calculateEvaluatedPaidPayments(eventSequence, cashflowEvaluationContext));
				
				List<RedemptionSchedule> repaymentSchedules = peSecurityInfoDTO.getRedemptionSchedules();
				
				for(EvaluatedPayment evaluatedPayment : evaluatedPayments){
					Date paymentDate = evaluatedPayment.getPaymentDate().getDate();
					for(RedemptionSchedule redSch : repaymentSchedules){
						if(paymentDate.compareTo(redSch.getRepaymentStartPeriod()) >= 0 && paymentDate.compareTo(redSch.getRepaymentEndPeriod()) <= 0){
							peSecurityInfoDTO.getRedemptionDateList().add(paymentDate);
							peSecurityInfoDTO.getRedemptionAmountList().add(redSch.getRepaymentPercent().doubleValue());
						}
					}
				}
			}
		}
		return qagApi.getBondDefination(peSecurityInfoDTO);
	}
	
	/**
	 * @param peSecurityInfoDTO
	 * @return
	 */
	public Map<String, Object> getCustomizedOrLatestUsedCompInfos(PESecurityInfoDTO peSecurityInfoDTO){
		
		Map<String, Object> results = new HashMap<String, Object>();
		
		List<PEComparableInfo> peComparableInfos = new ArrayList<PEComparableInfo>();
		
		logger.info("Fetching User Customized Comparables ..... ");
		peComparableInfos = valuationDalImpl.getCompInfosByParentChannelIdAndIsNotCal(peSecurityInfoDTO.getParentChannelId());
		
		if(peComparableInfos.isEmpty()){
			logger.info("No Customized Comparables found ..... ");
			logger.info("Fetching Comparables from Active Calibration..... ");
			peComparableInfos = valuationDalImpl.getCompInfosByParentChannelIdAndCalVersionForLatestUsed(peSecurityInfoDTO.getParentChannelId(), 
																			peSecurityInfoDTO.getActiveCalVersion());
		
		}
		
		results.put("resultsCount", peComparableInfos.size());
		results.put("content", peComparableInfos);
		logger.info("Number of Comparables returned: "+peComparableInfos.size());
		
		return results;
	}
	
	/**
	 * @param peSecurityInfoDTO
	 * @return
	 */
	public Map<String, Object> getSystemGeneratedCompInfos(PESecurityInfoDTO peSecurityInfoDTO){
		
		Map<String, Object> results = new HashMap<String, Object>();
		
		List<PEComparableInfo> peComparableInfos = new ArrayList<PEComparableInfo>();		

		logger.info("Fetching Comparables from First Calibration..... ");
		peComparableInfos = valuationDalImpl.getCompInfosByParentChannelIdAndCalVersionForSysGen(peSecurityInfoDTO.getParentChannelId());

		results.put("resultsCount", peComparableInfos.size());
		results.put("content", peComparableInfos);
		logger.info("Number of Comparables returned: "+peComparableInfos.size());
		
		return results;
	}

	public void customizeComparables(PECustomizeCompRequest customizeCompRequest) {
		PESecurityInfoDTO peSecurityInfoDTO = customizeCompRequest.getPeSecurityInfoDTO();
		List<PEComparableInfo> peComparableInfos = customizeCompRequest.getComparableInfos();
		for(PEComparableInfo peComparableInfo : peComparableInfos){
			peComparableInfo.setChannelId(peSecurityInfoDTO.getChannelId());
			peComparableInfo.setParentChannelId(peSecurityInfoDTO.getParentChannelId());
			peComparableInfo.setIsNotCalibrated(new Integer(1));
		}
		
		valuationDalImpl.persistCustomizedComps(peComparableInfos, peSecurityInfoDTO.getParentChannelId());
	}
	
	
	/**
	 * @param parentChannelId
	 * @return
	 */
	@Override
	public Map<String, Map> getValuationHistory(final Long parentChannelId){
		final Map<String, Map<String, Map<String, Object>>> historyMap = valuationDalImpl.getValuationHistory(parentChannelId);
		final Map<String, String> keyMap = valuationDalImpl.getLatestCalAndValKey(parentChannelId);
		
		final Map<String, Map> resultMap = new HashMap<String, Map>();
		resultMap.put("valuationHistoryMap", historyMap);
		resultMap.put("latestCalValKeyMap", keyMap);
		return resultMap;
	}
	
	/**
	 * @param parentChannelId
	 * @return
	 */
	public Map<String, String> getLatestCalAndValKey(long parentChannelId){
		Map<String, String> keyMap = valuationDalImpl.getLatestCalAndValKey(parentChannelId);
		return keyMap;
	}

	@Override
	public Map<String, Object> getPortfolioValuationStatus(PEPortfolio pePortfolio) {
		Map<String, Object> results = new HashMap<>();
			final Long portfolioId = pePortfolio.getId();
			logger.info("look for the portfolio id in the cache");
			boolean exists = inMemoryCacheClient.containskey(portfolioId.toString());
			if (exists){
				logger.info("Portfolio id exists in the cahce, look for the status in db");
			final PortfolioValuationUploadStatus portfolioValuationUploadStatus = pePortfolioValuationUploadStatusRepository
					.findByPePortfolioIdAndMaxUploadStatusId(portfolioId);
			if (portfolioValuationUploadStatus != null) {
				if (portfolioValuationUploadStatus.getPortfolioValuationStatus()
						.equals(PortfolioValuationStatus.IN_PROGRESS)) {
					results.put("uploadStatus", PortfolioValuationStatus.IN_PROGRESS);
					results.put("uploadMessage", PortfolioValuationConstants.PORT_VALUATION_IN_PROGRESS);
				}

				if (portfolioValuationUploadStatus.getPortfolioValuationStatus()
						.equals(PortfolioValuationStatus.DONE)) {
					results.put("uploadStatus", PortfolioValuationStatus.DONE);
					results.put("uploadMessage", PortfolioValuationConstants.PORT_VALUATION_DONE);
					logger.info("Clearing the cache as it has been doone");
					inMemoryCacheClient.remove(portfolioId.toString());
				}
				if (portfolioValuationUploadStatus.getPortfolioValuationStatus()
						.equals(PortfolioValuationStatus.DONE_WITH_EXCEPTIONS)) {
					results.put("uploadStatus", PortfolioValuationStatus.DONE_WITH_EXCEPTIONS);
					results.put("uploadMessage", PortfolioValuationConstants.PORT_VALUATION_DONE_WITH_EXCEPTIONS);
					logger.info("Clearing the cache as it has been doone with exceptions");
					inMemoryCacheClient.remove(portfolioId.toString());
				}
			}
			}
			return results;
		

	}
	
	@Override
	public Long persistPortfolioValuationStatusIndb(PEPortfolio portfolio) {
		logger.info("Persisting in started status in db");
		return portfolioValuationDao.persistPortfolioStatus(portfolio, PortfolioValuationStatus.IN_PROGRESS);
		
	}

	@Override
	public void persistPortfolioValuationStatusInCache(PEPortfolio portfolio) {
		final String portfolioId=portfolio.getId().toString();
		if(inMemoryCacheClient.containskey(portfolioId)){
			logger.info("Key is already present in cache");
		}
		else{
			logger.info("Inserting into the cache {}",portfolioId);
			inMemoryCacheClient.put(portfolioId);
		}
		
	}

	@Override
	public PEPortfolio fetchPortfolioForClient(PEClient client) {
		if(client!= null){
			final PEPortfolio pePortfolio=pePortfolioRepository.findByPortfolioName(client.getClientAbbrName());
			return pePortfolio;
		}
		return null;
		
	}
	@Override
	public List<RedemptionSchedule> getRedemptionScheduleByFiSecId(final Long fiSecId){
		logger.info("Fetching redemptionSchedule for the fisecid {}",fiSecId);
		final List<RedemptionSchedule> redemptionSchedules=redemptionScheduleRepository.getRedemptionScheduleByFiSecId(fiSecId);
		if(!redemptionSchedules.isEmpty() && redemptionSchedules != null){
		    return redemptionSchedules;
		}
		return null;
	}

	@Override
	public List<FloatingSecurityMargin> getFloatingSecurityMarginByFiSecId(Long fiSecId) {
		logger.info("Fetching FloatingSecurityMargin for the fisecid {}",fiSecId);
		final List<FloatingSecurityMargin> floatingSecurityMargins=floatingSecurityMarginRepository.getFloatingSecurityMarginByFiSecId(fiSecId);
		if(!floatingSecurityMargins.isEmpty() && floatingSecurityMargins != null){
		    return floatingSecurityMargins;
		}
		return null;
	
	}

}
