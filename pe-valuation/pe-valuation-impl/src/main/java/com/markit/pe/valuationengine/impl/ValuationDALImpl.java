
package com.markit.pe.valuationengine.impl;

import java.math.BigDecimal;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.markit.pe.portfoliodata.Currency;
import com.markit.pe.portfoliodata.PEComparableInfo;
import com.markit.pe.portfoliodata.constants.PEConstants;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.pe.valuationengine.api.IValuationDAL;
import com.markit.pe.valuationengine.domain.PECalibrationInfo;
import com.markit.pe.valuationengine.domain.PECashFlowInfo;
import com.markit.pe.valuationengine.domain.PEValuationInfo;

/**
 * @author mahesh.agarwal
 *
 */
@Component
public class ValuationDALImpl implements IValuationDAL{
	
	private static final Logger logger = LoggerFactory.getLogger(ValuationDALImpl.class);
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * @param calibrationInfo
	 * @return
	 */
	@Override
	public long persistCalibration(PECalibrationInfo calibrationInfo){

		String sql = "insert into T_FI_CALIBRATION_INFO(CHANNEL_ID, "
													+ "CAL_VERSION, "
													+ "CAL_DATE, "
													+ "TRANSACTION_PRICE, "
													+ "DAY1_SPREAD, "
													+ "IS_ACTIVE, "
													+ "PARENT_CHANNEL_ID)"
				+ " VALUES "
												+ "(:channelId, "
												+ ":calVersion, "
												+ ":calDate, "
												+ ":transactionPrice, "
												+ ":day1Spread, "
												+ ":isActive, "
												+ ":parentChannelId)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		logger.info("Querying : "+sql);
		
		int result = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(calibrationInfo), keyHolder);
		
		logger.info("Inserted Count : "+result);
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * @param calibrationInfo
	 * @return
	 */
	@Override
	public void resetActiveCalibration(long parentChannelId){

		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelId", parentChannelId);
		
		String sql = "UPDATE T_FI_CALIBRATION_INFO set IS_ACTIVE=NULL where PARENT_CHANNEL_ID=:parentChannelId";
		
		logger.info("Querying : "+sql);
		
		int result = namedParameterJdbcTemplate.update(sql, paramSource);
		
		logger.info("Updated Count : "+result);
	}
	
	/**
	 * @param calibrationInfo
	 * @return
	 */
	@Override
	public long persistReCalibration(PECalibrationInfo calibrationInfo){

		String sql = "insert into T_FI_CALIBRATION_INFO(CHANNEL_ID, "
													+ "CAL_VERSION, "
													+ "CAL_DATE, "
													+ "TRANSACTION_PRICE, "
													+ "DAY1_SPREAD, "
													+ "IS_ACTIVE, "
													+ "PARENT_CHANNEL_ID)"
				+ " VALUES "
												+ "(:channelId, "
												+ "(SELECT MAX(convert(int,cal.CAL_VERSION))+1 FROM T_FI_CALIBRATION_INFO cal where cal.PARENT_CHANNEL_ID = :parentChannelId group by cal.PARENT_CHANNEL_ID), "
												+ ":calDate, "
												+ ":transactionPrice, "
												+ ":day1Spread, "
												+ ":isActive, "
												+ ":parentChannelId)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		logger.info("Querying : "+sql);
		
		int result = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(calibrationInfo), keyHolder);
		
		logger.info("Inserted Count : "+result);
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * @param valuationInfo
	 * @return
	 */
	@Override
	public long persistValuation(PEValuationInfo valuationInfo){

		String sql = "insert into T_FI_VALUATION_INFO(FI_CALIBRATION_ID, "
													+ "CHANNEL_ID, "
													+ "VAL_VERSION, "
													+ "VALUATION_DATE, "
													+ "BENCHMARK_YTM, "
													+ "DAY_1_SPREAD, "
													+ "YTM, "
													+ "CALCULATED_DIRTY, "
													+ "ACCRUED_INTEREST, "
													+ "CLEAN_VALUE, "
													+ "DIRTY_PRICE, "
													+ "ACCRUED_INTEREST_RATIO, "
													+ "CLEAN_PRICE)"
						+ " VALUES "
													+ "(:calibrationId, "
													+ ":channelId, "
													+ ":valVersion, "
													+ ":valDate, "
													+ ":benchmarkYtm, "
													+ ":day1Spread, "
													+ ":ytm, "
													+ ":calculatedDirty, "
													+ ":accruedInterest, "
													+ ":cleanValue, "
													+ ":dirtyPrice, "
													+ ":accruedInterestRatio, "
													+ ":cleanPrice)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		logger.info("Querying : "+sql);
		
		int result = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(valuationInfo), keyHolder);
		
