// package com.visionvera.remoteservice.common.redis;
//
// import javax.servlet.http.Cookie;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
//
// /**
//  * ClassName: CookieUtils
//  *
//  * @author quboka
//  * @Description: Cookie操作类
//  * @date 2018年4月8日
//  */
// public class CookieUtils {
//
//   /**
//    * 删除cookie
//    *
//    * @param response
//    * @param path
//    * @param cookiename
//    * @return
//    * @author chenting
//    * @date 2017年6月13日
//    */
//   public static void deleteCookie(HttpServletResponse response, String path, String cookiename) {
//     Cookie cookie = new Cookie(cookiename, null);
//     cookie.setMaxAge(0);
//     cookie.setPath(path);
//     response.addCookie(cookie);
//   }
//
//   /**
//    * 获得cookie
//    *
//    * @param request
//    * @param name
//    * @return
//    */
//   public static String getCookie(HttpServletRequest request, String name) {
//     Cookie cookies[] = request.getCookies();
//     if (cookies == null || name == null || name.length() == 0) {
//       return null;
//     }
//     for (int i = 0; i < cookies.length; i++) {
//       if (cookies[i].getName().equals(name)) {
//         return cookies[i].getValue();
//       }
//     }
//     return null;
//   }
//
//   /**
//    * 保存cookie
//    *
//    * @param response
//    * @param name
//    * @param value
//    * @param saveTime
//    */
//   public static void saveCookie(HttpServletResponse response, String name, String value,
//       int saveTime) {
//     if (value == null) {
//       value = "";
//     }
//     Cookie cookie = new Cookie(name, value);
//     cookie.setMaxAge(saveTime);
//     cookie.setPath("/");
//     response.addCookie(cookie);
//   }
//
//   /**
//    * 保存cookie
//    *
//    * @param response
//    * @param name key
//    * @param value 值
//    * @param path 路径
//    * @param saveTime 时间
//    */
//   public static void saveCookie(HttpServletResponse response, String name, String value,
//       String path, int saveTime) {
//     if (value == null) {
//       value = "";
//     }
//     Cookie cookie = new Cookie(name, value);
//     cookie.setMaxAge(saveTime);
//     cookie.setPath(path);
//     response.addCookie(cookie);
//   }
// }
