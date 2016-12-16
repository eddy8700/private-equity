package com.markit.pe.valuationengine.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.markit.pe.portfoliodata.PEComparableInfo;
import com.markit.pe.valuationengine.domain.PECalibrationInfo;
import com.markit.pe.valuationengine.domain.PECashFlowInfo;
import com.markit.pe.valuationengine.domain.PEValuationInfo;

public interface IValuationDAL {

	long persistCalibration(PECalibrationInfo calibrationInfo);

	void resetActiveCalibration(long parentChannelId);

	PECalibrationInfo getCalByParentChannelIdAndCalVersion(long parentChannelId, String calVersion);

	BigDecimal getDay1SpreadByParentChannelIdAndCalVersion(long parentChannelId, String calVersion);

	List<PEComparableInfo> getCompInfosByParentChannelIdAndCalVersionForLatestUsed(long parentChannelId, String calVersion);

	List<String> getCompISINsByParentChannelIdAndCalVersion(long parentChannelId, String calVersion);

	List<PEComparableInfo> getCompInfosByParentChannelIdAndIsNotCal(long parentChannelId);

	List<String> getCompISINsByChannelIdAndIsNotCal(long channelId);

	List<String> getCompList(long channelId, String calVersion);

	BigDecimal getDay1Spread(long channelId, String calVersion);

	//int[] updateComparableInfo(List<PEComparableInfo> comparableInfos);

	int[] persistComparableInfo(List<PEComparableInfo> comparableInfos);

	int[] persistCashFlows(List<PECashFlowInfo> cashFlowInfos);

	long persistNextValuation(PEValuationInfo valuationInfo);

	long persistValuation(PEValuationInfo valuationInfo);

	long persistReCalibration(PECalibrationInfo calibrationInfo);

	Map getValuationHistory(long parentChannelId);

	List<PEComparableInfo> getCompInfosByParentChannelIdAndCalVersionForSysGen(long parentChannelId);

	Map<String, String> getLatestCalAndValKey(long parentChannelId);

	boolean checkForCustomizedCompsByChannelId(long channelId);

	int[] persistCustomizedComps(List<PEComparableInfo> comparableInfos, Long parentChannelId);

	void removeCustomizedComps(long channelId);

	void markInitialCalibrationFailed(long channelId);

}
