package com.pyj.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyUtility {

	   /**
     * Unicode数据流 转 字符串
     * 常常用来解析中文
     * len = 数据流长度，而不是字符串长度
     *
     */
    public static String utilFuncUnicodeByte2String(byte[] bBase, int offset, int len) {
        StringBuffer strBuffer = new StringBuffer(len / 2);
        for (int ii = 0; ii < len / 2; ii++) {
            int intValue = utilFuncByte2short(bBase, offset + ii * 2);
            if (intValue == 0) {
                break;
            }
            strBuffer.append((char) (intValue));
        }

        return strBuffer.toString();
    }
    
    /**
     * 从字节流解析一个short
     */
    public static short utilFuncByte2short(byte[] bBase, int offset) {
        try {
            short j = (short) (((bBase[offset + 1] & 0xff) << 8) +
                    (bBase[offset] & 0xff));
            if (j == 0x7fff) {
                j = 0;
            }
            return j;
        } catch (NullPointerException ex) {
            return -1;
        }
    }
    
    public static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm", java.util.Locale.CHINESE);

    
    public static SimpleDateFormat sdf1 = new java.text.SimpleDateFormat(
			"M月d日", java.util.Locale.CHINESE);
	/**
	 * 将long格式的日期值格式化成String
	 * 
	 * @param l
	 *            日期的毫秒值
	 * */
	public static String getFormateDate(long l) {

		Date date = new Date();
		date.setTime(l);
		return sdf1.format(date);
	}
	 public static SimpleDateFormat sdf2 = new java.text.SimpleDateFormat(
				"H:mm开始", java.util.Locale.CHINESE);
		/**
		 * 根据用默认的日期格式对时间戳进行格式化
		 * @return 格式后的日期 hh:mm开始
		 * */
	public static String getStartTime(long time) {

		Date date = new Date();
		date.setTime(time);
		return sdf2.format(date);
	}
	
	/**
	 * 根据指定的日期格式对时间戳进行格式化
	 * @return 格式后的日期
	 * */
	public static String getStartTime(long time , SimpleDateFormat sf)
	{
		Date date = new Date();
		date.setTime(time);
		return sf.format(date);
	}
	
	/**
	 * @return 指定日0时0分的million值
	 * */
	public static long getZeroTime(long d) {
		Calendar cc = Calendar.getInstance();
		cc.setTimeInMillis(d*1000);
		Calendar ca = new GregorianCalendar(cc.get(Calendar.YEAR),
				cc.get(Calendar.MONTH), cc.get(Calendar.DAY_OF_MONTH));
//		System.out.println("zero time="+ca.getTimeInMillis());
		return ca.getTimeInMillis()/1000;
	}
	
	public static SimpleDateFormat sdf3 = new java.text.SimpleDateFormat(
			"yyyyMMdd", java.util.Locale.CHINESE);
	public static String getDate(long d)
	{
		Date date = new Date();
		date.setTime(d);
		return sdf3.format(date);
	}
}
