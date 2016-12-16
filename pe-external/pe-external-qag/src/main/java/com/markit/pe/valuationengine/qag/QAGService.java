package com.markit.pe.valuationengine.qag;

import java.util.List;

import com.markit.qag.analytics.bonds.analyticssinglecalls.AnalyticsCalculationCaller;
import com.markit.qag.analytics.bonds.analyticssinglecalls.AnalyticsCallerFactory;
import com.markit.qag.analytics.bonds.marketdata.transfer.MarketDataUtils;
import com.markit.qag.analytics.bonds.referencedata.BondDefinition;
import com.markit.qag.analytics_api.bonds.analyticssinglecalls.QuoteType;
import com.markit.valuations.dates.ImmutableDate;

/**
 * @author aditya.gupta2
 *
 */
public class QAGService {
	
	/**
	 * @param analyticsCallerName
	 * @param bondDefinition
	 * @param asOfDate
	 * @return
	 * @throws Exception
	 */
	public static Object concreteCallerObject(final String analyticsCallerName,final BondDefinition bondDefinition,final ImmutableDate asOfDate) throws Exception
	{
		AnalyticsCalculationCaller analyticsCalculationCaller = AnalyticsCallerFactory
				.createConcreteCallerObject(analyticsCallerName, bondDefinition, asOfDate);
		return analyticsCalculationCaller.calculate(getQAGMarketDataContainer(asOfDate, analyticsCalculationCaller));

	}
	
	/**
	 * @param asOfDate
	 * @param analyticsCalculationCaller
	 */
	private static com.markit.qag.analytics_api.bonds.marketdata.MarketDataContainer getQAGMarketDataContainer(ImmutableDate asOfDate,
			AnalyticsCalculationCaller analyticsCalculationCaller) {
		List<com.markit.qag.analytics_api.bonds.marketdata.transfer.IMarketDataTransferIdentifier> marketDataIdentifiers = analyticsCalculationCaller.getMarketDataDescription(asOfDate);
		
		if(!marketDataIdentifiers.isEmpty()){
        	return MarketDataUtils.fillRequestedMarketData(marketDataIdentifiers);
        } else {
        	return com.markit.qag.analytics_api.bonds.marketdata.MarketDataContainer.createEmptyMarketDataContainer();
        }		
	}

	public static Object concreteCallerObject(String nAME, BondDefinition bondDefinition, ImmutableDate asOfDate,
			double purchaseValue, QuoteType cleanprice) throws Exception {
		AnalyticsCalculationCaller analyticsCalculationCaller = AnalyticsCallerFactory
				.createConcreteCallerObject(nAME, bondDefinition, asOfDate,purchaseValue,cleanprice);
		return analyticsCalculationCaller.calculate(getQAGMarketDataContainer(asOfDate, analyticsCalculationCaller));
	}
	
}