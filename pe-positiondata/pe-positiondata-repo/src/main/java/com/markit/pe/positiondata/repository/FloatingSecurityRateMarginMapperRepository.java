package com.markit.pe.positiondata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.positiondata.domain.FloatingSecurityRateMarginMapper;

@Repository
public interface FloatingSecurityRateMarginMapperRepository extends JpaRepository<FloatingSecurityRateMarginMapper, Long> {
	
	

}
