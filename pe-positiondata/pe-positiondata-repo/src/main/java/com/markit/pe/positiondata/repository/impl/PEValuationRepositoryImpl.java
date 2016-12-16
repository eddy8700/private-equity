package com.markit.pe.positiondata.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.markit.pe.portfoliodata.PEPositionInfo;
import com.markit.pe.positiondata.domain.PEPortfolioSecurityInfo;
import com.markit.pe.positiondata.repository.PEValuationRepository;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;

@Component
public class PEValuationRepositoryImpl implements PEValuationRepository {
	private static final Logger logger = LoggerFactory.getLogger(PEValuationRepositoryImpl.class);
	
	private static final String CHANNEL_ID = "channelId";
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<Long, Map<String, Object>> findLastestCalibrationAndValuationInfo(final List<Long> channelIds) {
		
		final Map namedParameters = Collections.singletonMap("channelIdsValues", channelIds);
		final String SQL="select VAL.CHANNEL_ID channelId, "
				+ "CAL.CAL_DATE calDate, "
				+ "CAL.CAL_VERSION calVer, "
				+ "VAL.VALUATION_DATE valDate, "
				+ "VAL.DAY_1_SPREAD day1Spread, "
				+ "VAL.ACCRUED_INTEREST_RATIO accIntRatio, "
				+ "VAL.ACCRUED_INTEREST accInt, "
				+ "VAL.CLEAN_VALUE cleanValue, "
				+ "VAL.CLEAN_PRICE cleanPrice, "
				+ "VAL.CALCULATED_DIRTY calcDirty, "
				+ "VAL.DIRTY_PRICE dirtyPrice, "
				+ "VAL.YTM ytm, "
				+ "VAL.COMP_TYPE compType, "
				+ "VAL.BENCHMARK_YTM benchmarkYtm, "
				+ "VAL.VAL_VERSION valVersion "
				+ "from "
				+ "T_FI_VALUATION_INFO VAL "
				+ "join "
				+ "(select "
					+ "CHANNEL_ID,"
					+ "MAX(VAL_VERSION) val_v "
					+ "from "
					+ "T_FI_VALUATION_INFO"+
					" group by CHANNEL_ID) VAL_1 "
				+ "ON VAL.CHANNEL_ID=VAL_1.CHANNEL_ID and VAL.VAL_VERSION=VAL_1.val_v "
				+ "join T_FI_CALIBRATION_INFO CAL on CAL.FI_CALIBRATION_ID = VAL.FI_CALIBRATION_ID where VAL.CHANNEL_ID in (:channelIdsValues)";
		List<Map<String, Object>> info =   namedParameterJdbcTemplate.queryForList(SQL, namedParameters);
		Map<Long, Map<String, Object>> map = new HashMap<Long, Map<String, Object>>();
		
		for(Map<String, Object> obj : info){
			map.put((Long)obj.get(CHANNEL_ID), obj);
		}
		return map;
		
	}
	
