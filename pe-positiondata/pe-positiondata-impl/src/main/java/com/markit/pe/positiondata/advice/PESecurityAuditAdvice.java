package com.markit.pe.positiondata.advice;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.markit.pe.portfoliodata.util.DuplicateDomainHandler;
import com.markit.pe.positiondata.api.FloatingRateMarginService;
import com.markit.pe.positiondata.api.RedemptionScheduleService;
import com.markit.pe.positiondata.domain.FloatingSecurity;
import com.markit.pe.positiondata.domain.FloatingSecurityMargin;
import com.markit.pe.positiondata.domain.PESecurityDetails;
import com.markit.pe.positiondata.domain.RedemptionSchedule;
import com.markit.pe.positiondata.repository.PESecurityDetailsRepository;

@Aspect
@Component
public class PESecurityAuditAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(PESecurityAuditAdvice.class);

	@Autowired
	private PESecurityDetailsRepository peSecDetailRepo;

	@Resource(name = "securityChangeDetector")
	private DuplicateDomainHandler<PESecurityDetails> securityChangeDetector;
	
	@Autowired
	private RedemptionScheduleService redemptionScheduleService;
	
	@Autowired
	private FloatingRateMarginService floatingRateMarginService;
	

	@Around("execution(* com.markit.pe.positiondata.repository.PESecurityDetailsRepository.save(..))")
	public Object handleSecurityAuditAdvice(ProceedingJoinPoint pjp) throws Throwable {
		LOGGER.debug("Creating auditing trail for  Security Details");
		Integer securityVersion = null;
		String securityId = null;
		Long fundId = null;
		String redemptionId= null;
		String marginId=null;
		for (final Object argument : pjp.getArgs()) {
			if (argument instanceof PESecurityDetails) {
				securityId = ((PESecurityDetails) argument).getSecurityId();
				fundId = ((PESecurityDetails) argument).getFund().getId();
				securityVersion = ((PESecurityDetails) argument).getSecurityVersion() - 1;
			}
		}
		LOGGER.debug("Fetching the previous PESecurityDetails with fundId{} and security Id {} and securityVersion {}",
				fundId, securityId, securityVersion);
		final PESecurityDetails beforePESecurityDetails = peSecDetailRepo
				.findByFundIdAndSecurityIdAndSecurityVersion(fundId, securityId, securityVersion);
		if (beforePESecurityDetails == null) {
			LOGGER.debug("There is no pe security details found in the db This may be fresh insert into security details");
			return pjp.proceed();
		} else {
			LOGGER.info("Fetching the redemption schedule for fisecId {}",beforePESecurityDetails.getFiSecId());
			final List<RedemptionSchedule> beforeRedemptionList=getRedemptionScheduleByFiSecId(beforePESecurityDetails.getFiSecId());
			if(CollectionUtils.isNotEmpty(beforeRedemptionList)){
				  redemptionId=beforeRedemptionList.get(0).getRedemptionId();
				  beforePESecurityDetails.setRedemptionSchedules(beforeRedemptionList);
			}
			final Object secDetailsLatest = pjp.proceed();
			 
			LOGGER.debug("PESecurityDetails saved with the updated info with the id ");
			for (final Object argument : pjp.getArgs()) {
				if (secDetailsLatest instanceof PESecurityDetails) {
					final PESecurityDetails afterpeSecurityDetails = (PESecurityDetails) argument;					
					final boolean isRedemptionSame=checkForDuplicateRedemptionSchedule(beforeRedemptionList,afterpeSecurityDetails.getRedemptionSchedules());
					  checkForResetMargin(beforePESecurityDetails,afterpeSecurityDetails);
					if(isRedemptionSame){
						LOGGER.info("There is no change in the redemption schedule for the edit case");
						LOGGER.info("Updating the mapping entry for security redemption");
						if(StringUtils.isNotBlank(redemptionId)){
							redemptionScheduleService.persistSecurityRedemptionMapping(afterpeSecurityDetails.getFiSecId(), redemptionId);
						}
					}
					else{
						LOGGER.info("Building the new  redemption info object");
						List<RedemptionSchedule> redemtionScheduleList = afterpeSecurityDetails.getRedemptionSchedules();
						if(CollectionUtils.isNotEmpty(redemtionScheduleList)){
							redemptionScheduleService.persistRedemptionInfo(afterpeSecurityDetails);
						 	LOGGER.info("Building mapping entry");
						 	Long fiSecId = afterpeSecurityDetails.getFiSecId();
							String rId = afterpeSecurityDetails.getSecurityId() + "_"
									+ afterpeSecurityDetails.getSecurityVersion();
						 	redemptionScheduleService.persistSecurityRedemptionMapping(fiSecId, rId);
						}
					
					}					
				securityChangeDetector.checkForDifferenceInObjects(beforePESecurityDetails, afterpeSecurityDetails);
				}
			}
			return pjp.proceed();
		}

	}


	private void checkForResetMargin(PESecurityDetails beforePESecurityDetails,
			PESecurityDetails afterpeSecurityDetails) {
		LOGGER.info("Check for the reset margin in floating sec");
		String marginId = null;
		List<FloatingSecurityMargin> beforeFloatingSecurityMarginList = null;
		FloatingSecurity beforefloatingSecurity = null;
		FloatingSecurity afterfloatingSecurity = null;
		if (beforePESecurityDetails instanceof FloatingSecurity) {
			beforefloatingSecurity = (FloatingSecurity) beforePESecurityDetails;
			beforeFloatingSecurityMarginList = getFloatingSecurityMarginByFiSecId(beforefloatingSecurity.getFiSecId());

			if (CollectionUtils.isNotEmpty(beforeFloatingSecurityMarginList)) {
				marginId = beforeFloatingSecurityMarginList.get(0).getMarginId();
				beforefloatingSecurity.setResetMargins(beforeFloatingSecurityMarginList);
			}
			if (afterpeSecurityDetails instanceof FloatingSecurity) {
				afterfloatingSecurity = (FloatingSecurity) afterpeSecurityDetails;

			}
			final boolean isResetMarginSame = checkForDuplicateFloatingSecMargin(beforeFloatingSecurityMarginList,
					afterfloatingSecurity.getResetMargins());
			if (isResetMarginSame) {
				LOGGER.info("There is no change in the reset margin for the edit case");
				LOGGER.info("Updating the mapping entry for security margin");
				if (StringUtils.isNotBlank(marginId)) {
					floatingRateMarginService.persistSecurityMarginMapping(afterpeSecurityDetails.getFiSecId(),
							marginId);
				}

			} else {
				LOGGER.info("Building the new  floating margin set info object");
				List<FloatingSecurityMargin> floatingSecurityMargins = afterfloatingSecurity.getResetMargins();
				if (CollectionUtils.isNotEmpty(floatingSecurityMargins)) {
					floatingRateMarginService.persistFloatingSecurityMarginInfo(afterfloatingSecurity);
					LOGGER.info("Building mapping entry");
					Long fiSecId = afterpeSecurityDetails.getFiSecId();
					String mId = afterpeSecurityDetails.getSecurityId() + "_"
							+ afterpeSecurityDetails.getSecurityVersion();
					floatingRateMarginService.persistSecurityMarginMapping(fiSecId, mId);
				}

			}

		}

	}


	private boolean checkForDuplicateRedemptionSchedule(List<RedemptionSchedule> beforeRedemptionList,
			List<RedemptionSchedule> afterRedemptionList) {
		return beforeRedemptionList.equals(afterRedemptionList);
	}
	
	private boolean checkForDuplicateFloatingSecMargin(List<FloatingSecurityMargin> beforeFloatingSecurityMarginList,
			List<FloatingSecurityMargin> afterFloatingSecurityMarginList) {
		return beforeFloatingSecurityMarginList.equals(afterFloatingSecurityMarginList);
	}
	
	public List<RedemptionSchedule> getRedemptionScheduleByFiSecId(long fiSecId){
        LOGGER.info("Fetching redemption schedule for fisecId {}",fiSecId);		
		final List<RedemptionSchedule> redemptionList=redemptionScheduleService.getRedemptionScheduleByFiSecId(fiSecId);
		LOGGER.info("Redemption Schedule Size {}",redemptionList.size());
		return redemptionList;
	}
	
	public List<FloatingSecurityMargin> getFloatingSecurityMarginByFiSecId(long fiSecId){
        LOGGER.info("Fetching FloatingSecurityMargin for fisecId {}",fiSecId);		
		final List<FloatingSecurityMargin> floatingSecurityMargins=floatingRateMarginService.getFloatingSecurityMarginByFiSecId(fiSecId);
		LOGGER.info("floatingSecurityMargins Size {}",floatingSecurityMargins.size());
		return floatingSecurityMargins;
	}

}
