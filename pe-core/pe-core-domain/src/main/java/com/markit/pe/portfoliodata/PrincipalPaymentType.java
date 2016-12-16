package com.markit.pe.portfoliodata;

public enum PrincipalPaymentType {
	
	SINKING_FUND("Sinking Fund"),BULLET_ON_MATURITY("Bullet on Maturity");
	
	private String displayName;
	
	private PrincipalPaymentType(String displayName) {
     this.displayName=displayName;
	}
	public String displayName(){
		return this.displayName;
	}
	 public static PrincipalPaymentType convert(String str) {
	        for (PrincipalPaymentType loadStatus : PrincipalPaymentType.values()) {
	            if (loadStatus.displayName().equals(str)) {
	                return loadStatus;
	            }
	        }
	        return null;
	    }
	
}
