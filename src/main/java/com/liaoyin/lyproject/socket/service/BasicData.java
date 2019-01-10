package com.liaoyin.lyproject.socket.service;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * 基本数据转换工具
 */
public class BasicData {

	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();
	}

	public static char[] getChars(byte[] bytes) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);

		return cb.array();
	}


	public static short bytesToShort(byte[] data, int offset) {
		return (short) (((data[offset + 1] << 8) | data[offset + 0] & 0xff));
	}

	public static void putLong(byte[] bb, long x, int index) {
		bb[index + 7] = (byte) (x >> 56);
		bb[index + 6] = (byte) (x >> 48);
		bb[index + 5] = (byte) (x >> 40);
		bb[index + 4] = (byte) (x >> 32);
		bb[index + 3] = (byte) (x >> 24);
		bb[index + 2] = (byte) (x >> 16);
		bb[index + 1] = (byte) (x >> 8);
		bb[index + 0] = (byte) (x >> 0);
	}

	public static void putInt(byte[] bb, int x, int index) {
		bb[index + 3] = (byte) (x >> 24);
		bb[index + 2] = (byte) (x >> 16);
		bb[index + 1] = (byte) (x >> 8);
		bb[index + 0] = (byte) (x >> 0);
	}

	public static void putShort(byte b[], short s, int index) {
		b[index + 1] = (byte) (s >> 8);
		b[index + 0] = (byte) (s >> 0);
	}

	public static long getLong(byte[] bb, int index) {
		return ((((long) bb[index + 7] & 0xff) << 56)
				| (((long) bb[index + 6] & 0xff) << 48)
				| (((long) bb[index + 5] & 0xff) << 40)
				| (((long) bb[index + 4] & 0xff) << 32)
				| (((long) bb[index + 3] & 0xff) << 24)
				| (((long) bb[index + 2] & 0xff) << 16)
				| (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] & 0xff) << 0));
	}

	// ++

	public static byte[] longToByte(long number) {
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	// 结束
	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。 和bytesToInt（）配套使用
	 * 
	 * @param value要转换的int值
	 * @return byte数组
	 */
	public static byte[] int2byteArray(int num) {
		byte[] result = new byte[4];
		result[3] = (byte) (num >>> 24);
		result[2] = (byte) (num >>> 16);
		result[1] = (byte) (num >>> 8);
		result[0] = (byte) (num);
		return result;
	}

	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt2（）配套使用
	 */
	public static byte[] intToBytes2(int value) {
		byte[] src = new byte[4];
		src[0] = (byte) ((value >> 24) & 0xFF);
		src[1] = (byte) ((value >> 16) & 0xFF);
		src[2] = (byte) ((value >> 8) & 0xFF);
		src[3] = (byte) (value & 0xFF);
		return src;
	}

	public static byte[] remove_N_Bytes(byte[] data, int n) {
		byte[] temp = null;
		if (n < data.length) {
			temp = new byte[data.length - n];
			for (int i = n; i < data.length; i++) {
				temp[i - n] = data[i];
			}
		}
		return temp.clone();
	}

	public static byte[] remove_N_Behind_Bytes(byte[] data, int n) {
		byte[] temp = null;
		if (n < data.length) {
			temp = new byte[data.length - n];
			for (int i = 0; i < data.length - n; i++) {
				temp[i] = data[i];
			}
		}
		return temp;
	}

	public static byte[] get_N_Bytes(byte[] data, int n) {
		byte[] temp = new byte[n];
		if (n < data.length) {
			for (int i = 0; i < n; i++) {
				temp[i] = data[i];
			}
		}
		return temp.clone();
	}

	public static byte[] shortToBytes(short s) {
		byte b[] = new byte[2];
		b[0] = (byte) (s >> 0);
		b[1] = (byte) (s >> 8);
		return b.clone();
	}

	public static String Bytes2HexString(List<Byte> b) {
		String ret = "[";
		for (int i = 0; i < b.size(); i++) {
			String hex = Integer.toHexString(b.get(i).byteValue() & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}

			ret += "0x" + hex.toUpperCase() + ",";
		}
		ret += "]";
		return ret;
	}

	public static String Bytes2HexString(byte[] b) {
		String ret = "[";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}

			ret += "0x" + hex.toUpperCase() + ",";
		}
		ret += "]";
		return ret;
	}

	/**
	 * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
	 * 
	 * @param src
	 *            byte数组
	 * @param offset
	 *            从数组的第offset位开始
	 * @return int数值 //
	 */
	// public static int bytesToInt(byte[] src, int offset) {
	// int value;
	// value = (int) ((src[offset] & 0xFF)
	// | ((src[offset+1] & 0xFF)<<8)
	// | ((src[offset+2] & 0xFF)<<16)
	// | ((src[offset+3] & 0xFF)<<24));
	// return value;
	// }

	/**
	 * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
	 */
	public static int bytesToInt2(byte[] src, int offset) {
		int value;
		value = (((src[offset] & 0xFF) << 24)
				| ((src[offset + 1] & 0xFF) << 16)
				| ((src[offset + 2] & 0xFF) << 8) | (src[offset + 3] & 0xFF));
		return value;
	}

	public static int bytesToInt(byte[] data, int offset) {
		int num = 0;
		num = (int) (((data[offset + 3] & 0xff) << 24)
				| ((data[offset + 2] & 0xff) << 16)
				| ((data[offset + 1] & 0xff) << 8) 
				| (((long) data[offset + 0] & 0xff) << 0));
		return num;
	}
	/**
	 * @param buf
	 * @param asc true 低位在前  false 高位在前
	 * @return
	 */
	public final static short bytesToShort2(byte[] buf, boolean asc) 
	{
	    if (buf == null) 
	    {
	      throw new IllegalArgumentException("byte array is null!");
	    }
	    if (buf.length > 2) 
	    {
	      throw new IllegalArgumentException("byte array size > 2 !");
	    }
	    short r = 0;
	    if (asc)
	      for (int i = buf.length - 1; i >= 0; i--) 
	        {
	        r <<= 8;
	        r |= (buf[i] & 0x00ff);
	      }
	    else
	      for (int i = 0; i < buf.length; i++) 
	        {
	        r <<= 8;
	        r |= (buf[i] & 0x00ff);
	      }
	    return r;
	}
	
	/**
	 * CRC取高位在前
	 * */
	public static byte[] intTo2Byte(int crc) {
		byte[] crc16 = BasicData.intToBytes2(crc);// 低位在前，取后两位
		byte[] crcs = new byte[2];
		crcs[0] = crc16[2];
		crcs[1] = crc16[3];
		return crcs;
	}

	/**
	 * 
	 * @param data1
	 * @param data2
	 * @return data1 与 data2拼接的结果
	 */
