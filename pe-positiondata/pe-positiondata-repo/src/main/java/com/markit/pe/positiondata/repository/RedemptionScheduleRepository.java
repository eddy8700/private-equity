package com.markit.pe.positiondata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.markit.pe.positiondata.domain.RedemptionSchedule;
@Repository
public interface RedemptionScheduleRepository extends JpaRepository<RedemptionSchedule, Long> {

	@Query(nativeQuery= true,value="select red.* from [T_REDEMPTION_SCHEDULE] red, T_FI_SEC_REDEMPTION map where map.REDEMPTION_ID = red.REDEMPTION_ID and map.FI_SEC_ID=?1")
	public java.util.List<RedemptionSchedule> getRedemptionScheduleByFiSecId(final long fiSecId);
	
}
