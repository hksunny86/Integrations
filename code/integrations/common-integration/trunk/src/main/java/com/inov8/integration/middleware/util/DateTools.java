package com.inov8.integration.middleware.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Provides support for converting dates to strings and vice-versa. The strings
 * are structured so that lexicographic sorting orders them by date, which makes
 * them suitable for use as field values and search terms.
 * 
 * <P>
 * This class also helps you to limit the resolution of your dates. Do not save
 * dates with a finer resolution than you really need, as then RangeQuery and
 * PrefixQuery will require more memory and become slower.
 * 
 * <P>
 * class take slightly more space, unless your selected resolution is set to
 * <code>Resolution.DAY</code> or lower.
 */
public class DateTools {

	private final static TimeZone GMT = TimeZone.getTimeZone("GMT");

	private static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");
	private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("yyyyMMddHH");
	private static final SimpleDateFormat MINUTE_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");
	private static final SimpleDateFormat SECOND_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat MILLISECOND_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	static {
		// times need to be normalized so the value doesn't depend on the
		// location the index is created/used:
		YEAR_FORMAT.setTimeZone(GMT);
		MONTH_FORMAT.setTimeZone(GMT);
		DAY_FORMAT.setTimeZone(GMT);
		HOUR_FORMAT.setTimeZone(GMT);
		MINUTE_FORMAT.setTimeZone(GMT);
		SECOND_FORMAT.setTimeZone(GMT);
		MILLISECOND_FORMAT.setTimeZone(GMT);
	}

	// cannot create, the class has static methods only
	private DateTools() {
	}

	/**
	 * Converts a Date to a string suitable for indexing.
	 * 
	 * @param date
	 *            the date to be converted
	 * @param resolution
	 *            the desired resolution, see
	 *            {@link #round(Date, Resolution)}
	 * @return a string in format <code>yyyyMMddHHmmssSSS</code> or shorter,
	 *         depeding on <code>resolution</code>; using UTC as timezone
	 */
	public static String dateToString(Date date, Resolution resolution) {
		return timeToString(date.getTime(), resolution);
	}
	
	public static String dateToString(Date date, String dateFormat) {
		if(date == null)
			return null;

		String formatedDate = null;
		try{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			formatedDate = simpleDateFormat.format(date);
		}catch(IllegalArgumentException ex){
			throw new IllegalArgumentException("Date format is not correct: " + formatedDate);
		}
		return formatedDate;
	}
	
	
	public static Date stringToDate(String dateString, String dateFormat) throws ParseException {
		if(StringUtils.isEmpty(dateString))
			return null;

		Date parsedDate = null;
		try{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			parsedDate = simpleDateFormat.parse(dateString);
		}catch(ParseException ex){
			throw new ParseException("Input is not valid date string: " + dateString, 0);
		}catch(IllegalArgumentException ex){
			throw new IllegalArgumentException("Date format is not correct: " + dateFormat);
		}
		return parsedDate;
	}

	/**
	 * Converts a millisecond time to a string suitable for indexing.
	 * 
	 * @param time
	 *            the date expressed as milliseconds since January 1, 1970,
	 *            00:00:00 GMT
	 * @param resolution
	 *            the desired resolution, see
	 *            {@link #round(long, Resolution)}
	 * @return a string in format <code>yyyyMMddHHmmssSSS</code> or shorter,
	 *         depeding on <code>resolution</code>; using UTC as timezone
	 */
	public static String timeToString(long time, Resolution resolution) {
		Calendar cal = Calendar.getInstance(GMT);

		// protected in JDK's prior to 1.4
		// cal.setTimeInMillis(round(time, resolution));

		cal.setTime(new Date(round(time, resolution)));

		String result;
		if (resolution == Resolution.YEAR) {
			synchronized (YEAR_FORMAT) {
				result = YEAR_FORMAT.format(cal.getTime());
			}
		} else if (resolution == Resolution.MONTH) {
			synchronized (MONTH_FORMAT) {
				result = MONTH_FORMAT.format(cal.getTime());
			}
		} else if (resolution == Resolution.DAY) {
			synchronized (DAY_FORMAT) {
				result = DAY_FORMAT.format(cal.getTime());
			}
		} else if (resolution == Resolution.HOUR) {
			synchronized (HOUR_FORMAT) {
				result = HOUR_FORMAT.format(cal.getTime());
			}
		} else if (resolution == Resolution.MINUTE) {
			synchronized (MINUTE_FORMAT) {
				result = MINUTE_FORMAT.format(cal.getTime());
			}
		} else if (resolution == Resolution.SECOND) {
			synchronized (SECOND_FORMAT) {
				result = SECOND_FORMAT.format(cal.getTime());
			}
		} else if (resolution == Resolution.MILLISECOND) {
			synchronized (MILLISECOND_FORMAT) {
				result = MILLISECOND_FORMAT.format(cal.getTime());
			}
		} else {
			throw new IllegalArgumentException("unknown resolution " + resolution);
		}
		return result;
	}

