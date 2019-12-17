// package com.visionvera.remoteservice.Intercepter;
//
// import com.visionvera.remoteservice.bean.CommonConfigBean;
// import com.visionvera.remoteservice.bean.UserBean;
// import com.visionvera.remoteservice.common.redis.CookieUtils;
// import com.visionvera.remoteservice.common.redis.RedisUserManage;
// import com.visionvera.remoteservice.dao.CommonConfigDao;
// import com.visionvera.remoteservice.util.StringUtil;
// import java.io.IOException;
// import java.util.Calendar;
// import java.util.HashMap;
// import java.util.Map;
// import javax.annotation.Resource;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.web.servlet.HandlerInterceptor;
// import org.springframework.web.servlet.ModelAndView;
//
//
// /**
//  * ClassName: LoginIntercepter
//  *
//  * @author wangrz
//  * @Description: 登录验证拦截器
//  * @date 2016年5月30日
//  */
// public class LoginIntercepter implements HandlerInterceptor {
//
//   private static Logger logger = LoggerFactory.getLogger(LoginIntercepter.class);
//
//   @Resource
//   private RedisUserManage redisUserManage;
//
//   @Resource
//   private CommonConfigDao commonConfigDao;
//
// //	@Resource
// //	private TitleDao titleDao ;
//
//   @Value("${maintenance.time}")
//   private boolean maintenanceTime;
//
//   @Override
//   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2,
//       Exception arg3) {
//   }
//
//   @Override
//   public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
//       ModelAndView arg3) {
//   }
//
//   private Calendar getCalendar(int hour, int minute) {
//     Calendar cal = Calendar.getInstance();
//     cal.set(Calendar.HOUR_OF_DAY, hour);
//     cal.set(Calendar.MINUTE, minute);
//     return cal;
//   }
//
//   @Override
//   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) {
//     Calendar startCalendar = getCalendar(0, 0);
//     Calendar endCalendar = getCalendar(2, 0);
//     Calendar instance = Calendar.getInstance();
//     String url = request.getRequestURL().toString();
//     if (maintenanceTime && startCalendar.before(instance) && endCalendar.after(instance) && !url
//         .contains("index")) {
//       String requestType = request.getHeader("X-Requested-With");
//       logger.info("系统维护时间禁止登陆");
//       // 普通请求
//       if (StringUtil.isNull(requestType)) {
//         try {
//           response.sendRedirect(request.getContextPath() + "/user/index");
//         } catch (IOException e) {
//           e.printStackTrace();
//         }
//       } else {// ajax请求
//         try {
//           StringUtil.sendResponse(response,
//               "{\"result\":false, \"logon_error\":true, \"msg\":\"系统维护中维护时间：00:00-02:00\"}",
//               "UTF-8");
//         } catch (IOException e) {
//           e.printStackTrace();
//         }
//       }
//       return false;
//     }
//     if (url.contains("index") || url.contains("login")
//         || url.contains("logout") || url.contains("getWaitingNumber")
//         || url.contains("getNumber") || url.contains("getBusinessType") || url.contains("test")) {
//       return true;
//     } else {
// //			HttpSession session = request.getSession();
// //            UserBean userInfo = (UserBean) session.getAttribute("user");
// //            //获取不到sessionID，表示用户未登录
// //            if(userInfo == null){
// //                this.sendRedirect(request, response);
// //                return false;
// //            }
// //            return true;
//
//       //是否拦截
//       if (!allowAccess(request)) {
//         try {
//           this.sendRedirect(request, response);
//         } catch (IOException e) {
//           e.printStackTrace();
//         }
//         return false;
//       }
//
//       return true;
//     }
//   }
//
//   /**
//    * 判断是否允许访问
//    *
//    * @param request
//    * @return boolean
//    */
//   public boolean allowAccess(HttpServletRequest request) {
//
//     String userKey = CookieUtils.getCookie(request, RedisUserManage.USER_COOKIENAME);
//
//     //cookie中获取不到用户key，表示用户未登录
//     if (StringUtil.isNull(userKey)) {
//       return false;
//     }
//
//     //获取redis中的用户信息
//     UserBean user = redisUserManage.getUserInfo(userKey);
//     //用户信息不存在，表示用户未登录，或登录失效
//     if (user == null) {
//       return false;
//     }
//
//     //session续时
//     //由于页面中头部需要显示用户名，及用户身份等信息，故session中需要存储数据，因此当session中的用户信息没有了后，需要重新往session中填入内容
//     HttpSession session = request.getSession();
//     UserBean userInfoFromSession = (UserBean) session.getAttribute("user");
//     if (userInfoFromSession == null ||
//         userInfoFromSession.getUserId().intValue() != user.getUserId().intValue()) {
//
//       session.removeAttribute("user");
//       session.setAttribute("user", user);
//
//       //查询版本号
//       CommonConfigBean commonConfig = commonConfigDao.getCommonConfigByName("version");
//       session.setAttribute("version", commonConfig.getCommonValue());
//       Map<String, String> title = new HashMap<>(3);
//       commonConfig = commonConfigDao.getCommonConfigByName("mainTitle");
//       title.put("mainTitle", commonConfig.getCommonValue());
//       commonConfig = commonConfigDao.getCommonConfigByName("publicSubhead");
//       title.put("publicSubhead", commonConfig.getCommonValue());
//       commonConfig = commonConfigDao.getCommonConfigByName("socialSubhead");
//       title.put("socialSubhead", commonConfig.getCommonValue());
//
//       //标题
// //			TitleBean title = titleDao.getTileById(CommonConstant.TITLEID) ;
//       session.setAttribute("title", title);
//     }
//
//     //redis的key重设过期时间
//     redisUserManage.setKeyExpiresTime(userKey, RedisUserManage.REDIS_MAXAGE);
//
//     logger.info("拦截器中重设" + user.getLoginName() + " 有效期为：" + RedisUserManage.REDIS_MAXAGE + "秒钟");
//
//     return true;
//   }
//
//   // 针对不同的请求方式，返回不同的处理请求
//   public void sendRedirect(HttpServletRequest request, HttpServletResponse response)
//       throws IOException {
//     // 判断请求方式，返回值为XMLHttpRequest表示为Ajax请求，为空，表示普通请求
//     String requestType = request.getHeader("X-Requested-With");
//     // 普通请求
//     if (StringUtil.isNull(requestType)) {
//       response.sendRedirect(request.getContextPath() + "/user/index");
//     } else {// ajax请求
//       StringUtil.sendResponse(response,
//           "{\"result\":false, \"logon_error\":true, \"msg\":\"用户未登录或者登录失效\"}", "UTF-8");
//     }
//   }
//
//
// }
