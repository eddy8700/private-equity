package com.markit.pe.portfoliodata.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {
	
	private static final String DATE_FORMAT = "EEE MMM d HH:mm:ss zzz yyyy";
	private static final String DATE_FORMAT_NEEDED = "yyyy-MM-dd";
	private static final String DATE_FORMAT_AUDIT = "yyyy-MM-dd HH:mm:ss.SSSSSS";
	
	/*public static String buildDateFormat(Date maturityDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		SimpleDateFormat formatneeded = new SimpleDateFormat(DATE_FORMAT_NEEDED);
		Date convertedCurrentDate = null;
		try {
			convertedCurrentDate = sdf.parse(maturityDate.toString());
			formatneeded.format(convertedCurrentDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatneeded.format(convertedCurrentDate);
	}*/
	
	
	public static boolean isAfter(final Date d1,final Date d2) {
		return d2.compareTo(d1) > 0;
	}


	public static boolean isAfterOrEquals(final Date d1,final Date d2) {
		return d2.compareTo(d1) >= 0;
	}

	public static boolean isBefore(final Date d1,final Date d2) {
		return d2.compareTo(d1) < 0;
	}

	public static boolean isBeforeOrEquals(final Date d1,final Date d2) {
		return d2.compareTo(d1) <= 0;
	}

	
	public static String buildDateFormat(Date maturityDate) {
		SimpleDateFormat formatneeded = new SimpleDateFormat(DATE_FORMAT_NEEDED);
		return formatneeded.format(maturityDate);
	}
	
	public static String  auditDateFormat(Date date){

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_AUDIT);
		SimpleDateFormat formatneeded = new SimpleDateFormat(DATE_FORMAT_NEEDED);
		Date convertedCurrentDate = null;
		try {
			convertedCurrentDate = sdf.parse(date.toString());
			formatneeded.format(convertedCurrentDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatneeded.format(convertedCurrentDate);
	
		
	}

	public static Date plusMonths(final Date date,final int months) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		final Date dateToReturn = calendar.getTime();
		return dateToReturn;
	}
	
	
	public static Date subtractMonths(final Date date,final int months) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -months);
		final Date dateToReturn = calendar.getTime();
		return dateToReturn;
	}
	
}
