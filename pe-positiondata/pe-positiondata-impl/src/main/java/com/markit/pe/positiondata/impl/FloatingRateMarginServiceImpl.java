package com.markit.pe.positiondata.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.markit.pe.positiondata.api.FloatingRateMarginService;
import com.markit.pe.positiondata.domain.FloatingSecurity;
import com.markit.pe.positiondata.domain.FloatingSecurityMargin;
import com.markit.pe.positiondata.domain.FloatingSecurityRateMarginMapper;
import com.markit.pe.positiondata.repository.FloatingSecurityMarginRepository;
import com.markit.pe.positiondata.repository.FloatingSecurityRateMarginMapperRepository;

@Component
public class FloatingRateMarginServiceImpl implements FloatingRateMarginService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FloatingRateMarginServiceImpl.class);
	
	@Autowired
	private FloatingSecurityMarginRepository floatingSecurityMarginRepository;
	
	
	@Autowired
	private FloatingSecurityRateMarginMapperRepository floatingSecurityRateMarginMapperRepository;

	@Override
	public void persistSecurityMarginMapping(final Long fiSecId, final String marginId) {
		LOGGER.info("Mapping Security and floating rate margin");
		final FloatingSecurityRateMarginMapper floatingMarginMapper = new FloatingSecurityRateMarginMapper(fiSecId, marginId);
		floatingSecurityRateMarginMapperRepository.save(floatingMarginMapper);
		LOGGER.info("Mapping info saved into the db");
	}

	@Override
	public void persistFloatingSecurityMarginInfo(FloatingSecurity floatingSecurity) {
		final List<FloatingSecurityMargin> floatingSecurityMargins = floatingSecurity.getResetMargins();
		LOGGER.info("Fetching the floating margin list of size {}", floatingSecurityMargins.size());
		//if (!redemtionScheduleList.isEmpty() && redemtionScheduleList != null) {
		for (FloatingSecurityMargin floatingSecurityMargin : floatingSecurityMargins) {
			floatingSecurityMargin.setMarginId(
					floatingSecurity.getSecurityId() + "_" + floatingSecurity.getSecurityVersion());
			floatingSecurityMarginRepository.save(floatingSecurityMargin);
		}
		LOGGER.info("Persisted the floating security margin info of size {}", floatingSecurityMargins.size());
		//}
	}

	@Override
	public List<FloatingSecurityMargin> getFloatingSecurityMarginByFiSecId(long fiSecId) {
		 return floatingSecurityMarginRepository.getFloatingSecurityMarginByFiSecId(fiSecId);
	}

}
