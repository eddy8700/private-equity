package com.markit.pe.positiondata.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * @author aditya.gupta
 *
 */
@Entity
@Getter
@Setter
@Table( name="T_SEC_UPLOAD_STATUS")
public class SecurityUploadStatus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "UPLOAD_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	
	public Long getId() {
		return id;
	}

	public enum SecurityLoadStatus{
		 NEW("New"),IN_PROGRESS("In Progress"),LOADED_WITH_EXCEPTIONS("Loaded with Exception"),LOAD_COMPLETED("Load Completed"),LOAD_FAILED("Load Failed");
		  private String displayName;
		  
		  SecurityLoadStatus(String displayName) {
		        this.displayName = displayName;
		    }
		  
		  public String displayName() { return displayName; }
		public String parse(SecurityLoadStatus status,String fileName){
			switch (status) {
			case NEW:
			case IN_PROGRESS:
				return "Security upload for the file "+fileName+"still in progress";
			case LOADED_WITH_EXCEPTIONS:
				return "Security upload for the file "+fileName+" loaded with exceptions";
			case LOAD_COMPLETED:
				return "Security upload for the file "+fileName+" loaded successfully";
			case LOAD_FAILED:
				return "Security upload for the file "+fileName+" failed";
			default:
				break;
			}
			return null;
			
		}
		 public static SecurityLoadStatus convert(String str) {
		        for (SecurityLoadStatus loadStatus : SecurityLoadStatus.values()) {
		            if (loadStatus.displayName().equals(str)) {
		                return loadStatus;
		            }
		        }
		        return null;
		    }
	}
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="PORTFOLIO_ID")
	@Access(AccessType.FIELD)
	private PEPortfolio pePortfolio;
	
	@Column(name="LOAD_STATUS")
	@Access(AccessType.FIELD)
	private SecurityLoadStatus loadStatus;
	
	@Column(name="FILE_NAME")
	@Access(AccessType.FIELD)
	private String fileName;

	

	public SecurityUploadStatus(PEPortfolio pePortfolio, SecurityLoadStatus loadStatus, String fileName) {
		super();
		this.pePortfolio = pePortfolio;
		this.loadStatus = loadStatus;
		this.fileName = fileName;
	}

	

}