		logger.info("Inserted Count : "+result);
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * @param valuationInfo
	 * @return
	 */
	@Override
	public long persistNextValuation(PEValuationInfo valuationInfo){

		String sql = "insert into T_FI_VALUATION_INFO(FI_CALIBRATION_ID, "
													+ "CHANNEL_ID, "
													+ "VAL_VERSION, "
													+ "VALUATION_DATE, "
													+ "BENCHMARK_YTM, "
													+ "DAY_1_SPREAD, "
													+ "YTM, "
													+ "CALCULATED_DIRTY, "
													+ "ACCRUED_INTEREST, "
													+ "CLEAN_VALUE, "
													+ "DIRTY_PRICE, "
													+ "ACCRUED_INTEREST_RATIO, "
													+ "CLEAN_PRICE)"
						+ " VALUES "
													+ "(:calibrationId, "
													+ ":channelId, "
													+ "(SELECT MAX(convert(int,val.VAL_VERSION))+1 FROM T_FI_VALUATION_INFO val where val.FI_CALIBRATION_ID = :calibrationId group by val.FI_CALIBRATION_ID), "
													+ ":valDate, "
													+ ":benchmarkYtm, "
													+ ":day1Spread, "
													+ ":ytm, "
													+ ":calculatedDirty, "
													+ ":accruedInterest, "
													+ ":cleanValue, "
													+ ":dirtyPrice, "
													+ ":accruedInterestRatio, "
													+ ":cleanPrice)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		logger.info("Querying : "+sql);
		
		int result = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(valuationInfo), keyHolder);
		
		logger.info("Inserted Count : "+result);
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * @param cashFlowInfos
	 * @return
	 */
	@Override
	public int[] persistCashFlows(List<PECashFlowInfo> cashFlowInfos){

		String sql = "insert into T_FI_CASHFLOW(FI_VALUATION_ID, "
													+ "ACCRUAL_START_DATE, "
													+ "VAL_DATE, "
													+ "PAYMENT_DATE, "
													+ "STARTING_PRINCIPAL, "
													+ "[PERIODIC INTEREST], "
													+ "INTEREST_DUE, "
													+ "PRINCIPAL_PAYMENT, "
													+ "ACCRUED_INTEREST, "
													+ "DAYS_UNTIL_CF, "
													+ "YEARS, "
													+ "DISCOUNT_FACTOR, "
													+ "PV_OF_CF"
													+ ")"
						+ " VALUES "
													+ "(:valuationId, "
													+ ":accrualStartDate, "
													+ ":valDate, "
													+ ":paymentDate, "
													+ ":startingPrincipal, "
													+ ":periodicInterest, "
													+ ":interestDue, "
													+ ":principalPayment, "
													+ ":accruedInterest, "
													+ ":daysUntilCf, "
													+ ":years, "
													+ ":discountFactor, "
													+ ":presentValueOfCf"
													+ ")";
		
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(cashFlowInfos.toArray());
		
		logger.info("Querying : "+sql);
		
		int[] result = namedParameterJdbcTemplate.batchUpdate(sql, batch);
		
		logger.info("Inserted Count : "+result.length);
		return result;
	}
	
	/**
	 * @param comparableInfos
	 * @return
	 */
	@Override
	public int[] persistComparableInfo(List<PEComparableInfo> comparableInfos){
		String sql = "insert into T_FI_COMPARABLE_INFO([VAL_VERSION], "
													+ "[CHANNEL_ID], "
													+ "[PARENT_CHANNEL_ID], "
													+ "[CAL_VERSION], "
													+ "[COMP_SEC_ID], "
													+ "[ISSUER], "
													+ "[MATURITY_DATE], "
													+ "[CURRENCY], "
													+ "[RATING], "
													+ "[REGION], "
													+ "[SECTOR], "
													+ "[CLASSIFICATION], "
													+ "[EVB_FILE_DATE], "
													+ "[MID_YTM], "
													+ "[COUPON], "
													+ "[MID_PRICE], "
													+ "[IS_NOT_CALIBRATED], "
													+ "[SYS_GEN]"
													+ ")"
						+ " VALUES "
													+ "((select val.VAL_VERSION from T_FI_VALUATION_INFO val where val.FI_VALUATION_ID=:valuationId), "
													+ ":channelId, "
													+ ":parentChannelId, "
													+ "(select cal.CAL_VERSION from T_FI_CALIBRATION_INFO cal where cal.FI_CALIBRATION_ID=:calibrationId), "
													+ ":compSecId, "
													+ ":issuer, "
													+ ":maturityDate, "
													+ ":currency, "
													+ ":rating, "
													+ ":region, "
													+ ":sector, "
													+ ":classification, "
													+ ":evbFileDate, "
													+ ":midYTM, "
													+ ":coupon, "
													+ ":midPrice, "
													+ ":isNotCalibrated, "
													+ ":sysGen"
													+ ")";
		
		
		
		SqlParameterSource[] batch = ValuationDALImpl.createBatch(comparableInfos.toArray());
		
		logger.info("Querying : "+sql);
		
		int[] result = namedParameterJdbcTemplate.batchUpdate(sql, batch);
		
		logger.info("Inserted Count : "+result.length);
		return result;
	}
	
	public static SqlParameterSource[] createBatch(Object[] beans) {
		BeanPropertySqlParameterSource[] batch = new BeanPropertySqlParameterSource[beans.length];
		for (int i = 0; i < beans.length; i++) {
			Object bean = beans[i];
			batch[i] = new BeanPropertySqlParameterSource(bean);
			batch[i].registerSqlType("currency", Types.VARCHAR);
		}
		return batch;
	}
	
