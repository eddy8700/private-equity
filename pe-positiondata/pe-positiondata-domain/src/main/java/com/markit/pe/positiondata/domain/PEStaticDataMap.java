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
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name="T_PE_STATIC_DATA_MAP")
public class PEStaticDataMap implements Serializable {
private static final long serialVersionUID = 1L;	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "STATIC_DATA_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	@Column(name="TYPE")
	private String type;
		
	@Column(name="VALUE")
	private String value;
	
	@Column(name="PARENT_ID")
	private Long parentId;
	
	
}
