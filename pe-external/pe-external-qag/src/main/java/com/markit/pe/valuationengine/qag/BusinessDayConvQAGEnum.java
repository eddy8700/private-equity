/**
 * 
 */
package com.markit.pe.valuationengine.qag;

/**
 * @author mahesh.agarwal
 *
 */
public enum BusinessDayConvQAGEnum {
	
	Following("F", "Following"),
	Preceding("P", "Preceding"),
	Modified_following("M", "Modified following"),
	//Modified_following_bimonthly("Y", "Modified following bimonthly"),
	End_of_month("M", "End of month");
	
	String std;
	
	String supplied;
	
	private BusinessDayConvQAGEnum(String std, String supplied) {
		this.std = std;
		this.supplied = supplied;
	}


	/**
	 * @return the std
	 */
	public String getStd() {
		return std;
	}


	/**
	 * @param std the std to set
	 */
	public void setStd(String std) {
		this.std = std;
	}


	/**
	 * @return the supplied
	 */
	public String getSupplied() {
		return supplied;
	}

	/**
	 * @param supplied the supplied to set
	 */
	public void setSupplied(String supplied) {
		this.supplied = supplied;
	}

	public static BusinessDayConvQAGEnum getBusinessDayConvEnum (String supplied){
		if(supplied!=null){
			for (BusinessDayConvQAGEnum businessDayConvEnum : BusinessDayConvQAGEnum.values()) {
				if(supplied.equalsIgnoreCase(businessDayConvEnum.getSupplied())){
					return businessDayConvEnum;		 
				}
			}
		} 
		return Modified_following;
	}
}