	public void buildCalibrationAndValuationMapper(PEPortfolioSecurityInfo portfolioSecurityInfo,
			Map<Long, Map<String, Object>> map, PEPositionInfo positionInfo) {
		if(map.size()!= 0 && map.containsKey(portfolioSecurityInfo.getId())){
		positionInfo.setCalDate((Date) map.get(portfolioSecurityInfo.getId()).get("calDate"));
		positionInfo.setCalVersion((String)map.get(portfolioSecurityInfo.getId()).get("calVer"));
		
		//Valuation details		
		positionInfo.setValDate((Date) map.get(portfolioSecurityInfo.getId()).get("valDate"));
		positionInfo.setDay1Spread((BigDecimal)map.get(portfolioSecurityInfo.getId()).get("day1Spread"));
		positionInfo.setAccruedInterestRatio((BigDecimal)map.get(portfolioSecurityInfo.getId()).get("accIntRatio"));
		positionInfo.setCleanValue((BigDecimal)map.get(portfolioSecurityInfo.getId()).get("cleanValue"));
		positionInfo.setCleanPrice((BigDecimal)map.get(portfolioSecurityInfo.getId()).get("cleanPrice"));
		positionInfo.setCalculatedDirty((BigDecimal)map.get(portfolioSecurityInfo.getId()).get("calcDirty"));
		positionInfo.setDirtyPrice((BigDecimal)map.get(portfolioSecurityInfo.getId()).get("dirtyPrice"));
		positionInfo.setAccruedInterest((BigDecimal)map.get(portfolioSecurityInfo.getId()).get("accInt"));
		positionInfo.setYtm((BigDecimal)map.get(portfolioSecurityInfo.getId()).get("ytm"));
		positionInfo.setCompType((String)map.get(portfolioSecurityInfo.getId()).get("compType"));
		positionInfo.setBenchmarkYtm((BigDecimal)map.get(portfolioSecurityInfo.getId()).get("benchmarkYtm"));
		positionInfo.setValVersion((String)map.get(portfolioSecurityInfo.getId()).get("valVersion"));
		}
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public List<PEValuationInfo> findByChannelId(Long channelId){
	         final String queryString="Select peval from PEValuationInfo peval where peval.channelId=:channelId";
		        return (List<PEValuationInfo>) entityManager.createQuery(queryString, PEValuationInfo.class).setParameter("channelId", channelId);
	}
	*/
	/* (non-Javadoc)
	 * @see com.markit.pe.positiondata.repository.PEValuationRepository#getActiveCalVersion(long)
	 */
	@Override
	public Map<Long, String> getActiveCalVersions(List<Long> parentChannelIds){
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelIds", parentChannelIds)
				.addValue("isActive", new Integer(1));
		
		String sql = "select PARENT_CHANNEL_ID parentChannelId, CAL_VERSION calVersion from T_FI_CALIBRATION_INFO where "
												+ "PARENT_CHANNEL_ID in (:parentChannelIds) "
												+ "and "
												+ "IS_ACTIVE=:isActive";
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try{
			logger.info("Querying : "+sql);
			result = namedParameterJdbcTemplate.queryForList(sql, paramSource);
		} catch (EmptyResultDataAccessException eRDAE) {
			// TODO handle it
		}  catch (DataAccessException dAE) {
			// TODO handle it
		}
		
		Map<Long, String> map = new HashMap<Long, String>();
		
		for(Map<String, Object> obj : result){
			map.put((Long)obj.get("parentChannelId"), (String)obj.get("calVersion"));
		}
		
		return map;		
	}
	
	/* (non-Javadoc)
	 * @see com.markit.pe.positiondata.repository.PEValuationRepository#getActiveCalVersion(long)
	 */
	@Override
	public String getActiveCalVersion(long parentChannelId){
		String activeCalVersion = null;
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("parentChannelId", parentChannelId)
				.addValue("isActive", new Integer(1));
		
		String sql = "select CAL_VERSION calVersion from T_FI_CALIBRATION_INFO where "
												+ "PARENT_CHANNEL_ID=:parentChannelId "
												+ "and "
												+ "IS_ACTIVE=:isActive";
		
		try{
			logger.info("Querying : "+sql);
			activeCalVersion = namedParameterJdbcTemplate.queryForObject(sql, paramSource, String.class);
		} catch (EmptyResultDataAccessException eRDAE) {
			activeCalVersion = null;
			// TODO handle it
		}  catch (DataAccessException dAE) {
			activeCalVersion = null;
			// TODO handle it
		}
		
		return activeCalVersion;
	}
	
	@Override
	public List<PEPositionInfo> getPortfolioPositions(String portfolioName){
		List<PEPositionInfo> positions = new ArrayList<PEPositionInfo>();
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("portfolioName", portfolioName);
		
		String sql = "select "
				+ "sec.FI_SEC_ID AS fiSecId, "
				+ "psec.PORTFOLIO_ID AS portfolioId, "
				+ "psec.PARENT_CHANNEL_ID AS parentChannelId, "
				+ "val.CHANNEL_ID AS channelId, "
				+ "sec.SEC_NAME AS securityName, "
				+ "sec.SEC_ID AS securityId, "
				+ "sec.SEC_VERSION AS securityVersion, "
				+ "sec.ISSUE_DATE AS issueDate, "
				+ "sec.CURRENCY AS currency, "
				+ "sec.NEXT_PAYMENT_DATE AS nextPaymentDate, "
				+ "sec.PAR_VALUE AS startingPrincipal, "
				+ "sec.COUPON_FREQUENCY AS frequency, "
				+ "sec.COUPON_RATE AS coupon, "
				+ "sec.MATURITY_DATE AS maturityDate, "
				+ "sec.CLASSIFICATION AS classification, "
				+ "sec.[TYPE] AS type, "
				+ "sec.DAY_COUNT_CONV AS dayCountConvention, "
				+ "sec.BUSINESS_DAY_CONV AS businessDayConvention, "
				+ "sec.SECTOR AS sector, "
				+ "sec.UNITPRICE AS unitPrice, "
				+ "sec.SEC_DESCRIPTION AS securityDescription, "
				+ "sec.FIID AS fiid, "
				+ "sec.RATING AS rating, "
				+ "sec.DEBT_TYPE AS debtType, "
				+ "sec.SPREAD AS spread, "
				+ "sec.CALL_PRICE AS callPrice, "
				+ "sec.YIELD_TO_CALL AS yieldToCall, "
				+ "sec.CALL_DATE AS callDate, "
				+ "sec.CALL_TYPE AS callType, "
				+ "sec.TRANSACTION_DATE AS transactionDate, "
				+ "sec.TRANSACTION_PRICE AS transactionPrice, "
				+ "sec.CAP AS cap, "
				+ "sec.[FLOOR] AS floor, "
				+ "sec.PRINCIPAL_PAYMENT_TYPE AS principalPaymentType, "
				+ "sec.MARGIN AS margin, "
				+ "sec.COUPON_RESET_FREQUENCY AS couponResetFrequency, "
				+ "sec.COUPON_BENCHMARK AS couponBenchmark, "
				+ "cal.CAL_DATE AS calDate, "
				+ "cal.CAL_VERSION AS calVersion, "
				+ "val.VALUATION_DATE AS valDate, "
				+ "val.VAL_VERSION AS valVersion, "
				+ "val.DAY_1_SPREAD AS day1Spread, "
				+ "val.ACCRUED_INTEREST_RATIO AS accruedInterestRatio, "
				+ "val.ACCRUED_INTEREST AS accruedInterest, "
				+ "val.CLEAN_VALUE AS cleanValue, "
				+ "val.CLEAN_PRICE AS cleanPrice, "
				+ "val.CALCULATED_DIRTY AS calculatedDirty, "
				+ "val.DIRTY_PRICE AS dirtyPrice, "
				+ "val.YTM AS ytm, "
				+ "val.COMP_TYPE AS compType, "
				+ "val.BENCHMARK_YTM AS benchmarkYtm "
				+ "from "
				+ "T_FI_CALIBRATION_INFO cal, "
				+ "T_FI_VALUATION_INFO val, "
				+ "T_FI_SEC_DETAILS sec, "
				+ "T_PORTFOLIO_SEC_INFO psec "
				+ "where "
				+ "val.FI_CALIBRATION_ID = cal.FI_CALIBRATION_ID "
				+ "and cal.PARENT_CHANNEL_ID in "
					+ "(select distinct psec1.PARENT_CHANNEL_ID from T_PORTFOLIO_SEC_INFO psec1 where psec1.PORTFOLIO_ID in "
						+ "(select pInfo.PORTFOLIO_ID from T_PORTFOLIO_INFO pInfo where pInfo.PORTFOLIO_NAME=:portfolioName)) "
				+ "and cal.IS_ACTIVE=1 "
				+ "and val.VAL_VERSION = (select max(val1.VAL_VERSION) from T_FI_VALUATION_INFO val1 where val1.FI_CALIBRATION_ID = cal.FI_CALIBRATION_ID) "
				+ "and psec.CHANNEL_ID = val.CHANNEL_ID "
				+ "and psec.FI_SEC_ID = sec.FI_SEC_ID";
		
		logger.info("Querying : "+sql);
		
		positions = namedParameterJdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper<PEPositionInfo>(PEPositionInfo.class));
		
		logger.info("Result Size - Valued positions: "+positions.size());
		return positions;
	}
	
