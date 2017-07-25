package com.gt.mall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeekDayUtil {
	/**
	 * 输入一个日期的时间段，以及相应的星期数，获得这些星期的日期
	 */
	private static Map<Integer,Integer> weekNumberMap = new HashMap<Integer,Integer>();
	static {
		weekNumberMap.put(0, 1);
		weekNumberMap.put(1, 2);
		weekNumberMap.put(2, 3);
		weekNumberMap.put(3, 4);
		weekNumberMap.put(4, 5);
		weekNumberMap.put(5, 6);
		weekNumberMap.put(6, 7);
	}

	public static List<String> getDates(String dateFrom, String dateEnd, List<Integer> weekDays) {
		long time;
		long perDayMilSec = 24L * 60 * 60 * 1000;
		List<String> dateList = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 需要查询的星期系数
		String strWeekNumber = weekForNum(weekDays);
		System.out.println(strWeekNumber);
		try {
			dateFrom = sdf.format(sdf.parse(dateFrom).getTime() - perDayMilSec);
			while (true) {
				time = sdf.parse(dateFrom).getTime();
				time = time + perDayMilSec;
				Date date = new Date(time);
				dateFrom = sdf.format(date);
				if (dateFrom.compareTo(dateEnd) <= 0) {
					// 查询的某一时间的星期系数
					Integer weekDay = dayForWeek(date);
					// 判断当期日期的星期系数是否是需要查询的
					if (strWeekNumber.contains(weekDay.toString())) {
						dateList.add(dateFrom);
					}
				} else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateList;
	}

	// 等到当期时间的周系数。星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7
	public static Integer dayForWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 得到对应星期的系数 0：1，星期
	 */
	public static String weekForNum(List<Integer> weekDays) {
		// 返回结果为组合的星期系数
		String weekNumber = "";
		for (Integer weekDay : weekDays) {
			if(weekDay == 7){
				weekDay = 0;
			}
			weekNumber = weekNumber + "" + getWeekNum(weekDay).toString();
		}
		return weekNumber;

	}

	// 将星期转换为对应的系数 0,星期日; 1,星期一; 2....
	public static Integer getWeekNum(int strWeek) {
		return weekNumberMap.get(strWeek);
	}
	

	public static void main(String[] args) throws ParseException {
		// 输出从2015-01-01到2015-01-21之间的所有星期一和星期二的日期。
		List<Integer> daysOfOneWeek = new ArrayList<Integer>();
		daysOfOneWeek.add(7);
		List<String> daysNeedBookList = getDates("2016-07-21", "2016-08-21",
				daysOfOneWeek);
		for (String s : daysNeedBookList) {
			System.out.println(s);
		}
	}
}