	@Override
	public int[] persistCustomizedComps(List<PEComparableInfo> comparableInfos, Long parentChannelId){
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelId", parentChannelId);
		
		String deleteSql = "delete from T_FI_COMPARABLE_INFO where PARENT_CHANNEL_ID=:parentChannelId and IS_NOT_CALIBRATED is not NULL";
		
		logger.info("Querying : "+deleteSql);
		
		int deletedCount = namedParameterJdbcTemplate.update(deleteSql, paramSource);
		
		logger.info("Deleted Count : "+deletedCount);
		
		String insertSql = "insert into T_FI_COMPARABLE_INFO("
													//+ "[VAL_VERSION], "
													+ "[CHANNEL_ID], "
													+ "[PARENT_CHANNEL_ID], "
													//+ "[CAL_VERSION], "
													+ "[COMP_SEC_ID], "
													+ "[ISSUER], "
													+ "[MATURITY_DATE], "
													+ "[CURRENCY], "
													+ "[RATING], "
													+ "[REGION], "
													+ "[SECTOR], "
													+ "[CLASSIFICATION], "
													+ "[EVB_FILE_DATE], "
													+ "[MID_YTM], "
													+ "[COUPON], "
													+ "[MID_PRICE], "
													+ "[IS_NOT_CALIBRATED]"
													+ ")"
									+ " VALUES ("
													//+ "(select val.VAL_VERSION from T_FI_VALUATION_INFO val where val.FI_VALUATION_ID=:valuationId), "
													+ ":channelId, "
													+ ":parentChannelId, "
													//+ "(select cal.CAL_VERSION from T_FI_CALIBRATION_INFO cal where cal.FI_CALIBRATION_ID=:calibrationId), "
													+ ":compSecId, "
													+ ":issuer, "
													+ ":maturityDate, "
													+ ":currency, "
													+ ":rating, "
													+ ":region, "
													+ ":sector, "
													+ ":classification, "
													+ ":evbFileDate, "
													+ ":midYTM, "
													+ ":coupon, "
													+ ":midPrice, "
													+ ":isNotCalibrated"
													+ ")";
		
		SqlParameterSource[] batch = ValuationDALImpl.createBatch(comparableInfos.toArray());
		
		logger.info("Querying : "+insertSql);
		
		int[] result = namedParameterJdbcTemplate.batchUpdate(insertSql, batch);
		
		logger.info("Inserted Count : "+result.length);
		
		return result;
	}

	/**
	 * @param channelId
	 */
	@Override
	public void removeCustomizedComps(long channelId) {
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("channelId", channelId);
		
		String deleteSql = "delete from T_FI_COMPARABLE_INFO where CHANNEL_ID = :channelId and IS_NOT_CALIBRATED = 1";
		
		logger.info("Querying : "+deleteSql);
		
		int deletedCount = namedParameterJdbcTemplate.update(deleteSql, paramSource);
		
		logger.info("Deleted Count : "+deletedCount);
	}
	
	/**
	 * @param channelId
	 * @param calVersion
	 * @return
	 */
	@Override
	public BigDecimal getDay1Spread(long channelId, String calVersion){
		
		/*Map namedParameters = new HashMap();
		namedParameters.put("channelId", channelId);
		namedParameters.put("calVersion", calVersion);*/
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("channelId", channelId)
				.addValue("calVersion", calVersion);
		
		
		String sql = "select DAY1_SPREAD from T_FI_CALIBRATION_INFO where "
												+ "CHANNEL_ID=:channelId "
												+ "and "
												+ "CAL_VERSION=:calVersion";
		
		//BigDecimal day1Spread = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, BigDecimal.class);
		logger.info("Querying : "+sql);
		
		BigDecimal day1Spread = namedParameterJdbcTemplate.queryForObject(sql, paramSource, BigDecimal.class);
		
		logger.info("Result : "+day1Spread);
		return day1Spread;
	}
	
	/**
	 * @param channelId
	 * @param calVersion
	 * @return
	 */
	@Override
	public List<String> getCompList(long channelId, String calVersion){
		List<String> compList = new ArrayList<String>();
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("channelId", channelId)
				.addValue("calVersion", calVersion);
		
		String sql = "select distinct COMP_SEC_ID from T_FI_COMPARABLE_INFO where "
												+ "CHANNEL_ID=:channelId "
												+ "and "
												+ "CAL_VERSION=:calVersion";
		
		logger.info("Querying : "+sql);
		
		compList = namedParameterJdbcTemplate.queryForList(sql, paramSource, String.class);
		
		logger.info("Result Size : "+compList.size());
		return compList;
	}
	
