package com.liaoyin.lyproject.common;
import java.text.SimpleDateFormat;
import java.util.*;
import com.liaoyin.lyproject.common.util.*;
import org.apache.commons.codec.binary.Base64;
import com.alibaba.fastjson.JSONArray;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 创 建 人 ：
 * 创建日期：2018年1月2日
 * 描       述：公共方法类
 */
public class Common {


	/**
	 * 
	 * 创 建 人 ：
	 * 创建日期：2018年1月2日
	 * 描       述：二进制数据编码为BASE64字符串
	 * @param bytes
	 * @return
	 */
	public static String encode(final byte[] bytes) {
		return new String(Base64.encodeBase64(bytes));
	}

	/**
	 * 
	 * 创 建 人 ：
	 * 创建日期：2018年1月2日
	 * 描       述：密码MD5加密
	 * @param password
	 * @return
	 */
	public static String personEncrypt(String password) {
		if (isNull(password))  return null;
		String temp = EncryptUtils.encodeMD5String(EncryptUtils.encodeMD5String(password).concat(EncryptUtils.encodeMD5String(password)
				.concat(EncryptUtils.encodeMD5String(password))));

		return EncryptUtils
				.encodeMD5String(EncryptUtils.encodeMD5String(temp
						.substring(
								0,
								temp.length() > 6 ? (temp.length() - 6) : temp
										.length()).concat(
								temp.substring(0, temp.length() / 2))));
	}


	public static String personEncrypt2(String password) {
		if (isNull(password))  return null;
		String temp = EncryptUtils.encodeMD5String(password.concat(password
				.concat(password)));

		return EncryptUtils
				.encodeMD5String(EncryptUtils.encodeMD5String(temp
						.substring(
								0,
								temp.length() > 6 ? (temp.length() - 6) : temp
										.length()).concat(
								temp.substring(0, temp.length() / 2))));
	}

	public static String personEncrypt3(String password) {
		if (isNull(password))  return null;
		String temp = EncryptUtils.encodeMD5String(password.concat(password)
				.concat(EncryptUtils.encodeMD5String(password)));
		return EncryptUtils
				.encodeMD5String(EncryptUtils.encodeMD5String(temp
						.substring(
								0,
								temp.length() > 6 ? (temp.length() - 6) : temp
										.length()).concat(
								temp.substring(0, temp.length() / 2))));
	}

	
	/**
	 * 
	 * 创 建 人 ：
	 * 创建日期：2018年1月29日
	 * 描       述：string转List<Map>
	 * @param str
	 * @return
	 */
	public static List<Map> stringToListMap(String str){
		List<Map> list = JSONArray.parseArray(str,Map.class);
		return list;
	}
	
	/**
	 * 
	 * 创 建 人 ：
	 * 创建日期：2018年1月2日
	 * 描       述：精确到毫秒数产生时间数
	 * @return
	 */
	public synchronized static String numberOnlyRoomId() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Calendar calendar = Calendar.getInstance();
		String str = format.format(calendar.getTime());
		return str;
	}

	/**
	 * 
	 * 创 建 人 ：
	 * 创建日期：2018年1月2日
	 * 描       述： 年月日时间数
	 * @return
	 */
	public static String numberDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String str = format.format(calendar.getTime());
		return str;
	}

	/**
	 * 
	 * 创 建 人 ：
	 * 创建日期：2018年1月2日
	 * 描       述：判定是否为空
	 * @param o
	 * @return
	 */
	public static boolean isNull(Object o) {
		if (o == null || o.equals("") || o == "" || o == "null"
				|| o.equals("null") || o.equals("undefined")) {
			return true;
		}
		if (o != null) {
			if (o.toString().replaceAll("\\s*", "").length() != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * 创 建 人 ：
	 * 创建日期：2018年1月2日
	 * 描       述：判定是否相等
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean isEqual(Object o1, Object o2) {
		if (!Common.isNull(o1) && !Common.isNull(o2)) {
			if (o1 == o2 || o1.equals(o2)) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * 
	 * 创 建 人 ：
	 * 创建日期：2018年1月2日
	 * 描       述：判断一个元素是否存在数组中
	 * @param arr
	 * @param targetValue
	 * @return
	 */
	public static boolean isExist(String[] arr, String targetValue) {
		return Arrays.asList(arr).contains(targetValue);
	}


	/**
	 * 作者：
	 * 时间： 2018/9/25 15:08
	 * 描述： 判断字符串是否包含数字
	 **/
	public static boolean isContainNumber(String company) {

		Pattern p = Pattern.compile("[0-9]");
		Matcher m = p.matcher(company);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 作者：
	 * 时间： 2018/9/25 15:12
	 * 描述： 判断字符串是否包含英文
	 **/
	public static boolean isContainLetter(String company) {
		String regex = ".*[a-zA-z].*";
		return company.matches(regex);
	}

	/**
	 * 
	 * 创 建 人 ：
	 * 创建日期：2018年1月2日
	 * 描       述：随机数，数量自定义
	 * @param o
	 * @return
	 */
	public static Object randomNumber(Integer o) {
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < o; i++) {
			buffer.append(random.nextInt(9) % (9) + 1);
		}
		return buffer;
	}
	


	/**
	 *
	 * 创 建 人 ：
	 * 创建日期：2018年4月11日
	 * 描       述：生成token
	 * @param appkey
	 * @return
	 */
	public synchronized static final String getToken(String appkey){
		return AESUtils.encrypt(appkey + numberOnlyRoomId()
				+ randomNumber(3)).replace("_", "");
	}


	/**
	 * 作者：
	 * 时间： 2018/11/23 9:44
	 * 描述： 请求地址组装
	 **/
	public static final String requestUrlAssemble(String path){
		path = path.replaceAll("//","/");
		if (!path.startsWith("/")){
			path = "/"+path;
		}
		if (path.endsWith("/")){
			path = path.substring(0,path.length()-1);
		}
		return path;
	}
}
