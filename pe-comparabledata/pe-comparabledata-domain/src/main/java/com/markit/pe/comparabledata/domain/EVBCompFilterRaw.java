/**
 * 
 */
package com.markit.pe.comparabledata.domain;

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
import lombok.Setter;

/**
 * @author mahesh.agarwal
 *
 */
@Entity
@Getter
@Setter
@Table(name = "T_REF_FILTER_VALUE")
public class EVBCompFilterRaw  implements Serializable {
	
	private static final long serialVersionUID = 1L;	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	@Column(name="TYPE")
	private String type;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="VALUE")
	private String value;
	
	@Column(name="PARENT")
	private String parent;
	
	@Column(name="REFERENCE_VALUE")
	private Integer referenceValue;

	

}