	/**
	 * @param channelId
	 * @param calVersion
	 * @return
	 */
	@Override
	public boolean checkForCustomizedCompsByChannelId(long channelId){
		String compSecId = null;
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("channelId", channelId)
				.addValue("isNotCal", new Integer(1));
		
		String sql = "select top 1 COMP_SEC_ID from T_FI_COMPARABLE_INFO where "
												+ "CHANNEL_ID=:channelId "
												+ "and "
												+ "IS_NOT_CALIBRATED=:isNotCal";
		
		logger.info("Querying : "+sql);
		try{
			compSecId = namedParameterJdbcTemplate.queryForObject(sql, paramSource, String.class);
		}  catch (EmptyResultDataAccessException eRDAE) {
			logger.error(eRDAE.getMessage());
		}  catch (DataAccessException dAE) {
			logger.error(dAE.getMessage());
		}
		logger.info("Result "+compSecId);
		
		return StringUtils.isNotBlank(compSecId);
	}
	
	
	/**
	 * @param channelId
	 * @param calVersion
	 * @return
	 */
	@Override
	public List<String> getCompISINsByChannelIdAndIsNotCal(long channelId){
		List<String> compList = new ArrayList<String>();
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("channelId", channelId)
				.addValue("isNotCal", new Integer(1));
		
		String sql = "select COMP_SEC_ID from T_FI_COMPARABLE_INFO where "
												+ "CHANNEL_ID=:channelId "
												+ "and "
												+ "IS_NOT_CALIBRATED=:isNotCal";
		
		logger.info("Querying : "+sql);
		
		compList = namedParameterJdbcTemplate.queryForList(sql, paramSource, String.class);
		
		logger.info("Result Size : "+compList.size());
		return compList;
	}
	
	/**
	 * @param channelId
	 * @param calVersion
	 * @return
	 */
	@Override
	public List<PEComparableInfo> getCompInfosByParentChannelIdAndIsNotCal(long parentChannelId){
		List<PEComparableInfo> compList = new ArrayList<PEComparableInfo>();
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelId", parentChannelId)
				.addValue("isNotCal", new Integer(1));
		
		String sql = "select MID_YTM AS midYTM,* from T_FI_COMPARABLE_INFO where "
												+ "PARENT_CHANNEL_ID=:parentChannelId "
												+ "and "
												+ "IS_NOT_CALIBRATED=:isNotCal";
		
		logger.info("Querying : "+sql);
		
		compList = namedParameterJdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper<PEComparableInfo>(PEComparableInfo.class));
		
