package com.markit.pe.commons.security.domain;

import java.io.Serializable;
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

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * 
 * @author aditya.gupta2
 *
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_ROLE_INFO")
public class Role implements Serializable, GrantedAuthority {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "ROLE_NAME")
	private String roleName;

	@Override
	public String getAuthority() {
		return getRoleName();
	}

	@ManyToMany
	@JoinTable(name = "T_ROLE_PERMISSION_INFO", joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "id"))
	private Set<Permission> permissions;

}
