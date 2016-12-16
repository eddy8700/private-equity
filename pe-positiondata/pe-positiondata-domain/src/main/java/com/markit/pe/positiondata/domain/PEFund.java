/**
 * 
 */
package com.markit.pe.positiondata.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mahesh.agarwal
 *
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "T_FUND_INFO")
public class PEFund implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "FUND_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;

	@Column(name = "FUND_NAME")
	@Access(AccessType.FIELD)
	private String fundName;


	@Column(name = "NUMBER_OF_PORTFOLIO")
	@Access(AccessType.FIELD)
	private Integer noOfPortfolio;

	@Column(name = "FUND_VALUE")
	@Access(AccessType.FIELD)
	private BigDecimal fundValue;

}
