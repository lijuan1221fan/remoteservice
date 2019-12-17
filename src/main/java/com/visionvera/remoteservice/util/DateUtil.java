package com.visionvera.remoteservice.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ljfan
 * @ClassNameDateUtil 时间格式化
 * @date 2018/12/05 version
 */
public class DateUtil {

  public static Logger logger = LoggerFactory.getLogger(DateUtil.class);
  private static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
  //日期时分秒
  public static String getDateInMinutesAndSecondsZero(Date date) {
    String msZero = formatDate.format(date) + " 00:00:00";
    return msZero;
  }

  //日期时分秒
  public static String getDateInMinutesAndSecondsMax(Date date) {
    String msMax = formatDate.format(date) + " 23:59:59";
    return msMax;
  }
  //根据已知时间，取得时间点
  public static Integer getTime(String date){
    String tn = date.substring(11,13);
    Integer tm = Integer.valueOf(tn);
    return tm;

  }
  //当前年月日
  public static String getNowDate() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    String now = format.format(new Date());
    return now;
  }
  //根据日期区间判断当前日期是否满足
  public static boolean IsSupport(String datesStr,Date day){
   // private ReservationBean setIsSupport(ReservationBean bean,String datesStr,Date day){
      String dateFt = formatDate.format(day);
      int date = Integer.parseInt(dateFt.substring(8,10));
      String[] dateArr = datesStr.split(",");
      int[] dates = new int[2];
      dates[0] = Integer.valueOf(dateArr[0]);
      if(dateArr.length==1){
        dates[1] = Integer.valueOf(dateArr[0]);
      }else {
        dates[1] = Integer.valueOf(dateArr[1]);
      }
      int start = dates[0];
      int end = dates[1];
      if (date >= start && date <= end) {
        return true;
      }else{
       return false;
      }
  }

  /**
   * 根据当前日期所在接下来一周时间
   * @param date
   * @return
   */

  public static List<Date> getTimeInterval(Date date) {
    List lDate = new ArrayList();
    Calendar calBegin = Calendar.getInstance();
    // 使用给定的 Date 设置此 Calendar 的时间
    calBegin.setTime(date);
    Calendar calEnd = Calendar.getInstance();
    // 使用给定的 Date 设置此 Calendar 的时间
    calEnd.add(Calendar.DATE, 6);
    Date endDay = calEnd.getTime();
    // 测试此日期是否在指定日期之后
    while (endDay.after(calBegin.getTime())) {
      // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
      calBegin.add(Calendar.DAY_OF_MONTH, 1);
      lDate.add(calBegin.getTime());
    }
    return lDate;
  }
}
