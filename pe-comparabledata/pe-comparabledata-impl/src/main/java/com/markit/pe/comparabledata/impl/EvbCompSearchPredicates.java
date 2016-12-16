/**
 * 
 */
package com.markit.pe.comparabledata.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.markit.pe.comparabledata.domain.EVBCompSearchCriteria;
import com.markit.pe.comparabledata.domain.QEVBData;
import com.markit.pe.portfoliodata.util.DateUtility;
import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * @author mahesh.agarwal
 *
 */
public final class EvbCompSearchPredicates {
	private EvbCompSearchPredicates(){
		
	}
	
	static BooleanExpression build(EVBCompSearchCriteria evbCompSearchCriteria){
		List<BooleanExpression> predicates = new ArrayList<BooleanExpression>();
		
		BooleanExpression expDate = evbCompDate(evbCompSearchCriteria.getDate());
		
        if (expDate != null) {
            predicates.add(expDate);
        }
		
		BooleanExpression expCcy = evbCompCcy(evbCompSearchCriteria.getCurrency());
		
        if (expCcy != null) {
            predicates.add(expCcy);
        }
        
        BooleanExpression expClassification = evbCompClassification(evbCompSearchCriteria.getClassification());
		
        if (expClassification != null) {
            predicates.add(expClassification);
        }
        
        BooleanExpression expCoupon = evbCompCoupon(evbCompSearchCriteria.getCoupon());
		
        if (expCoupon != null) {
            predicates.add(expCoupon);
        }
        
        BooleanExpression expFreq = evbCompFrequency(evbCompSearchCriteria.getFrequency());
		
        if (expFreq != null) {
            predicates.add(expFreq);
        }
		
        BooleanExpression expIsin = evbCompIsin(evbCompSearchCriteria.getIsin());
		
        if (expIsin != null) {
            predicates.add(expIsin);
        }
        
        BooleanExpression expMaturityDate = evbCompMaturityDate(evbCompSearchCriteria.getMaturityDate(),evbCompSearchCriteria.getMaturityMonthRange());
		
        if (expMaturityDate != null) {
            predicates.add(expMaturityDate);
        }
        
        BooleanExpression expRegion = evbCompRegion(evbCompSearchCriteria.getRegion());
		
        if (expRegion != null) {
            predicates.add(expRegion);
        }
        
        BooleanExpression expSecLevel5 = evbCompSectorLevel5(evbCompSearchCriteria.getSectorLevel5());
		
        if (expSecLevel5 != null) {
            predicates.add(expSecLevel5);
        }
        
        BooleanExpression expShorName = evbCompShortName(evbCompSearchCriteria.getShortName());
		
        if (expShorName != null) {
            predicates.add(expShorName);
        }
        
        BooleanExpression expType = evbCompType(evbCompSearchCriteria.getType());
		
        if (expType != null) {
            predicates.add(expType);
        }
        
        BooleanExpression midYTM = evbCompYtm(evbCompSearchCriteria.getMinYTM(),evbCompSearchCriteria.getMaxYTM());
        
        if (midYTM != null) {
            predicates.add(midYTM);
        }
        
     BooleanExpression modDuration = evbCompDuration(evbCompSearchCriteria.getMinModDuration(),evbCompSearchCriteria.getMaxModDuration());
        
        if (modDuration != null) {
            predicates.add(modDuration);
        }
        
        if(!predicates.isEmpty()){
        	BooleanExpression result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = result.and(predicates.get(i));
            }
            return result;
        }
        return null;		
	}
	
	private static BooleanExpression evbCompDuration(BigDecimal minModDuration, BigDecimal maxModDuration) {
		if(null != minModDuration && null != maxModDuration){
			return QEVBData.eVBData.midModDuration.between(minModDuration, maxModDuration);
		}
			else if (minModDuration== null && maxModDuration != null) {
				return QEVBData.eVBData.midModDuration.loe(maxModDuration);
			}
			else if (minModDuration != null && maxModDuration == null) {
				return QEVBData.eVBData.midModDuration.goe(minModDuration);
			}
			else{
				return null;
			}
		
	}
	

	private static BooleanExpression evbCompMaturityDate(Date maturityDate, Integer months) {
		if(null != maturityDate && months != null){
			Date beforeMaturity=DateUtility.subtractMonths(maturityDate, months);
			Date afterMaturity=DateUtility.plusMonths(maturityDate, months);
			return QEVBData.eVBData.maturityDate.between(beforeMaturity, afterMaturity);
		} 
		if(maturityDate != null && months == null){
			
			return QEVBData.eVBData.maturityDate.eq(maturityDate);
		}
		return null;
	
	}
	
	static BooleanExpression evbCompYtm(BigDecimal minYtm,BigDecimal maxYtm){
		if(null != minYtm && null != maxYtm){
			return QEVBData.eVBData.midYTM.between(minYtm, maxYtm);
		}
			 else if (minYtm == null && maxYtm != null) {
				return QEVBData.eVBData.midYTM.loe(maxYtm);
			}
			 else if (minYtm != null && maxYtm == null) {
				return QEVBData.eVBData.midYTM.goe(minYtm);
			}
			else{
				return null;
			}
		
	}

	static BooleanExpression evbCompDate(Date date){
		if(null != date){
			return QEVBData.eVBData.date.eq(date);
		} return null;
	}
	
	static BooleanExpression evbCompCcy(String ccy){
		if(StringUtils.isNotBlank(ccy)){
			return QEVBData.eVBData.currency.eq(ccy);
			
		} return null;
		
	}
	static BooleanExpression evbCompClassification(String classification){
		if(StringUtils.isNotBlank(classification)){
			return QEVBData.eVBData.classification.eq(classification);
			
		} return null;		
	}
	
	static BooleanExpression evbCompCoupon(BigDecimal coupon){
		if(null != coupon){
			return QEVBData.eVBData.coupon.eq(coupon);
		} return null;
	}
	
	static BooleanExpression evbCompFrequency(String frequency){
		if(StringUtils.isNotBlank(frequency)){
			return QEVBData.eVBData.frequency.eq(frequency);
			
		} return null;
	}
	
	static BooleanExpression evbCompIsin(String isin){
		if(StringUtils.isNotBlank((String) isin)){
			String isinsStr = (String) isin;
			String [] items = isinsStr.split("\\s*,\\s*");
			List<String> list = Arrays.asList(items);
			return QEVBData.eVBData.isin.in(list);
		} return null;
	}
	
	static BooleanExpression evbCompMaturityDate(Date maturityDate){
		if(null != maturityDate){
			return QEVBData.eVBData.maturityDate.eq(maturityDate);
		} return null;
	}
	
	static BooleanExpression evbCompRegion(String region){
		if(StringUtils.isNotBlank(region)){
			return QEVBData.eVBData.region.eq(region);
			
		} return null;
	}
	
	static BooleanExpression evbCompSectorLevel5(String sectorLevel5){
		if(StringUtils.isNotBlank(sectorLevel5)){
			return QEVBData.eVBData.sectorLevel5.eq(sectorLevel5);
		} return null;
	}
	
	static BooleanExpression evbCompShortName(String shortName){
		if(StringUtils.isNotBlank(shortName)){
			return QEVBData.eVBData.shortName.eq(shortName);
		} return null;
	}
	
	static BooleanExpression evbCompType(String type){
		if(StringUtils.isNotBlank(type)){
			return QEVBData.eVBData.type.eq(type);
		} return null;
	}
	
}
