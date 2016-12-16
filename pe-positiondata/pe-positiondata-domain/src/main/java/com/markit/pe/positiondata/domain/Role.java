/*package com.markit.pe.positiondata.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_ROLE_INFO")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
    @Column(name="NAME")
	private String name;

	protected Role() {

	}

	public Role(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("Role[id=%d, name=%s]", id, name);
	}
}
*/