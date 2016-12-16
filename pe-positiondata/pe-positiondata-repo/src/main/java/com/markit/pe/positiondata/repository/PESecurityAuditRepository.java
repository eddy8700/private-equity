package com.markit.pe.positiondata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.positiondata.domain.PESecurityDetailsAudit;

@Repository
public interface PESecurityAuditRepository extends JpaRepository<PESecurityDetailsAudit, Long> {

	List<PESecurityDetailsAudit> findByPeSecurityDetailsFiSecId(Long id);

}
