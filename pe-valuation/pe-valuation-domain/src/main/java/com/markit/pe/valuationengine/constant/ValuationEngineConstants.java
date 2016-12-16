/**
 * 
 */
package com.markit.pe.valuationengine.constant;

/**
 * @author mahesh.agarwal
 *
 */
public final class ValuationEngineConstants {

	public static final double NOTIONAL_AMOUNT_QAG=100d;
	public static final double NOTIONAL_FACE_QAG=100d;
	public static final String DEFAULT_DAY_COUNT_CONV="Actual/360";
	//public static final double CLEAN_PRICE_QAG=100d;

	private ValuationEngineConstants() {
		throw new AssertionError();
	}
}
