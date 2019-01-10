package com.liaoyin.lyproject.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：日期工具类
 * @日期：Created in 2018/6/8 15:05
 */
public class DateUtil {
	
	private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdf_date=new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdf_date_currentTime=new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentTime4Str(){
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	
	
	/**
	 * 获取当前时间毫秒值
	 * @return
	 */
	public static String getcurrentTimeMillis(){
		return System.currentTimeMillis()+"";
	}
    /**
     * 获取当前时间秒数
     * @return
     */
    public static String getcurrentSecondsString(){
        long currentTimeMillis = System.currentTimeMillis();
        long l = currentTimeMillis/1000;
        String ls = Long.toString(l);
        return ls;
	}
	/**
	 * 获取当前时间秒数
	 * @return  int
	 */
	public static Integer getCurrentSecondsInt(){
		long currentTimeMillis = System.currentTimeMillis();
		long l = currentTimeMillis/1000;
		int i = Integer.parseInt(l + "");
		return i;
	}
	/**
	 * 将字符串时间转换为秒值
	 * @return
	 */
	public static long formateConverLong(String fmt){

		try {
			Date parse = sdf.parse(fmt);
			long l = parse.getTime()/1000;
			return  l;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  0;
	}

	/**
	 * 将时间  yyyy-MM-dd HH:mm:ss 转为Date
	 * @param time
	 * @return
	 */
	public static Date formatyyyyMMddHHmmss(String time){
		try {
			Date parse = sdf.parse(time);
			return  parse;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  null;
	}

	/**
	 * 将时间   毫秒值  转换为 yyyy-MM-dd HH:mm:ss
	 * @param time
	 * @return
	 */
	public static String formatTimeMillis(String time){
			String format = sdf.format(Long.parseLong(time));
			return format;
	}

	/**
	 * 获取当前时间 yyyy--mmdd
	 * @return
	 */
	public static String getCurrentyyyymmdd(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(Calendar.getInstance().getTime());
	}
	/**
	 * 获取当前时间  yyyy-MM-dd
	 * @return
	 */
	public static String getCurrentyyyyMMdd(){
		return sdf_date.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * 获取当前时间 yyyymmddHHmmss
	 * @return
	 */
	public static String getCurrentyyyymmddHHmmss(){
		return sdf_date_currentTime.format(Calendar.getInstance().getTime());
	}
	/**
	 * 获取当前时间 yyyymmddHHmmss
	 * @return
	 */
	public static String formatyyyyMMddHHmmss(Date date){
		return sdf.format(date);
	}

    public static Date parseDate(String dateStr, String fmt) throws ParseException {
		return new SimpleDateFormat(fmt).parse(dateStr);
    }
    public static String format(Date date,String fmt){
		return new SimpleDateFormat(fmt).format(date);
	}
}
