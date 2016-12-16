package com.markit.pe.valuationengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.markit.pe.positiondata.domain.FloatingSecurityMargin;

@Repository
public interface FloatingSecurityMarginRepository extends JpaRepository<FloatingSecurityMargin, Long> {
	
	@Query(nativeQuery= true,value="select margin.* from [T_FLOATING_MARGIN] margin, T_FI_SEC_RATE_MARGIN map where map.MARGIN_ID = margin.MARGIN_ID and map.FI_SEC_ID=?1")
	public java.util.List<FloatingSecurityMargin> getFloatingSecurityMarginByFiSecId(final long fiSecId);


}
