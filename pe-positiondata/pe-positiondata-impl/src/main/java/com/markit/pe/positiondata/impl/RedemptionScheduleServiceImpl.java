package com.markit.pe.positiondata.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.markit.pe.positiondata.api.RedemptionScheduleService;
import com.markit.pe.positiondata.domain.PESecurityDetails;
import com.markit.pe.positiondata.domain.RedemptionSchedule;
import com.markit.pe.positiondata.domain.SecurityRedemption;
import com.markit.pe.positiondata.repository.RedemptionScheduleRepository;
import com.markit.pe.positiondata.repository.SecurityRedemptionDetailsRepo;

@Component  
public class RedemptionScheduleServiceImpl  implements RedemptionScheduleService{
	
	
	@Autowired
	RedemptionScheduleRepository redemptionScheduleRepository;
	
	@Autowired
    SecurityRedemptionDetailsRepo securityRedemptionDetailsRepo;

	private static final Logger LOGGER = LoggerFactory.getLogger(RedemptionScheduleServiceImpl.class);
	
	@Override
	public void persistSecurityRedemptionMapping(final Long fiSecId, final String redemptionId) {
		LOGGER.info("Mapping Security and Redemption");
		/*final Long fiSecId = persistedSecurityDetails.getId();
		final String redemptionId = persistedSecurityDetails.getSecurityId() + "_"
				+ persistedSecurityDetails.getSecurityVersion();*/
		final SecurityRedemption redemption = new SecurityRedemption(fiSecId, redemptionId);
		securityRedemptionDetailsRepo.save(redemption);
		LOGGER.info("Mapping info saved into the db");
	}
	
	@Override
	public void persistRedemptionInfo(final PESecurityDetails peSecurityDetails) {
		final List<RedemptionSchedule> redemtionScheduleList = peSecurityDetails.getRedemptionSchedules();
		LOGGER.info("Fetching the redemption schedule set info of size {}", redemtionScheduleList.size());
		//if (!redemtionScheduleList.isEmpty() && redemtionScheduleList != null) {
		for (RedemptionSchedule redemptionSchedule : redemtionScheduleList) {
			redemptionSchedule.setRedemptionId(
					peSecurityDetails.getSecurityId() + "_" + peSecurityDetails.getSecurityVersion());
			redemptionScheduleRepository.save(redemptionSchedule);
		}
		LOGGER.info("Persisted the redemption schedule  info of size {}", redemtionScheduleList.size());
		//}
		
	}
	
	
	@Override
	public java.util.List<RedemptionSchedule> getRedemptionScheduleByFiSecId(final long fiSecId){
		 return redemptionScheduleRepository.getRedemptionScheduleByFiSecId(fiSecId);
	}
}
