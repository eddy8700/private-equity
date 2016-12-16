/*package com.markit.pe.positiondata.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="T_USER_INFO")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	
	@Column(name="EMAIL")
	private String email;

	@ManyToMany
	@JoinTable(name = "T_USER_ROLE_INFO", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "id"))
	private Set<Role> roles;



	protected User() {
	}

	

	public User( String email, Set<Role> roles) {
		super();
		this.email = email;
		this.roles = roles;
	}



	public String getEmail() {
		return email;
	}

	public int getId() {
		return id;
	}

	

	public Set<Role> getRoles() {
		return roles;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		String rolesString = "";
		if (roles != null) {
			for (Role role : roles) {
				rolesString += role.toString();
			}
		}

		return String.format("User[id=%d, email=%s, roles=%s]", id, email, rolesString);
	}
}
*/