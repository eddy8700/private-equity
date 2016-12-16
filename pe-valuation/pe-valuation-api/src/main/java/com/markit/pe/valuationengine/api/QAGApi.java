package com.markit.pe.valuationengine.api;

import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.qag.analytics.bonds.events.EventSequence;
import com.markit.qag.analytics.bonds.referencedata.BondDefinition;
import com.markit.valuations.dates.ImmutableDate;

public interface QAGApi {

	public Object calculateSinkingFactor(BondDefinition bondDefinition, ImmutableDate asOfDate);

	public Object calculateAverageLife(BondDefinition bondDefinition, ImmutableDate asOfDate);

	public Object calculateDirtyPrice(BondDefinition bondDefinition, ImmutableDate asOfDate, double purchaseValue);

	public Object calculateCleanPrice(BondDefinition bondDefinition, ImmutableDate asOfDate, double purchaseValue);

	public Object calculateAccrued(BondDefinition bondDefinition, ImmutableDate asOfDate);

	public Object calculateStraightYTM(BondDefinition bondDefinition, ImmutableDate asOfDate, double purchaseValue)
			throws Exception;

	public Object calculateDirtyPriceUsingStraightYield(BondDefinition bondDefinition, ImmutableDate asOfDate,
			double yield) throws Exception;

	public Object calculateCleanPriceUsingStraightYield(BondDefinition bondDefinition, ImmutableDate asOfDate,
			double yield) throws Exception;

	public ImmutableDate getAccrualPeriodStartDate(EventSequence eventSequence, ImmutableDate evaluationDate);

	public Object calculateDirtyPriceUsingAnnualYield(BondDefinition bondDefinition, ImmutableDate asOfDate,
			double yield);

	public Object calculateCleanPriceUsingAnnualYield(BondDefinition bondDefinition, ImmutableDate asOfDate,
			double yield);

	public double calculateDiscountFactorUsingStraightYTM(double straightYTM, int couponFreq, double timeToPayment);

	double calculateDiscountFactorUsingAnnualYTM(double annualYTM, int couponFreq, double timeToPayment);

	BondDefinition getBondDefination(PESecurityInfoDTO peSecurityInfoDTO);

	Object calculateAnnualYTM(BondDefinition bondDefinition, ImmutableDate asOfDate, double purchaseValue);

}
