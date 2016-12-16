
package com.markit.pe.positiondata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.positiondata.domain.PEClient;

@Repository
public interface PEClientRepository extends JpaRepository<PEClient, Long>{
	
	
	public List<PEClient> findByClientAbbrName(final String abbName);
	

}
