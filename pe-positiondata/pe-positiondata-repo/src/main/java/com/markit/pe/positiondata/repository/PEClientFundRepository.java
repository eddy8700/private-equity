
package com.markit.pe.positiondata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.domain.PEClientFundInfo;
import com.markit.pe.positiondata.domain.PEFund;

@Repository
public interface PEClientFundRepository extends JpaRepository<PEClientFundInfo, Long>{

	PEClientFundInfo findByFundAndClient(PEFund fund, PEClient persistedPeClient);
	

}