	/**
	 * Converts a string produced by <code>timeToString</code> or
	 * <code>dateToString</code> back to a time, represented as the number of
	 * milliseconds since January 1, 1970, 00:00:00 GMT.
	 * 
	 * @param dateString
	 *            the date string to be converted
	 * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT
	 * @throws ParseException
	 *             if <code>dateString</code> is not in the expected format
	 */
	public static long stringToTime(String dateString) throws ParseException {
		return stringToDate(dateString).getTime();
	}

	/**
	 * Converts a string produced by <code>timeToString</code> or
	 * <code>dateToString</code> back to a time, represented as a Date object.
	 * 
	 * @param dateString
	 *            the date string to be converted
	 * @return the parsed time as a Date object
	 * @throws ParseException
	 *             if <code>dateString</code> is not in the expected format
	 */
	public static Date stringToDate(String dateString) throws ParseException {
		Date date;
		if (dateString.length() == 4) {
			synchronized (YEAR_FORMAT) {
				date = YEAR_FORMAT.parse(dateString);
			}
		} else if (dateString.length() == 6) {
			synchronized (MONTH_FORMAT) {
				date = MONTH_FORMAT.parse(dateString);
			}
		} else if (dateString.length() == 8) {
			synchronized (DAY_FORMAT) {
				date = DAY_FORMAT.parse(dateString);
			}
		} else if (dateString.length() == 10) {
			synchronized (HOUR_FORMAT) {
				date = HOUR_FORMAT.parse(dateString);
			}
		} else if (dateString.length() == 12) {
			synchronized (MINUTE_FORMAT) {
				date = MINUTE_FORMAT.parse(dateString);
			}
		} else if (dateString.length() == 14) {
			synchronized (SECOND_FORMAT) {
				date = SECOND_FORMAT.parse(dateString);
			}
		} else if (dateString.length() == 17) {
			synchronized (MILLISECOND_FORMAT) {
				date = MILLISECOND_FORMAT.parse(dateString);
			}
		} else {
			throw new ParseException("Input is not valid date string: " + dateString, 0);
		}
		return date;
	}
	
	/**
	 * Limit a date's resolution. For example, the date
	 * <code>2004-09-21 13:50:11</code> will be changed to
	 * <code>2004-09-01 00:00:00</code> when using <code>Resolution.MONTH</code>
	 * .
	 * 
	 * @param resolution
	 *            The desired resolution of the date to be returned
	 * @return the date with all values more precise than
	 *         <code>resolution</code> set to 0 or 1
	 */
	public static Date round(Date date, Resolution resolution) {
		return new Date(round(date.getTime(), resolution));
	}

	/**
	 * Limit a date's resolution. For example, the date
	 * <code>1095767411000</code> (which represents 2004-09-21 13:50:11) will be
	 * changed to <code>1093989600000</code> (2004-09-01 00:00:00) when using
	 * <code>Resolution.MONTH</code>.
	 * 
	 * @param resolution
	 *            The desired resolution of the date to be returned
	 * @return the date with all values more precise than
	 *         <code>resolution</code> set to 0 or 1, expressed as milliseconds
	 *         since January 1, 1970, 00:00:00 GMT
	 */
	public static long round(long time, Resolution resolution) {
		Calendar cal = Calendar.getInstance(GMT);

		// protected in JDK's prior to 1.4
		// cal.setTimeInMillis(time);

		cal.setTime(new Date(time));

		if (resolution == Resolution.YEAR) {
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		} else if (resolution == Resolution.MONTH) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		} else if (resolution == Resolution.DAY) {
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		} else if (resolution == Resolution.HOUR) {
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		} else if (resolution == Resolution.MINUTE) {
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		} else if (resolution == Resolution.SECOND) {
			cal.set(Calendar.MILLISECOND, 0);
		} else if (resolution == Resolution.MILLISECOND) {
			// don't cut off anything
		} else {
			throw new IllegalArgumentException("unknown resolution " + resolution);
		}
		return cal.getTime().getTime();
	}

	/** Specifies the time granularity. */
	public static class Resolution {

		public static final Resolution YEAR = new Resolution("year");
		public static final Resolution MONTH = new Resolution("month");
		public static final Resolution DAY = new Resolution("day");
		public static final Resolution HOUR = new Resolution("hour");
		public static final Resolution MINUTE = new Resolution("minute");
		public static final Resolution SECOND = new Resolution("second");
		public static final Resolution MILLISECOND = new Resolution("millisecond");

		private String resolution;

		private Resolution() {
		}

		private Resolution(String resolution) {
			this.resolution = resolution;
		}

		public String toString() {
			return resolution;
		}
	}

	public static void main(String[] args) {
		DateTime dateTime = new DateTime(2015, 2, 23, 0, 0);
		System.out.println(dateTime.toDate());
		System.out.println(DateTools.dateToString(dateTime.toDate(), "yyyyMMdd"));
	}
}