package com.iuie.basic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 日期工具类
 * @author liu.jie
 * @since 2019年6月21日
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	/** 预定义格式*/
	public static final String[] innerPatterns = { "yyyy-MM-dd HH:mm:ss",
			"yyyy.MM.dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyyMMddHHmmss",
			"yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMdd", "yyyy年MM月dd日", "yyyy.MM.dd" };
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final DateTimeFormatter formater =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	/**
	 * 转换多种格式日期
	 * @author liu.jie
	 * @since 2019年6月21日
	 * @param value
	 * @param customPatterns	自定义格式
	 * @return
	 */
	public static Date parseFullDate(Object value, String... customPatterns) {
		if(StringUtils.isBlank(value)) {
			return null;
		}
		try {
			if (value instanceof String) {
				if(ArrayUtils.isNotEmpty(customPatterns)) {
					value = parseDate(value.toString(), customPatterns);
				} else {
					value = parseDate(value.toString(), innerPatterns);
				}
				return (Date) value;
			} else if (value instanceof Date) {
				return (Date) value;
			} else if(value instanceof Long) {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 格式化日期
	 * @author liu.jie
	 * @since 2019年6月21日
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(final Date date, final String pattern) {
		return org.apache.http.client.utils.DateUtils.formatDate(date, pattern);
	}
	
	/**
	 * 按格式，获取当前时间
	 * @author liu.jie
	 * @since 2019年6月21日
	 * @param field
	 * @return
	 */
	public static Date now(int field) {
		return truncate(new Date(), field);
	}
	public static Date now() {
		return new Date();
	}
	
	
	
	/**
	 * 从当前时间向前或者向后推几天得到的一个字符串时间
	 * @param days
	 * @return 字符串的时间
	 */
	public static String getDate(Integer days){
		Calendar c = Calendar.getInstance();
		String sdate = getDayMin();
		Date date = null;
		try {
			date = sdf.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		String sdayTime = sdf.format(c.getTime());
		return sdayTime;
	}
	/**
	 * 获取一天中最小的时间
	 * @return
	 */
	public static String getDayMin(){
		//获取当天最大的时间
		LocalDate  local = LocalDate.now();
		LocalDateTime min = local.atTime(LocalTime.MIN);
		return formater.format(min);
	}
	

	
	public static void main(String[] args) {

	}
}
