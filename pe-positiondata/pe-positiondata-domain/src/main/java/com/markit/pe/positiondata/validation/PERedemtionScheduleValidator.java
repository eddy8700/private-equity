package com.markit.pe.positiondata.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.markit.pe.portfoliodata.validator.SingleValidationError;
import com.markit.pe.portfoliodata.validator.ValidationError;
import com.markit.pe.portfoliodata.validator.Validator;
import com.markit.pe.positiondata.domain.RedemptionSchedule;

@Component
public class PERedemtionScheduleValidator implements Validator<List<RedemptionSchedule>, ValidationError<List<RedemptionSchedule>>> {

	@Override
	public List<SingleValidationError<List<RedemptionSchedule>>> validate(List<RedemptionSchedule> redemptionSchedule) {
		final List<SingleValidationError<List<RedemptionSchedule>>> validationErrorList = new ArrayList<>();
		if(redemptionSchedule != null){
			 if(redemptionSchedule.size()>1){
				 for (int i=0;i<redemptionSchedule.size();i++){
					 
					 
					 /*
					 final RedemptionSchedule redemptionSchedule2=redemptionSchedule.get(i);
					 final Date startDate=redemptionSchedule2.getRepaymentStartPeriod();
					 final Date  endDate=redemptionSchedule2.getRepaymentEndPeriod();
					 if(endDate != null && startDate != null){
						 
					 }
					 
					 
				 */}
			 }
			
			
		}
		
		
		
		return null;
	}

	@Override
	public String identity() {
		return "PERedemtionScheduleValidator";
	}


	@Override
	public List<String> buildErrorMessage(List<SingleValidationError<List<RedemptionSchedule>>> validationErrorSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RedemptionSchedule> buildForDefaultValues(List<RedemptionSchedule> t) {
		return null;
	}

}
