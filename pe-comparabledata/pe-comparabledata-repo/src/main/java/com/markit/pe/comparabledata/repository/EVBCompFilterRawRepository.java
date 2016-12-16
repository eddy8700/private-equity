/**
 * 
 */
package com.markit.pe.comparabledata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.comparabledata.domain.EVBCompFilterRaw;

/**
 * @author mahesh.agarwal
 *
 */
@Repository
public interface EVBCompFilterRawRepository extends JpaRepository<EVBCompFilterRaw, Long>{

}