		logger.info("Result Size : "+compList.size());
		return compList;
	}
	
	/**
	 * @param channelId
	 * @param calVersion
	 * @return
	 */
	@Override
	public List<String> getCompISINsByParentChannelIdAndCalVersion(long parentChannelId, String calVersion){
		List<String> compList = new ArrayList<String>();
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelId", parentChannelId)
				.addValue("calVersion", calVersion);
		
		String sql = "select distinct COMP_SEC_ID from T_FI_COMPARABLE_INFO where "
												+ "PARENT_CHANNEL_ID=:parentChannelId "
												+ "and "
												+ "CAL_VERSION=:calVersion";
		
		logger.info("Querying : "+sql);
		
		compList = namedParameterJdbcTemplate.queryForList(sql, paramSource, String.class);
		
		logger.info("Result Size : "+compList.size());
		return compList;
	}
	
	/**
	 * @param channelId
	 * @param calVersion
	 * @return
	 */
	@Override
	public List<PEComparableInfo> getCompInfosByParentChannelIdAndCalVersionForLatestUsed(long parentChannelId, String calVersion){
		List<PEComparableInfo> compList = new ArrayList<PEComparableInfo>();
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelId", parentChannelId)
				.addValue("calVersion", calVersion);
		
		String sql = "select MID_YTM AS midYTM,* from T_FI_COMPARABLE_INFO where "
												+ "PARENT_CHANNEL_ID=:parentChannelId "
												+ "and "
												+ "CAL_VERSION=:calVersion "
												+ "and "
												+ "VAL_VERSION = (select max(val.VAL_VERSION) "
													+ "from T_FI_COMPARABLE_INFO val "
													+ "where "
													+ "val.PARENT_CHANNEL_ID=:parentChannelId "
													+ "and "
													+ "val.CAL_VERSION=:calVersion)"
													;
		
		logger.info("Querying : "+sql);
		
		compList = namedParameterJdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper<PEComparableInfo>(PEComparableInfo.class));
		
		logger.info("Result Size : "+compList.size());
		return compList;
	}
	
	/**
	 * @param channelId
	 * @param calVersion
	 * @return
	 */
	@Override
	public List<PEComparableInfo> getCompInfosByParentChannelIdAndCalVersionForSysGen(long parentChannelId){
		List<PEComparableInfo> compList = new ArrayList<PEComparableInfo>();
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelId", parentChannelId)
				.addValue("calVersion", String.valueOf(1))
				.addValue("sysGen", new Integer(1));
		
		String sql = "select MID_YTM AS midYTM,* from T_FI_COMPARABLE_INFO where "
												+ "PARENT_CHANNEL_ID=:parentChannelId "
												+ "and "
												+ "CAL_VERSION=:calVersion "
												+ "and SYS_GEN=:sysGen "
												+ "and "
												+ "VAL_VERSION = (select min(val.VAL_VERSION) "
													+ "from T_FI_COMPARABLE_INFO val "
													+ "where "
													+ "val.PARENT_CHANNEL_ID=:parentChannelId "
													+ "and "
													+ "val.CAL_VERSION=:calVersion)"
													;
		
		logger.info("Querying : "+sql);
		
		compList = namedParameterJdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper<PEComparableInfo>(PEComparableInfo.class));
		
		logger.info("Result Size : "+compList.size());
		return compList;
	}
	
	/**
	 * @param channelId
	 * @param calVersion
	 * @return
	 */
	@Override
	public BigDecimal getDay1SpreadByParentChannelIdAndCalVersion(long parentChannelId, String calVersion){
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelId", parentChannelId)
				.addValue("calVersion", calVersion);
		
		
		String sql = "select DAY1_SPREAD from T_FI_CALIBRATION_INFO where "
												+ "PARENT_CHANNEL_ID=:parentChannelId "
												+ "and "
												+ "CAL_VERSION=:calVersion";
		
		//BigDecimal day1Spread = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, BigDecimal.class);
		
		logger.info("Querying : "+sql);
		
		BigDecimal day1Spread = namedParameterJdbcTemplate.queryForObject(sql, paramSource, BigDecimal.class);
		
		logger.info("Result : "+day1Spread);
		return day1Spread;
	}
	
	/**
	 * @param channelId
	 * @param calVersion
	 * @return
	 */
	@Override
	public PECalibrationInfo getCalByParentChannelIdAndCalVersion(long parentChannelId, String calVersion){
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelId", parentChannelId)
				.addValue("calVersion", calVersion);
		
		
		String sql = "select "
						+ "cal.FI_CALIBRATION_ID AS id, "
						+ "cal.CAL_VERSION AS calVersion, "
						+ "cal.CHANNEL_ID AS channelId, "
						+ "cal.CAL_DATE AS calDate, "
						+ "cal.TRANSACTION_PRICE AS transactionPrice, "
						+ "cal.DAY1_SPREAD AS day1Spread, "
						+ "cal.DAY_CONVENTION AS dayConvention, "
						+ "cal.IS_ACTIVE AS isActive, "
						+ "cal.PARENT_CHANNEL_ID AS parentChannelId "
						+ "from T_FI_CALIBRATION_INFO cal where "
												+ "cal.PARENT_CHANNEL_ID=:parentChannelId "
												+ "and "
												+ "cal.CAL_VERSION=:calVersion";
		
		logger.info("Querying : "+sql);
		
		PECalibrationInfo peCalibrationInfo = (PECalibrationInfo)namedParameterJdbcTemplate.queryForObject(
				sql, paramSource, new BeanPropertyRowMapper<PECalibrationInfo>(PECalibrationInfo.class));
		
		logger.info("Result : "+peCalibrationInfo);
		return peCalibrationInfo;
	}
	
	@Override
	public Map<String, Map<String, Map<String, Object>>> getValuationHistory(long parentChannelId){
		
		Map<String, Map<String, Map<String, Object>>> calMap = new TreeMap<String, Map<String, Map<String, Object>>>();
		
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelId", parentChannelId);
		
		String sql = "select "
					+ "cal.CAL_VERSION AS calVersion, "
					+ "cal.IS_ACTIVE AS isActive, "
					+ "cal.CAL_DATE AS calDate, "
					+ "cal.DAY1_SPREAD AS day1Spread, "
					+ "val.VALUATION_DATE AS valDate, "
					+ "val.VAL_VERSION AS valVersion, "
					+ "val.BENCHMARK_YTM AS benchmarkYtm, "
					+ "val.YTM AS ytm, "
					+ "val.CALCULATED_DIRTY AS calcDirty, "
					+ "val.ACCRUED_INTEREST AS accInterest, "
					+ "val.CLEAN_VALUE AS cleanValue, "
					+ "val.DIRTY_PRICE AS dirtyPrice, "
					+ "val.ACCRUED_INTEREST_RATIO AS accInterestRatio, "
					+ "val.CLEAN_PRICE AS cleanPrice, "
					+ "sec.SEC_ID AS secId, "
					+ "sec.SEC_VERSION AS secVersion, "
					+ "sec.ISSUE_DATE AS issueDate, "
					+ "sec.SEC_NAME AS secName, "
					+ "sec.CURRENCY AS currency, "
					+ "sec.PAR_VALUE AS startingPrincipal, "
					+ "sec.COUPON_FREQUENCY AS frequency, "
					+ "sec.COUPON_RATE AS coupon, "
					+ "sec.MATURITY_DATE AS maturityDate, "
					+ "sec.SECTOR AS sector, "
					+ "sec.CLASSIFICATION AS classification, "
					+ "sec.TYPE AS type, "
					+ "sec.TRANSACTION_PRICE AS transactionPrice, "
					+ "sec.TRANSACTION_DATE AS transactionDate, "
					+ "sec.DAY_COUNT_CONV AS dayCountConvention, "
					+ "sec.BUSINESS_DAY_CONV AS businessDayConvention, "
					+ "sec.NEXT_PAYMENT_DATE AS nextPaymentDate, "
					+ "sec.MARGIN AS margin, "
					+ "sec.COUPON_BENCHMARK AS couponBenchmark, "
					+ "cf.ACCRUAL_START_DATE AS accrualStartDate, "
					+ "cf.PAYMENT_DATE AS paymentDate, "
					+ "cf.STARTING_PRINCIPAL AS startingPrincipal, "
					+ "cf.[PERIODIC INTEREST] AS periodicInterest, "
					+ "cf.INTEREST_DUE AS interestDue, "
					+ "cf.PRINCIPAL_PAYMENT AS principalPayment, "
					+ "cf.ACCRUED_INTEREST AS accruedInterest, "
					+ "cf.DAYS_UNTIL_CF AS daysUntilCf, "
					+ "cf.YEARS AS years, "
					+ "cf.DISCOUNT_FACTOR AS discountFactor, "
					+ "cf.PV_OF_CF AS presentValueOfCf "					
					+ "from "
					+ "T_FI_CALIBRATION_INFO cal, "
					+ "T_FI_VALUATION_INFO val, "
					+ "T_FI_SEC_DETAILS sec, "
					+ "T_PORTFOLIO_SEC_INFO psec, "
					+ "T_FI_CASHFLOW cf "
					+ "where "
					+ "cal.PARENT_CHANNEL_ID=:parentChannelId "
					+ "and "
					+ "cal.FI_CALIBRATION_ID = val.FI_CALIBRATION_ID "
					+ "and "
					+ "val.CHANNEL_ID = psec.CHANNEL_ID "
					+ "and psec.FI_SEC_ID = sec.FI_SEC_ID "
					+ "and val.FI_VALUATION_ID = cf.FI_VALUATION_ID";		
		
		logger.info("Querying : "+sql);
		
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(sql, paramSource);
		
		for(Map<String, Object> obj : rows){
			
			//String calKey = "CalVersion-"+obj.get("calVersion")+"_"+obj.get("calDate");
			String calKey = (String) obj.get("calVersion");
			
			if(calMap.containsKey(calKey)){
				Map<String, Map<String, Object>> valMap = calMap.get(calKey);
				
				//String valKey = "ValVersion-"+obj.get("valVersion")+"_"+obj.get("valDate");
				
				//String valKey = obj.get("valVersion")+"_"+obj.get("valDate");
				String valKey = obj.get("valVersion")+"_"+new SimpleDateFormat(PEConstants.PE_DEFAULT_DATE_FORMAT).format(new Date(((java.sql.Timestamp)obj.get("valDate")).getTime()));
				
				if(valMap.containsKey(valKey)){
					Map<String, Object> valData = valMap.get(valKey);
					
					PECashFlowInfo cfInfo =  new PECashFlowInfo();					
					//cfInfo.setAccrualStartDate((Date) obj.get("accrualStartDate"));
					cfInfo.setAccrualStartDate(new Date(((java.sql.Timestamp)obj.get("accrualStartDate")).getTime()));
					//cfInfo.setPaymentDate((Date) obj.get("paymentDate"));
					cfInfo.setPaymentDate(new Date(((java.sql.Timestamp)obj.get("paymentDate")).getTime()));
					cfInfo.setStartingPrincipal((BigDecimal) obj.get("startingPrincipal"));
					cfInfo.setPeriodicInterest( (BigDecimal) obj.get("periodicInterest"));
					cfInfo.setInterestDue( (BigDecimal) obj.get("interestDue"));
					cfInfo.setPrincipalPayment((BigDecimal) obj.get("principalPayment"));
					cfInfo.setAccruedInterest((BigDecimal) obj.get("accruedInterest"));
					cfInfo.setDaysUntilCf((Integer) obj.get("daysUntilCf"));
					cfInfo.setYears((BigDecimal) obj.get("years"));
					cfInfo.setDiscountFactor((BigDecimal) obj.get("discountFactor"));
					cfInfo.setPresentValueOfCf((BigDecimal) obj.get("presentValueOfCf"));
					
					List<PECashFlowInfo> cfInfoList = (List<PECashFlowInfo>) valData.get("cfInfoList");
					cfInfoList.add(cfInfo);
					
				} else {					
					PEValuationInfo valInfo = new PEValuationInfo();
					PESecurityInfoDTO secInfo = new PESecurityInfoDTO();
					PECashFlowInfo cfInfo =  new PECashFlowInfo();
					List<PECashFlowInfo> cfInfoList = new ArrayList<PECashFlowInfo>();
					
					Map<String, Object> valData = new HashMap<String, Object>();
					
					valInfo.setDay1Spread((BigDecimal) obj.get("day1Spread"));
					valInfo.setValVersion((String) obj.get("valVersion"));
					//valInfo.setValDate((Date) obj.get("valDate"));
					valInfo.setValDate(new Date(((java.sql.Timestamp)obj.get("valDate")).getTime()));
					valInfo.setBenchmarkYtm((BigDecimal) obj.get("benchmarkYtm"));
					valInfo.setYtm((BigDecimal) obj.get("ytm"));
					valInfo.setCalculatedDirty((BigDecimal) obj.get("calcDirty"));
					valInfo.setAccruedInterest((BigDecimal) obj.get("accInterest"));
					valInfo.setCleanValue((BigDecimal) obj.get("cleanValue"));
					valInfo.setDirtyPrice((BigDecimal) obj.get("dirtyPrice"));
					valInfo.setAccruedInterestRatio((BigDecimal) obj.get("accInterestRatio"));
					valInfo.setCleanPrice((BigDecimal) obj.get("cleanPrice"));
					
					secInfo.setSecurityId((String) obj.get("secId"));
					secInfo.setSecurityVersion(((Long) obj.get("secVersion")).intValue());
					secInfo.setIssueDate(new Date(((java.sql.Timestamp)obj.get("issueDate")).getTime()));
					secInfo.setSecurityName((String) obj.get("secName"));
					secInfo.setCurrency(Currency.valueOf((String) obj.get("currency")));
					secInfo.setStartingPrincipal((BigDecimal) obj.get("startingPrincipal"));
					secInfo.setFrequency((String) obj.get("frequency"));
					secInfo.setCoupon((BigDecimal) obj.get("coupon"));
					secInfo.setMaturityDate(new Date(((java.sql.Timestamp)obj.get("maturityDate")).getTime()));
					secInfo.setSector((String) obj.get("sector"));
					secInfo.setClassification((String) obj.get("classification"));
					secInfo.setType((String) obj.get("type"));
					secInfo.setTransactionPrice((BigDecimal) obj.get("transactionPrice"));
					secInfo.setTransactionDate(new Date(((java.sql.Timestamp)obj.get("transactionDate")).getTime()));
					secInfo.setDayCountConvention((String) obj.get("dayCountConvention"));
					secInfo.setBusinessDayConvention((String) obj.get("businessDayConvention"));
					secInfo.setNextPaymentDate(new Date(((java.sql.Timestamp)obj.get("nextPaymentDate")).getTime()));
					secInfo.setMargin((BigDecimal) obj.get("margin"));
					secInfo.setCouponBenchmark((String) obj.get("couponBenchmark"));
					
					cfInfo.setAccrualStartDate(new Date(((java.sql.Timestamp)obj.get("accrualStartDate")).getTime()));
					cfInfo.setPaymentDate(new Date(((java.sql.Timestamp)obj.get("paymentDate")).getTime()));
					cfInfo.setStartingPrincipal((BigDecimal) obj.get("startingPrincipal"));
					cfInfo.setPeriodicInterest( (BigDecimal) obj.get("periodicInterest"));
					cfInfo.setInterestDue( (BigDecimal) obj.get("interestDue"));
					cfInfo.setPrincipalPayment((BigDecimal) obj.get("principalPayment"));
					cfInfo.setAccruedInterest((BigDecimal) obj.get("accruedInterest"));
					cfInfo.setDaysUntilCf((Integer) obj.get("daysUntilCf"));
					cfInfo.setYears((BigDecimal) obj.get("years"));
					cfInfo.setDiscountFactor((BigDecimal) obj.get("discountFactor"));
					cfInfo.setPresentValueOfCf((BigDecimal) obj.get("presentValueOfCf"));
					
					cfInfoList.add(cfInfo);
					
					valData.put("valInfo", valInfo);
					valData.put("secInfo", secInfo);
					valData.put("cfInfoList", cfInfoList);
					
					valMap.put(valKey, valData);
				}
			} else {
				//String valKey = "ValVersion-"+obj.get("valVersion")+"_"+obj.get("valDate");
				//String valKey = obj.get("valVersion")+"_"+obj.get("valDate");
				String valKey = obj.get("valVersion")+"_"+new SimpleDateFormat(PEConstants.PE_DEFAULT_DATE_FORMAT).format(new Date(((java.sql.Timestamp)obj.get("valDate")).getTime()));
				
				
				PEValuationInfo valInfo = new PEValuationInfo();
				PESecurityInfoDTO secInfo = new PESecurityInfoDTO();
				PECashFlowInfo cfInfo =  new PECashFlowInfo();
				List<PECashFlowInfo> cfInfoList = new ArrayList<PECashFlowInfo>();
				Map<String, Map<String, Object>> valMap = new TreeMap<String, Map<String, Object>>();
				Map<String, Object> valData = new HashMap<String, Object>();
				
				valInfo.setDay1Spread((BigDecimal) obj.get("day1Spread"));
				valInfo.setValVersion((String) obj.get("valVersion"));
				valInfo.setValDate(new Date(((java.sql.Timestamp)obj.get("valDate")).getTime()));
				valInfo.setBenchmarkYtm((BigDecimal) obj.get("benchmarkYtm"));
				valInfo.setYtm((BigDecimal) obj.get("ytm"));
				valInfo.setCalculatedDirty((BigDecimal) obj.get("calcDirty"));
				valInfo.setAccruedInterest((BigDecimal) obj.get("accInterest"));
				valInfo.setCleanValue((BigDecimal) obj.get("cleanValue"));
				valInfo.setDirtyPrice((BigDecimal) obj.get("dirtyPrice"));
				valInfo.setAccruedInterestRatio((BigDecimal) obj.get("accInterestRatio"));
				valInfo.setCleanPrice((BigDecimal) obj.get("cleanPrice"));
				
				secInfo.setSecurityId((String) obj.get("secId"));
				secInfo.setSecurityVersion(((Long) obj.get("secVersion")).intValue());
				secInfo.setIssueDate(new Date(((java.sql.Timestamp)obj.get("issueDate")).getTime()));
				secInfo.setSecurityName((String) obj.get("secName"));
				secInfo.setCurrency(Currency.valueOf((String) obj.get("currency")));
				secInfo.setStartingPrincipal((BigDecimal) obj.get("startingPrincipal"));
				secInfo.setFrequency((String) obj.get("frequency"));
				secInfo.setCoupon((BigDecimal) obj.get("coupon"));
				//java.sql.Date sqlDate = (java.sql.Date) obj.get("maturityDate");
				secInfo.setMaturityDate(new Date(((java.sql.Timestamp)obj.get("maturityDate")).getTime()));
				secInfo.setSector((String) obj.get("sector"));
				secInfo.setClassification((String) obj.get("classification"));
				secInfo.setType((String) obj.get("type"));
				secInfo.setTransactionPrice((BigDecimal) obj.get("transactionPrice"));
				secInfo.setTransactionDate(new Date(((java.sql.Timestamp)obj.get("transactionDate")).getTime()));
				secInfo.setDayCountConvention((String) obj.get("dayCountConvention"));
				secInfo.setBusinessDayConvention((String) obj.get("businessDayConvention"));
				secInfo.setNextPaymentDate(new Date(((java.sql.Timestamp)obj.get("nextPaymentDate")).getTime()));
				secInfo.setMargin((BigDecimal) obj.get("margin"));
				secInfo.setCouponBenchmark((String) obj.get("couponBenchmark"));
				
				cfInfo.setAccrualStartDate(new Date(((java.sql.Timestamp)obj.get("accrualStartDate")).getTime()));
				cfInfo.setPaymentDate(new Date(((java.sql.Timestamp)obj.get("paymentDate")).getTime()));
				cfInfo.setStartingPrincipal((BigDecimal) obj.get("startingPrincipal"));
				cfInfo.setPeriodicInterest( (BigDecimal) obj.get("periodicInterest"));
				cfInfo.setInterestDue( (BigDecimal) obj.get("interestDue"));
				cfInfo.setPrincipalPayment((BigDecimal) obj.get("principalPayment"));
				cfInfo.setAccruedInterest((BigDecimal) obj.get("accruedInterest"));
				cfInfo.setDaysUntilCf((Integer) obj.get("daysUntilCf"));
				cfInfo.setYears((BigDecimal) obj.get("years"));
				cfInfo.setDiscountFactor((BigDecimal) obj.get("discountFactor"));
				cfInfo.setPresentValueOfCf((BigDecimal) obj.get("presentValueOfCf"));
				
				cfInfoList.add(cfInfo);
				
				valData.put("valInfo", valInfo);
				valData.put("secInfo", secInfo);
				valData.put("cfInfoList", cfInfoList);
				
				valMap.put(valKey, valData);				
				
				calMap.put(calKey, valMap);
			}			
		}
		logger.info("Result : "+calMap);
		return calMap;
	}
	
	/* (non-Javadoc)
	 * @see com.markit.pe.valuationengine.dataaccess.IValuationDAL#getLatestCalAndValKey(long)
	 */
	@Override
	public Map<String, String> getLatestCalAndValKey(long parentChannelId){
		
		Map<String, String> keyMap = new HashMap<String, String>();
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelId", parentChannelId);
		
		String sql = "select "
				+ "cal.CAL_VERSION AS calVersion, "
				+ "cal.CAL_DATE AS calDate, "
				+ "val.VAL_VERSION AS valVersion, "
				+ "val.VALUATION_DATE AS valDate "
				+ "from "
				+ "T_FI_CALIBRATION_INFO cal, "
				+ "T_FI_VALUATION_INFO val "
				+ "where "
				+ "val.FI_CALIBRATION_ID = cal.FI_CALIBRATION_ID "
				+ "and "
				+ "cal.PARENT_CHANNEL_ID = :parentChannelId "
				+ "and "
				+ "cal.IS_ACTIVE=1 "
				+ "and val.VAL_VERSION = (select max(val1.VAL_VERSION) from T_FI_VALUATION_INFO val1 where val1.FI_CALIBRATION_ID = cal.FI_CALIBRATION_ID)";
		
		
		
		try{
			logger.info("Querying : "+sql);
			Map<String, Object> result = namedParameterJdbcTemplate.queryForMap(sql, paramSource);
			
			if(null != result && !result.isEmpty()){
				/*String calKey = "CalVersion-"+result.get("calVersion")+"_"+result.get("calDate");
				String valKey = "ValVersion-"+result.get("valVersion")+"_"+result.get("valDate");*/
				String calKey = (String) result.get("calVersion");
				//String valKey = result.get("valVersion")+"_"+result.get("valDate");
				String valKey = result.get("valVersion")+"_"+new SimpleDateFormat(PEConstants.PE_DEFAULT_DATE_FORMAT).format(new Date(((java.sql.Timestamp)result.get("valDate")).getTime()));
				
				keyMap.put("calKey", calKey);
				keyMap.put("valKey", valKey);
			}
			
		} catch (EmptyResultDataAccessException eRDAE) {
			logger.error(eRDAE.getMessage());
		}  catch (DataAccessException dAE) {
			logger.error(dAE.getMessage());
		}
		logger.info("Result : "+keyMap);
		return keyMap;
	}
	
	/**
	 * @param calibrationInfo
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void markInitialCalibrationFailed(long channelId){

		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("channelId", channelId)
				.addValue("isInitCalFailed", new Integer(1));;
		
		String sql = "UPDATE T_PORTFOLIO_SEC_INFO set IS_INIT_CAL_FAILED=:isInitCalFailed where CHANNEL_ID=:channelId";
		
		logger.info("Querying : "+sql);
		
		int result = namedParameterJdbcTemplate.update(sql, paramSource);
		
		logger.info("Updated Count : "+result);
	}
}