//	public static byte[] addBytes(byte[] data1, byte[] data2) {
//		byte[] data3 = new byte[data1.length + data2.length];
//		System.arraycopy(data1, 0, data3, 0, data1.length);
//		System.arraycopy(data2, 0, data3, data1.length, data2.length);
//		return data3;
//	}
	/**
	 * 字节组成
	 * @param data 字节数组
	 * @return 
	 */
	public static byte[] bytesForm(byte[]... data) {
		if(data == null){
			return new byte[0];
		}
		if(data.length == 1){
			return data[0];
		}
		byte[] newbata = new byte[0];
		byte[] bs = null;
		int len = 0;
		for (int i = 0; i < data.length; i++) {
			len = newbata.length;
			bs = new byte[newbata.length + data[i].length];
			System.arraycopy(data[i], 0, bs, len, data[i].length);
			System.arraycopy(newbata, 0, bs, 0, newbata.length);
			newbata = bs;
		}
		return newbata;
	}
	/**
	 * 字节截取
	 * @param data 目标
	 * @param s 起始位置
	 * @param e 结束位置 0=data.len
	 * @return
	 */
	public static byte[] bytesSplit(byte[] data, int s, int e) {
		if(data == null){
			return new byte[0];
		}
		if(e == 0){
			e = data.length;
		}
		if(s > e){
			return new byte[0];
		}
		if(data.length < e){
			return new byte[0];
		}
		
		byte[] newbata = new byte[e - s];
		int len = 0;
		for (int i = s; i < data.length && i < e; i++) {
			newbata[len] = data[i];
			len ++;
		}
		return newbata;
	}

	/**
	 * 
	 * 将RTU地址转换成集中器编号
	 */
	public static String bytesToString(byte[] rtuAddr) {

		String rtu = BasicData.Bytes2HexString(rtuAddr);
		// 去掉中括号
		String rtus = rtu.substring(1, rtu.length() - 2);
		String[] s = rtus.split(",");
		String no = "";
		for (int i = 0; i < s.length; i++) {
			no += s[i].substring(2, 4);
		}
		return no;
	}

	/**
	 * BCD字符串转byte数组
	 * */
	public static byte[] string2Byte(String str) {
		str = str.toUpperCase();
		int length = str.length() / 2;
		char[] hexChars = str.toCharArray();
		byte[] f = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			f[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		byte[] fd = Arrays.copyOfRange(f, 0, f.length);
		return fd;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 数据长度转byte数组，高位在前
	 * */
	public static byte[] lenToByteArray(int num) {
		byte[] result = new byte[4];
		result[3] = (byte) (num >>> 24);
		result[2] = (byte) (num >>> 16);
		result[1] = (byte) (num >>> 8);
		result[0] = (byte) (num);
		byte[] len = new byte[2];
		len[0] = result[0];
		len[1] = result[1];
		return len;
	}

	/**
	 * @功能: String转为BCD码
	 */
	public static byte[] strToBcd(String asc) {
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	/**
	 * double转BCD码
	 * */
	public static byte[] doubleToBCD(double num) {
		String str = Double.toString(num);
		byte[] bcd = strToBcd(str);
		byte[] result = Arrays.copyOfRange(bcd, 0, 2);
		return result;
	}
	/** 
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit 
     */  
	public static byte[] byte2bit(byte b) {  
        byte[] array = new byte[8];  
        for (int i = 7; i >= 0; i--) {  
            array[i] = (byte)(b & 1);  
            b = (byte) (b >> 1);  
        }  
        return array;  
    }  
	/** 
     * 取byte后两位判断阀门状态
     */  
	public static boolean getValveState(byte b){
		byte[] arr = byte2bit(b);
		if(arr[6] == 0 && arr[7] == 0){
			return true;
		}
		return false;
	}
	/**
	 * 版本转换
	 * */
	public static String insertStr(byte[] str){
		String ss = bytesToString(str);
		String s1 = ss.substring(0, 2);
		String s2 = ss.substring(2,ss.length());
		return s1+"."+s2;
	}
	public static long bytesToLong(byte[] b) {
		 return ((((long) b[ 0] & 0xff) << 56) 
	               | (((long) b[ 1] & 0xff) << 48) 
	               | (((long) b[ 2] & 0xff) << 40) 
	               | (((long) b[ 3] & 0xff) << 32) 
	               | (((long) b[ 4] & 0xff) << 24) 
	               | (((long) b[ 5] & 0xff) << 16) 
	               | (((long) b[ 6] & 0xff) << 8) | 
	               (((long) b[ 7] & 0xff) << 0)); 
	}
	public static long bytesToLong2(byte[] b) {
		 return (
				(((long) b[ 7] & 0xff) << 0)  	| 
				(((long) b[ 6] & 0xff) << 8) 	|
				(((long) b[ 5] & 0xff) << 16) 	|
				(((long) b[ 4] & 0xff) << 24)	| 
				(((long) b[ 3] & 0xff) << 32) 	| 
				(((long) b[ 2] & 0xff) << 40) 	| 
				(((long) b[ 1] & 0xff) << 48) 	| 
                (((long) b[ 0] & 0xff) << 64)); 
	}
	/** *//** 
	    * @函数功能: BCD码转为10进制串(阿拉伯数据) 
	    * @输入参数: BCD码 
	    * @输出结果: 10进制串 
	    */  
	public static String bcd2Str(byte[] bytes){  
	    StringBuffer temp=new StringBuffer(bytes.length*2);  
	   
	    for(int i=0;i<bytes.length;i++){  
	     temp.append((byte)((bytes[i]& 0xf0)>>>4));  
	     temp.append((byte)(bytes[i]& 0x0f));  
	    }  
	    return temp.toString().substring(0,1).equalsIgnoreCase("0")?temp.toString().substring(1):temp.toString();  
	}
	/**
	 * 将数组转为16进制字符串
	 * @param cmd
	 */
	public static String bytesToHex(byte[] cmd){
		StringBuilder sb = new StringBuilder();
		String tmp = null;
		for (byte b : cmd){
			// 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
			tmp = Integer.toHexString(0xFF & b);
			// 每个字节8为，转为16进制标志，2个16进制位
			if (tmp.length() == 1)  {
				tmp = "0" + tmp;
			}
			sb.append(tmp);
		}
        return sb.toString().toUpperCase();
	}
	/** 
	* * 两个Double数相减 * 
	*  
	* @param v1 * 
	* @param v2 * 
	* @return Double 
	*/  
	public static Double sub(Double v1, Double v2) {  
	   BigDecimal b1 = new BigDecimal(v1.toString());  
	   BigDecimal b2 = new BigDecimal(v2.toString());  
	   return new Double(b1.subtract(b2).doubleValue());  
	}  
	/** 
	* * 两个Double数相乘 * 
	*  
	* @param v1 * 
	* @param v2 * 
	* @return Double 
	*/  
	public static Double mul(Double v1, Double v2) {  
	   BigDecimal b1 = new BigDecimal(v1.toString());  
	   BigDecimal b2 = new BigDecimal(v2.toString());  
	   return new Double(b1.multiply(b2).doubleValue());  
	}
	/**
	 * 保留小数后几位（四舍五入）	PS:保留位后面为零则不做显示
	 * @param d
	 * @param position
	 * @return
	 */
	public static double doubleRetain(double d,int position){
		BigDecimal b = new BigDecimal(d);    
		return b.setScale(position, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 8bit转byte
	 * @param string
	 * */
	public static byte bitToByte(String bit) {  
        int re, len;  
        if (null == bit) {  
            return 0;  
        }  
        len = bit.length();  
        if (len != 4 && len != 8) {  
            return 0;  
        }  
        if (len == 8) {// 8 bit处理  
            if (bit.charAt(0) == '0') {// 正数  
                re = Integer.parseInt(bit, 2);  
            } else {// 负数  
                re = Integer.parseInt(bit, 2) - 256;  
            }  
        } else {//4 bit处理  
            re = Integer.parseInt(bit, 2);  
        }  
        return (byte) re;  
    }
	/** 
	 * 二进制字符串转byte 
	 */  
	public static byte decodeBinaryString(String byteStr) {  
	    int re, len;  
	    if (null == byteStr) {  
	        return 0;  
	    }  
	    len = byteStr.length();
	    if(len > 8){
	    	return 0;
	    }
	    for (int i = len; i < 8; i++) {
	    	byteStr = "0" +byteStr;
		}
	    if (len == 8) {// 8 bit处理  
	        if (byteStr.charAt(0) == '0') {// 正数  
	            re = Integer.parseInt(byteStr, 2);  
	        } else {// 负数  
	            re = Integer.parseInt(byteStr, 2) - 256;  
	        }  
	    } else {// 4 bit处理  
	        re = Integer.parseInt(byteStr, 2);  
	    }  
	    return (byte) re;  
	}
    /** 
     * 把byte转为字符串的bit 
     */  
    public static String byteToBit(byte b) {  
        return ""  
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)  
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)  
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)  
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);  
    }
    
    /**
     * 高低位转换
	 * @param buf
	 * @param asc true 低位在前  false 高位在前
	 * @return
	 */
	public final static byte[] bytesTobytes(byte[] buf, boolean asc) 
	{
	    if (buf == null) 
	    {
	      throw new IllegalArgumentException("byte array is null!");
	    }
	    if (buf.length < 1) 
	    {
	      throw new IllegalArgumentException("byte array size < 1 !");
	    }
	    byte[] nw = new byte[buf.length];
	    if(asc){
			for (int i = 0; i < buf.length; i++) {
				nw[i] =  (byte) (buf[i] & 0xFF);
			}
	    }else{
	    	//TODO 有误
	    	int c = 0;
			for (int i = buf.length-1; i >= 0; i--) {
				nw[c++] =  (byte) (buf[i] & 0xFF);
			}
	    }
	    return nw;
	}
	 /**
     * Ascii码转字符串
	 * @param buf
	 * @return
	 */
	public final static String asciiToString(byte[] buf)  {
		StringBuffer tStringBuf=new StringBuffer ();
		for(int i=0;i < buf.length;i++){
			tStringBuf.append((char)buf[i]);
		}
		return tStringBuf.toString();
	}
	/**
	 * 字符串转Ascii码
	 * @param str
	 * @return
	 */
	public final static byte[] stringToAscii(String str)  {
		char[] c = str.toCharArray();
		byte[] b = new byte[c.length];
		for(int i = 0; i < c.length; i++){
			b[i] = (byte) c[i];
		}
		return b;
	}
	/**
	 * 将byte转成8个bit
	 * */
	public static byte[] getBooleanArray(byte b) {  
        byte[] array = new byte[8];  
        for (int i = 7; i >= 0; i--) {  
            array[i] = (byte)(b & 1);  
            b = (byte) (b >> 1);  
        }  
        return array;  
    }
}
