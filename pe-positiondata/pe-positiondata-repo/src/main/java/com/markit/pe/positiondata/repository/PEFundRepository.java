
package com.markit.pe.positiondata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.positiondata.domain.PEFund;

@Repository
public interface PEFundRepository extends JpaRepository<PEFund, Long>{

	PEFund findByFundName(String fundName);
	

}
