/**
 * 
 */
package com.markit.pe.valuationengine.qag;

/**
 * @author mahesh.agarwal
 *
 */
public enum CouponFrequencyQAGEnum {
	
	Monthly("1M", 12, "Monthly", 1, "M"),
	BiMonthly("2M", 6, "BiMonthly", 2, "M"),
	Quarterly("3M", 4, "Quarterly", 3, "M"),
	SemiAnnually("6M", 2, "SemiAnnually", 6, "M"),
	Annually("12M", 1, "Annually", 12, "M");
	
	String supplied;
	
	int noCpns;
	
	String freqStr;
	
	String suffix;
	
	int prefix;
	
	private CouponFrequencyQAGEnum(String supplied, int noCpns, String freqStr, int prefix, String suffix) {
		this.supplied = supplied;
		this.noCpns = noCpns;
		this.freqStr = freqStr;
		this.suffix = suffix;
		this.prefix = prefix;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @return the prefix
	 */
	public int getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(int prefix) {
		this.prefix = prefix;
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

	
	/**
	 * @return the noCpns
	 */
	public int getNoCpns() {
		return noCpns;
	}

	/**
	 * @param noCpns the noCpns to set
	 */
	public void setNoCpns(int noCpns) {
		this.noCpns = noCpns;
	}

	/**
	 * @return the freqStr
	 */
	public String getFreqStr() {
		return freqStr;
	}

	/**
	 * @param freqStr the freqStr to set
	 */
	public void setFreqStr(String freqStr) {
		this.freqStr = freqStr;
	}

	
	public static CouponFrequencyQAGEnum getCouponFrequencyEnum (String supplied){
		if(supplied!=null){
			for (CouponFrequencyQAGEnum couponFrequencyEnum : CouponFrequencyQAGEnum.values()) {
				if(supplied.equalsIgnoreCase(couponFrequencyEnum.getSupplied())){
					return couponFrequencyEnum;		 
				}
			}
		} 
		return null;
	}
}
