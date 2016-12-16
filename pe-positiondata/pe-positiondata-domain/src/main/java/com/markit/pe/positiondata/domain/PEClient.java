/**
 * 
 */
package com.markit.pe.positiondata.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author mahesh.agarwal
 *
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "T_Client_Info")
public class PEClient implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CLIENT_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	@Column(name = "Client_Name")
	@Access(AccessType.FIELD)
	private String clientName;

	@Column(name = "Client_Type")
	@Access(AccessType.FIELD)
	private String clientType;

	@Column(name = "SHORT_NAME")
	@Access(AccessType.FIELD)
	private String clientShortName;

	@Column(name = "ABBREVIATED_NAME")
	@Access(AccessType.FIELD)
	private String clientAbbrName;



	public PEClient(String clientName, String clientType) {
		this.clientName = clientName;
		this.clientType = clientType;
	}

	
}
