package com.markit.pe.commons.security.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * 
 * @author aditya.gupta2
 *
 */
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "T_USER_INFO")
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5962377075810731122L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "EMAIL")
	private String userEmail;
	
	
	@Column(name = "USER_NAME")
	private String userName;

	@ManyToMany
	@JoinTable(name = "T_USER_ROLE_INFO", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "id"))
	private Set<Role> roles;
	
	@Transient
	private Set<Permission> permissions;
	
	@Override
	public String toString() {
		String rolesString = "";
		if (roles != null) {
			for (Role role : roles) {
				rolesString += role.toString();
			}
		}

		return String.format("User[id=%d, email=%s, roles=%s]", id, userEmail, rolesString);
	}

	@Transient
	public Set<Permission> getPermissions() {
		Set<Permission> perms = new HashSet<Permission>();
		for (Role role : roles) {
			perms.addAll(role.getPermissions());
		}
		return perms;
	}
	
	public User(final String userName,final String userEmail,final Set<GrantedAuthority> authorities){
		this.userName=userName;
		this.userEmail=userEmail;
		//this.roles=authorities;
		
	}

	@Override
	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		 authorities.addAll(getRoles());
		authorities.addAll(getPermissions());
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.userEmail; // as of now email is the username
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
