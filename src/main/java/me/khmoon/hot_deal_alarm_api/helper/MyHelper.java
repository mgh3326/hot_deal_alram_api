package me.khmoon.hot_deal_alarm_api.helper;

public class MyHelper {
  public static boolean isNumeric(String str) {
    try {
      Double.parseDouble(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
