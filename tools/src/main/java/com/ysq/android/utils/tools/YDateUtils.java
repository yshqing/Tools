package com.ysq.android.utils.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class YDateUtils {

	public static String getDateString(long milliseconds) {
		return getDateString(milliseconds, "yyyy-MM-dd");
	}
	
	public static String getDateString(long milliseconds, String formatStr) {
		Date date = new Date(milliseconds);
		SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.US);
		String data = format.format(date);
		return data;
	}

	/**
	 * 将指定格式的字符串转换成{@link Calendar}
	 *
	 * @param dateStr
	 *            时间字符串
	 * @param format
	 *            格式
	 * @return {@link Calendar}
	 */
	public static Calendar getCalendar(String dateStr, String format) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat yearFormat = new SimpleDateFormat(format);
		try {
			Date date = yearFormat.parse(dateStr);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}
}
