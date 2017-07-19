package com.gt.mall.util;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/7/18 0018
 * Time : 16:22
 */
public class CommonUtil {

    /**
     * 判断对象是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
	boolean b = false;
	try {
	    if (obj == null || "".equals(obj)) {
		b = true;
	    } else {
		b = false;
	    }
	} catch (Exception e) {
	    b = false;
	    e.printStackTrace();
	}
	return b;
    }

    /**
     * 判断对象是否不为空
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
	boolean b = false;
	try {
	    if (obj == null || "".equals(obj)) {
		b = false;
	    } else {
		b = true;
	    }
	} catch (Exception e) {
	    b = false;
	    e.printStackTrace();
	}
	return b;
    }

    /**
     * 转Integer
     *
     * @param obj
     */
    public static Integer toInteger(Object obj) {
	try {
	    if (!isEmpty(obj)) {
		return Integer.parseInt(obj.toString());
	    } else {
		throw new Exception("对象为空，转换失败！");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 转String
     *
     * @param obj
     */
    public static String toString(Object obj) {
	try {
	    if (!isEmpty(obj)) {
		return obj.toString();
	    } else {
		throw new Exception("对象为空，转换失败！");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 转Double
     *
     * @param obj
     */
    public static Double toDouble(Object obj) {
	try {
	    if (!isEmpty(obj)) {
		return Double.parseDouble(obj.toString());
	    } else {
		throw new Exception("对象为空，转换失败！");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 校验是否是double数据
     *
     */
    public static boolean isDouble(Object obj) {
	try {
	    Double.parseDouble(obj.toString());
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }
}
