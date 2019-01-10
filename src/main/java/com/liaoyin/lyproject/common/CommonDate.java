package com.liaoyin.lyproject.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * 创 建 人 ：
 * 创建日期：2018年1月2日
 * 描       述：公共时间类
 */
public class CommonDate {

	/**
	 * 
	 * 创 建 人 ：  创建日期：2017年12月29日 描 述：date转String
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2017年12月29日 描 述：String转date yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date StringToDate(String date){
		if (Common.isNull(date)){
			return null;
		}
		Date de = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			de = sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return de;
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2017年12月16日 描 述：String转date yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date StringTodateYMD(String date){
		if (Common.isNull(date)){
			return null;
		}
		Date de = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			de = sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return de;
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2017年12月29日 描 述：获取当前时间的时间戳
	 * 
	 * @return
	 */
	public static long getTimestamp() {
		return System.currentTimeMillis();
	}

	/**
	 *
	 * 创 建 人 ：  创建日期：2017年12月6日 描 述：根据指定日期和小时得到新的日期 -返回Date
	 *
	 * @return
	 * @throws Exception
	 */
	public static Date appointDateHour(Date date, int hour){
		long time = date.getTime(); // 得到指定日期的毫秒数
		long newTime = hour * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
		time += newTime; // 相加得到新的毫秒数
		return new Date(time); // 将毫秒数转换成日期
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2017年12月6日 描 述：获取指定时间的年
	 * 
	 * @param date
	 * @return
	 */
	public static int appointDateYear(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		return now.get(Calendar.YEAR);
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2017年12月6日 描 述：获取指定时间的月
	 * 
	 * @param date
	 * @return
	 */
	public static int appointDateMonth(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		return now.get(Calendar.MONTH) + 1;
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2017年12月6日 描 述：获取指定时间的日
	 * 
	 * @param date
	 * @return
	 */
	public static int appointDateDay(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		return now.get(Calendar.DAY_OF_MONTH);
	}

}
