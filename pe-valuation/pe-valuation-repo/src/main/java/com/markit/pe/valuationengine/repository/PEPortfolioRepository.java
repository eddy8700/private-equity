
package com.markit.pe.valuationengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.positiondata.domain.PEPortfolio;

@Repository
public interface PEPortfolioRepository extends JpaRepository<PEPortfolio, Long>{

	PEPortfolio findByPortfolioName(String clientName);
}
