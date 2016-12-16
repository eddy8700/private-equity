/**
 * 
 */
package com.markit.pe.comparabledata.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Period;

import com.markit.pe.comparabledata.domain.QEVBData;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * @author mahesh.agarwal
 *
 */
public final class EvbCompSystemGenPredicates {
	private EvbCompSystemGenPredicates(){
		
	}
	
	static BooleanExpression build(PESecurityInfoDTO peSecurityInfoDTO){
		List<BooleanExpression> predicates = new ArrayList<BooleanExpression>();
		
		BooleanExpression expMaturityDate = evbCompMaturityDate(peSecurityInfoDTO.getMaturityDate()
				, peSecurityInfoDTO.getIssueDate());
		
        if (expMaturityDate != null) {
            predicates.add(expMaturityDate);
        }
        
        BooleanExpression expYtm = evbCompYtm(peSecurityInfoDTO.getYtmTransient());
		
        if (expYtm != null) {        	
            predicates.add(expYtm);
        }		
		
		BooleanExpression expCcy = evbCompCcy(peSecurityInfoDTO.getCurrency().toString());
		
        if (expCcy != null) {
            predicates.add(expCcy);
        }
        
		BooleanExpression expLiqScore = evbCompLiqScore();
		
        if (expLiqScore != null) {
            predicates.add(expLiqScore);
        }
        
        BooleanExpression expClassification = evbCompClassification(peSecurityInfoDTO.getClassification());
		
        if (expClassification != null) {
            predicates.add(expClassification);
        }
        
        BooleanExpression expSecLevel5 = evbCompSectorLevel5(peSecurityInfoDTO.getSector());
		
        if (expSecLevel5 != null) {
            predicates.add(expSecLevel5);
        }
        
        BooleanExpression expDate = evbCompDate(peSecurityInfoDTO.getTransactionDate());
		
        if (expDate != null) {
            predicates.add(expDate);
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
	
	static BooleanExpression evbCompMaturityDate(Date maturityDate, Date issueDate){
		Period period = new Period(issueDate.getTime(), maturityDate.getTime());
		int yearsDiff = period.getYears();
		if(yearsDiff <= 6){
			Date minMaturity;
			Date maxMaturity;
			Calendar cal=Calendar.getInstance();
			cal.setTime(maturityDate);
			cal.add(Calendar.YEAR, -1);
			cal.add(Calendar.MONTH, -6);
			minMaturity = cal.getTime();
			
			Calendar cal2=Calendar.getInstance();
			cal2.setTime(maturityDate);
			cal2.add(Calendar.YEAR, 1);
			cal2.add(Calendar.MONTH, 6);
			maxMaturity = cal2.getTime();
			
			return QEVBData.eVBData.maturityDate.between(minMaturity, maxMaturity);
			
		} else if(yearsDiff > 6 && yearsDiff <= 12){
			Date minMaturity;
			Date maxMaturity;
			Calendar cal=Calendar.getInstance();
			cal.setTime(maturityDate);
			cal.add(Calendar.YEAR, -2);
			minMaturity = cal.getTime();
			
			Calendar cal2=Calendar.getInstance();
			cal2.setTime(maturityDate);
			cal2.add(Calendar.YEAR, 2);
			maxMaturity = cal2.getTime();
			
			return QEVBData.eVBData.maturityDate.between(minMaturity, maxMaturity);
		} else if(yearsDiff > 12){
			Date minMaturity;
			Date maxMaturity;
			Calendar cal=Calendar.getInstance();
			cal.setTime(maturityDate);
			cal.add(Calendar.YEAR, -8);
			minMaturity = cal.getTime();
			
			Calendar cal2=Calendar.getInstance();
			cal2.setTime(maturityDate);
			cal2.add(Calendar.YEAR, 8);
			maxMaturity = cal2.getTime();
			
			return QEVBData.eVBData.maturityDate.between(minMaturity, maxMaturity);
		}
		
		return null;
	}
	
	static BooleanExpression evbCompYtm(BigDecimal ytm){
		if(null != ytm){

			if(ytm.compareTo(new BigDecimal(6.5)) < 0 || ytm.compareTo(new BigDecimal(6.5)) == 0){

				BigDecimal minCompMidYtm = ytm.subtract(new BigDecimal(1.0));
				BigDecimal maxCompMidYtm = ytm.add(new BigDecimal(1.0));

				return QEVBData.eVBData.midYTM.between(minCompMidYtm, maxCompMidYtm);

			} else if (ytm.compareTo(new BigDecimal(6.5)) > 0) {
				QEVBData.eVBData.midYTM.eq(new BigDecimal(5.0));
			}

		} return null;
	}
	
	static BooleanExpression evbCompCcy(String ccy){
		if(StringUtils.isNotBlank(ccy)){
			return QEVBData.eVBData.currency.eq(ccy);
			
		} return null;
		
	}
	
	static BooleanExpression evbCompLiqScore(){
		return QEVBData.eVBData.liquidityScore.between(1, 3);
	}
	
	static BooleanExpression evbCompClassification(String classification){
		if(StringUtils.isNotBlank(classification)){
			return QEVBData.eVBData.classification.eq(classification);
			
		} return null;		
	}
	
	static BooleanExpression evbCompSectorLevel5(String sectorLevel5){
		if(StringUtils.isNotBlank(sectorLevel5)){
			return QEVBData.eVBData.sectorLevel5.eq(sectorLevel5);
		} return null;
	}
	
	static BooleanExpression evbCompDate(Date date){
		if(null != date){
			return QEVBData.eVBData.date.eq(date);
		} return null;
	}
	
}
