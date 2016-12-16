package com.markit.pe.comparabledata.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.markit.pe.comparabledata.domain.EVBData;

/**
 * @author mahesh.agarwal
 *
 */
@Repository
public interface EVBCompRawDataRepository extends JpaRepository<EVBData, Long>, QueryDslPredicateExecutor<EVBData> {
	  
	@Query(nativeQuery= true,value="exec sp_FetchSystemComparables ?1,?2,?3,?4,?5,?6,?7")
	List<EVBData> fetchSystemComparables(Date transactionDate, 
			 //Date issueDate, 
			 String classification, 
			 String currency, 
			 Date maturityDate, 
			 BigDecimal ytmTransient, 
			 String sector, 
			 //String principalPaymentType, 
			 BigDecimal averageLife);
	
	@Query(nativeQuery= true,value="exec sp_FetchLatestComparable ?1,?2")
	List<EVBData> getLatestCompsDetails(
			String commaSepISINs, 
			Date asOfDate);
}