	/* (non-Javadoc)
	 * @see com.markit.pe.positiondata.repository.PEValuationRepository#getSecurityDetails(java.lang.String)
	 */
	@Override
	public List<PESecurityInfoDTO> getSecurityDetails(String portfolioName){
		List<PESecurityInfoDTO> peSecurityInfoDTOs = new ArrayList<PESecurityInfoDTO>();
		
		SqlParameterSource paramSource = new MapSqlParameterSource()
				.addValue("portfolioName", portfolioName);
		
		String sql = "select "
				+ "sec.FI_SEC_ID AS fiSecId, "
				+ "psec1.PORTFOLIO_ID AS portfolioId, "
				+ "psec1.PARENT_CHANNEL_ID AS parentChannelId, "
				+ "psec1.CHANNEL_ID AS channelId, "
				+ "sec.SEC_NAME AS securityName, "
				+ "sec.SEC_ID AS securityId, "
				+ "sec.SEC_VERSION AS securityVersion, "
				+ "sec.ISSUE_DATE AS issueDate, "
				+ "sec.CURRENCY AS currency, "
				+ "sec.NEXT_PAYMENT_DATE AS nextPaymentDate, "
				+ "sec.PAR_VALUE AS startingPrincipal, "
				+ "sec.COUPON_FREQUENCY AS frequency, "
				+ "sec.COUPON_RATE AS coupon, "
				+ "sec.MATURITY_DATE AS maturityDate, "
				+ "sec.CLASSIFICATION AS classification, "
				+ "sec.[TYPE] AS type, "
				+ "sec.DAY_COUNT_CONV AS dayCountConvention, "
				+ "sec.BUSINESS_DAY_CONV AS businessDayConvention, "
				+ "sec.SECTOR AS sector, "
				+ "sec.UNITPRICE AS unitPrice, "
				+ "sec.SEC_DESCRIPTION AS securityDescription, "
				+ "sec.FIID AS fiid, "
				+ "sec.RATING AS rating, "
				+ "sec.DEBT_TYPE AS debtType, "
				+ "sec.SPREAD AS spread, "
				+ "sec.CALL_PRICE AS callPrice, "
				+ "sec.YIELD_TO_CALL AS yieldToCall, "
				+ "sec.CALL_DATE AS callDate, "
				+ "sec.CALL_TYPE AS callType, "
				+ "sec.TRANSACTION_DATE AS transactionDate, "
				+ "sec.TRANSACTION_PRICE AS transactionPrice, "
				+ "sec.CAP AS cap, "
				+ "sec.[FLOOR] AS floor, "
				+ "sec.PRINCIPAL_PAYMENT_TYPE AS principalPaymentType, "
				+ "sec.MARGIN AS margin, "
				+ "sec.COUPON_RESET_FREQUENCY AS couponResetFrequency, "
				+ "sec.COUPON_BENCHMARK AS couponBenchmark, "
				+ "cal.CAL_VERSION AS activeCalVersion "
				+ "from "
				+ "T_FI_SEC_DETAILS sec, "
				+ "T_PORTFOLIO_SEC_INFO psec1 "
				+ "LEFT JOIN "
				+ "T_FI_CALIBRATION_INFO cal "
				+ "on "
				+ "psec1.PARENT_CHANNEL_ID = cal.PARENT_CHANNEL_ID "
				+ "and cal.IS_ACTIVE =1 "
				+ "where psec1.CHANNEL_ID in (SELECT MAX(psec.CHANNEL_ID) AS channelId from T_PORTFOLIO_SEC_INFO psec where psec.PORTFOLIO_ID in (select pInfo.PORTFOLIO_ID from T_PORTFOLIO_INFO pInfo where pInfo.PORTFOLIO_NAME=:portfolioName) group by psec.PARENT_CHANNEL_ID ) "
				+ "and sec.FI_SEC_ID = psec1.FI_SEC_ID";
		
		logger.info("Querying : "+sql);
		
		peSecurityInfoDTOs = namedParameterJdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper<PESecurityInfoDTO>(PESecurityInfoDTO.class));
		
