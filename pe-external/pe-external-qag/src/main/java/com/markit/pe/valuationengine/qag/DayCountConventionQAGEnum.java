/**
 * 
 */
package com.markit.pe.valuationengine.qag;

/**
 * @author mahesh.agarwal
 *
 */
public enum DayCountConventionQAGEnum {
	
	//DCC_1_1("ACT/360", "1/1"),
	//DCC_30_360_Methods("30E/360", "30/360 methods"),
	DCC_30_360("30A/360NonEom", "30/360"),
	DCC_30E_360("30E/360", "30E/360"),
	//DCC_30E_360_ISDA("30E/360", "30E/360 (ISDA)"),
	//DCC_30E_Plus_360_ISDA("30E/360", "30E+/360 ISDA"),
	DCC_ACT_360("ACT/360", "ACT/360"),
	DCC_ACT_365_Fixed("ACT/365", "ACT/365 Fixed"),
	DCC_ACT_365_L("ACT/ACT", "ACT/365 L"),
	//DCC_ACT_365_A("ACT/365", "ACT/365 A"),
	DCC_NL_365("ACT/365", "NL/365"),
	DCC_ACT_ACT_ISDA("ACT/ACT ISDA", "ACT/ACT ISDA"),
	DCC_ACT_ACT_ICMA("ACT/ACT", "ACT/ACT ICMA"),
	DCC_Business_252("BUS/252", "Business/252");
	
	
	String std;
	
	String supplied;
	
	private DayCountConventionQAGEnum(String std, String supplied) {
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

	public static DayCountConventionQAGEnum getDayCountConvEnum (String supplied){
		if(supplied!=null){
			for (DayCountConventionQAGEnum dayCountConvention : DayCountConventionQAGEnum.values()) {
				if(supplied.equalsIgnoreCase(dayCountConvention.getSupplied())){
					return dayCountConvention;		 
				}
			}
		} 
		return DCC_ACT_360;
	}
}
