package com.markit.pe.valuationengine.async;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.markit.pe.portfoliodata.constants.PEConstants;
import com.markit.pe.positiondata.domain.FloatingSecurityMargin;
import com.markit.pe.positiondata.domain.RedemptionSchedule;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.pe.valuationengine.api.ValuationEngineService;
import com.markit.pe.valuationengine.exception.ValuationEngineException;


public class PortfolioValuationTask implements Runnable {
	

	private static final Logger logger = LoggerFactory.getLogger(PortfolioValuationTask.class);
	
	private ValuationEngineService iValuationEngineService;
	
	private PESecurityInfoDTO peSecurityInfoDTO;
	
	private Date valuationDate;	
	
	private Long portfolioValId;
	
	

	@Override
	public void run() {
		logger.info("Evaluating the task  sec Id:::" + peSecurityInfoDTO.getSecurityId()+"With the thread name"+Thread.currentThread().getName());
		
		try{
			if(null != peSecurityInfoDTO){
				logger.info("fetching the redemption schedule");
				if(peSecurityInfoDTO.getPrincipalPaymentType().equals(PEConstants.PRINCIPAL_PAYMENT_TYPE_SINKING_FUND)){
					final List<RedemptionSchedule> redemptionSchedules=iValuationEngineService.getRedemptionScheduleByFiSecId(peSecurityInfoDTO.getFiSecId());
					if(!CollectionUtils.isEmpty(redemptionSchedules))
					peSecurityInfoDTO.setRedemptionSchedules(redemptionSchedules);
				}
				if(peSecurityInfoDTO.getType().equals(PEConstants.SECURITY_TYPE_FLOAT)){
					final List<FloatingSecurityMargin> floatingSecurityMargins=iValuationEngineService.getFloatingSecurityMarginByFiSecId(peSecurityInfoDTO.getFiSecId());
					if(!CollectionUtils.isEmpty(floatingSecurityMargins))
					peSecurityInfoDTO.setFloatingSecurityMargins(floatingSecurityMargins);
				}
				if(StringUtils.isBlank(peSecurityInfoDTO.getActiveCalVersion())){
					iValuationEngineService.performInitialCalibration(peSecurityInfoDTO,portfolioValId);
					peSecurityInfoDTO.setActiveCalVersion(String.valueOf(new Integer(1)));
					iValuationEngineService.performNextValuation(peSecurityInfoDTO, valuationDate,portfolioValId);
				} else {
					iValuationEngineService.performNextValuation(peSecurityInfoDTO, valuationDate,portfolioValId);
				}
			} else {
				throw new ValuationEngineException("Unexpected error");
			}
		} catch (Exception ex){
			logger.error(ex.getMessage());
			ex.printStackTrace();
			//TODO handle here persist in db 
		}
		//iValuationEngineService.performValuation(peSecurityInfoDTO, valuationDate);
	}


	public PortfolioValuationTask(final PESecurityInfoDTO peSecurityInfoDTO, final Date valDate,final Long portfolioValId, final ValuationEngineService iValuationEngineService) {
		super();
		this.peSecurityInfoDTO = peSecurityInfoDTO;
		this.iValuationEngineService=iValuationEngineService;
		this.valuationDate = valDate;
		this.portfolioValId=portfolioValId;
	}


	public PortfolioValuationTask() {
		super();
	}
	

}