		logger.info("Result Size - all: "+peSecurityInfoDTOs.size());
		return peSecurityInfoDTOs;
	}
	
	  @Override
		public List<PEPositionInfo> findLatestSecurityWithoutValuationAndCalibration(final String portfolioName) {
	    	  List<PEPositionInfo> securityPositions = new ArrayList<PEPositionInfo>();
			SqlParameterSource paramSource = new MapSqlParameterSource().addValue("portfolioName", portfolioName);
			final String SQL="Select "
					+ "sec.FI_SEC_ID AS fiSecId, "
					+ "psec.PORTFOLIO_ID AS portfolioId, "
					+ "psec.PARENT_CHANNEL_ID AS parentChannelId, "
					+ "sec.SEC_NAME AS securityName, "
					+ "sec.SEC_ID AS securityId, "
					+ "sec.SEC_VERSION AS securityVersion, "
					+ "sec.ISSUE_DATE AS issueDate, "
					+ "psec.CHANNEL_ID AS channelId, "
					+ "sec.CURRENCY AS currency, "
					+ "sec.NEXT_PAYMENT_DATE AS nextPaymentDate, "
					+ "sec.PAR_VALUE AS startingPrincipal, "
					+ "sec.COUPON_FREQUENCY AS frequency, "
					+ "sec.COUPON_RATE AS coupon, "
					+ "sec.MATURITY_DATE AS maturityDate, "
					+ "sec.CLASSIFICATION AS classification, "
					+ "sec.[TYPE] AS type, "
					+ "sec.DAY_COUNT_CONV AS dayCountConvention, "
					+ "sec.BUSINESS_DAY_CONV AS businessDayConvention, "
					+ "sec.SECTOR AS sector, "
					+ "sec.UNITPRICE AS unitPrice, "
					+ "sec.SEC_DESCRIPTION AS securityDescription, "
					+ "sec.FIID AS fiid, "
					+ "sec.RATING AS rating, "
					+ "sec.DEBT_TYPE AS debtType, "
					+ "sec.SPREAD AS spread, "
					+ "sec.CALL_PRICE AS callPrice, "
					+ "sec.YIELD_TO_CALL AS yieldToCall, "
					+ "sec.CALL_DATE AS callDate, "
					+ "sec.CALL_TYPE AS callType, "
					+ "sec.TRANSACTION_DATE AS transactionDate, "
					+ "sec.TRANSACTION_PRICE AS transactionPrice, "
					+ "sec.CAP AS cap, "
					+ "sec.[FLOOR] AS floor, "
					+ "sec.PRINCIPAL_PAYMENT_TYPE AS principalPaymentType, "
					+ "sec.MARGIN AS margin, "
					+ "sec.COUPON_RESET_FREQUENCY AS couponResetFrequency, "
					+ "sec.COUPON_BENCHMARK AS couponBenchmark "
					+ "from "
					+ "T_FI_SEC_DETAILS sec, "
					+ "T_PORTFOLIO_SEC_INFO psec "
					+ "where "
					+ " psec.CHANNEL_ID in "
					          + "(SELECT MAX(psec.CHANNEL_ID) AS channelId from T_PORTFOLIO_SEC_INFO psec where psec.PORTFOLIO_ID in "
					          + "(select pInfo.PORTFOLIO_ID from T_PORTFOLIO_INFO pInfo where pInfo.PORTFOLIO_NAME=:portfolioName) "
					          + " and psec.PARENT_CHANNEL_ID not in "
					          + " (select distinct cal.PARENT_CHANNEL_ID from T_FI_CALIBRATION_INFO cal) "
					          +   " group by psec.PARENT_CHANNEL_ID )"
					 + "and sec.FI_SEC_ID = psec.FI_SEC_ID";         
					
			logger.info("Querying : "+SQL);
		    securityPositions = namedParameterJdbcTemplate.query(SQL, paramSource, new BeanPropertyRowMapper<PEPositionInfo>(PEPositionInfo.class));
			logger.info("Result Size - Never Valued: "+securityPositions.size());
			return securityPositions;

		}

}
