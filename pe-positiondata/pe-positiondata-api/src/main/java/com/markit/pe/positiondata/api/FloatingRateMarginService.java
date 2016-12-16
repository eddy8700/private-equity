package com.markit.pe.positiondata.api;

import java.util.List;

import com.markit.pe.positiondata.domain.FloatingSecurity;
import com.markit.pe.positiondata.domain.FloatingSecurityMargin;

public interface FloatingRateMarginService {
	
	void persistSecurityMarginMapping(final Long fiSecId, final String marginId);
	
	void persistFloatingSecurityMarginInfo(FloatingSecurity floatingSecurity);

	List<FloatingSecurityMargin> getFloatingSecurityMarginByFiSecId(long fiSecId);


}
