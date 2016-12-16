package com.markit.pe.positiondata.api;

import java.util.List;

import com.markit.pe.positiondata.domain.PESecurityDetails;
import com.markit.pe.positiondata.domain.RedemptionSchedule;

public interface RedemptionScheduleService {

	void persistSecurityRedemptionMapping(final Long fiSecId, final String redemptionId);

	void persistRedemptionInfo(PESecurityDetails peSecurityDetails);

	List<RedemptionSchedule> getRedemptionScheduleByFiSecId(long fiSecId);

}
