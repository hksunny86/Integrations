package com.inov8.verifly.common.util;
import java.util.Date;


public class DateUtil {

		public static Date addMinutes (Date date, int minutes) {
			date.setTime(date.getTime() + minutes * 60000);
			return date;
		}
}
