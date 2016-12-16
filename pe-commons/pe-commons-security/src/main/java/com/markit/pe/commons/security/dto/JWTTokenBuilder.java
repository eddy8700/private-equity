package com.markit.pe.commons.security.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * 
 * @author aditya.gupta2
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JWTTokenBuilder {
	
	private String userName;
	
	private String userEmail;
	
	private  List<String> roles;
	
	private List<String> permissions;
	
	private Long expirationTimeInMillis;

}
