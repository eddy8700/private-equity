package com.markit.pe.positiondata.domain;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUploadDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7692177676731773752L;

	private MultipartFile multipartFile;
	
	private PEClient clientDetails;


	
	
	
}
