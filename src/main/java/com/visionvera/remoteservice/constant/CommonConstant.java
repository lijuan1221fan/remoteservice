package com.visionvera.remoteservice.constant;

import com.visionvera.remoteservice.bean.Print;
import com.visionvera.remoteservice.bean.SysUserBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: CommonConstant
 *this evaluation copy of secureCrt has expired
 * for information on registering ,see ORDER.txt or
 * @author quboka
 * @Description: 公共常量
 * @date 2018年3月23日
 */
public class CommonConstant {

  public static final String zeroStr = "0";
  /**
   * 通用常量值
   */
  public static final int zero = 0;
  public static final int one = 1;
  public static final int two = 2;
  public static final int three = 3;
  public static final int four = 4;
  /**
   * 业务类型设定01一窗多办，02 分部门处理 ，03 分机取号
   */
  public static final String allProcess = "01";
  public static final String singleProcess= "02";
  public static final String showDeptList= "03";
  /**
   * 1：删除归属业务
   */
  public static final int businessOwn = 1;
  /**
   * 0：移到其他类别中
   */
  public static final int businessMove = 0;

  /**
   * @Fields RECEIPT_PRINTER : 签名位置
   */
  public static final int SIGN_LOCATION = 1;
  /**
   * @Fields RECEIPT_PRINTER : 盖章位置
   */
  public static final int STAMP_LOCATION = 2;
  /**
   * @Fields RECEIPT_PRINTER : 申请单打印机
   */
  public static final Integer RECEIPT_PRINTER = 2;
  /**
   * @Fields RECEIPT_PRINTER : 常用打印机
   */
  public static final Integer COMMON_PRINTER = 1;
  /**
   * @Fields WORK_STATE_LEISURE : 用户的工作状态   ： 0：空闲中
   */
  public static final Integer WORK_STATE_LEISURE = 0;
  /**
   * @Fields SYSTEM_PREFIX : 系统前缀
   */
  public static final String SYSTEM_PREFIX = "RS_";
  /***
   * @Fields meeting_base_url 会管服务地址
   */
  public static final String MEETING_BASE_URL = "/cmsweb/restful/";
  /**
   * @Fields WORK_STATE_WAIT :  用户的工作状态   ：1：等待中
   */
  public static final Integer WORK_STATE_WAIT = 1;
  /***
   * @Fields cms_base_url 存储网关服务地址
   */
  public static final String CMS_BASE_URL = "/cms/api/remoteHandleApi/";
  /***
   * @Fields cms_session_id 存储网关sessionID
   */
  public static final String CMS_SESSION_ID = "268785df-bd29-49ef-b991-4ae38209c6b9";
  /**
   * @Fields WORK_STATE_DISPOSE : 用户的工作状态   ：2：处理中 未拉会
   */
  public static final Integer WORK_STATE_DISPOSE = 2;
    /**
     * @Fields WORK_STATE_DISPOSE : 用户的工作状态   ：3：处理中 已拉会
     */
    public static final Integer WORK_STATE_DISPOSE_STARTMEET = 3;
  /***
   * @Fields cms_read_card 存储网关读取身份证地址
   */
  public static final String CMS_READ_CARD_URL = "scanIDCard.do";
  /***
   * @Fields cms_read_card 存储网关读取视频地址及图片列表
   */
  public static final String CMS_READ_VIDEO_URL = "getVideoInfo.do";
  /***
   * @Fields cms_read_video_snapshots 存储网关读取录像截图
   */
  public static final String CMS_READ_VIDEO_SNAPSHOTS_URL = "videoSnapshots.do";
  /***
   * @Fields cms_read_video_snapshots 存储网关删除录像截图
   */
  public static final String CMS_DELETE_VIDEO_SNAPSHOTS_URL = "deleteVideoInfo.do";
  /**
   * @Fields CMS_SIGNED_PHOTO_URL : 存储网关获取数字签名照片接口
   */
  public static final String CMS_SIGNED_PHOTO_URL = "signedPhoto.do";
  /**
   * @Fields CMS_SIGNED_PHOTO_LIST_URL : 存储网关获取数字签名照片列表接口(无分页)
   */
  public static final String CMS_SIGNED_PHOTO_LIST_URL = "getSignedPhotoList.do";
  /**
   * @Fields CMS_DELETE_SIGNED_PHOTO_URL : 存储网关删除签名照片
   */
  public static final String CMS_DELETE_SIGNED_PHOTO_URL = "deleteSignedPhotoes.do";
  /**
   * @Fields CMS_SCAN_FINGERPRINT_URL : 采集指纹照片接口
   */
  public static final String CMS_SCAN_FINGERPRINT_URL = "scanFingerprint.do";
  /**
   * @Fields CMS_DELETE_SCAN_FINGERPRINT_URL : 删除指纹照片接口
   */
  public static final String CMS_DELETE_SCAN_FINGERPRINT_URL = "deleteFingerprintPhotos.do";
  /**
   * @Fields CMS_HIGH_SPEED_PHOTOGRAPHIC_URL : 高拍仪采集资料接口
   */
  public static final String CMS_HIGH_SPEED_PHOTOGRAPHIC_URL = "highSpeedPhotographic.do";
  /**
   * @Fields CMS_DELETE_HIGH_SPEED_PHOTOGRAPHIC_URL : 删除高拍仪采集资料
   */
  public static final String CMS_DELETE_HIGH_SPEED_PHOTOGRAPHIC_URL = "deleteHighSpeedPhotographic.do";
  /**
   * @Fields TITLEID : 标题id
   */
  public static final Integer TITLEID = 1;
  /**
   * @Fields ServiceKey : 演示村的唯一标识
   */
  public static final String SERVICEKEY = "RS_5597b6e0240c4285983f4bbf15218e53";
  /**
   * 业务的状态常量，0未处理，1处理中，2已处理，3，过号
   */
  public static final Integer BUSINESS_UNTREATED = 0;
  public static final Integer BUSINESS_HANDING = 1;
  public static final Integer BUSINESS_HANDLED = 2;
  public static final Integer BUSINESS_EXPIRED = 3;
  /**
   * @Fields MAX_TASKS_NAME : 最大任务数名称
   */
  public static final String MAX_TASKS_NAME = "max_tasks";
  /**
   * @Fields MAX_TASKS : 最大任务数
   */
  public static final AtomicInteger MAX_TASKS = new AtomicInteger();
  /**
   * @Fields taskNumber : 可用任务数量
   */
  public static final AtomicInteger taskNumber = new AtomicInteger();
  /**
   * @Fields dynDelMap : 动态终端Map  {业务key：动态终端号码}
   */
  public static final ConcurrentHashMap<Integer, String> dynDelMap = new ConcurrentHashMap<Integer, String>();
  /**
   * @Fields 业务队列前缀
   */
  public final static String QUEUE_KEY = "remote:business:";
  /**
   * @Fields android叫号机队列前缀
   */
  public final static String WEBSOCKET_KEY = "webSocket:";
  /**
   * @Fields 在线等待业务员队列前缀
   */
  public final static String WAITING_WORK_KEY = "waiting:business:";
  /**
   *  x86 返回数据redis key前缀
   */
  public final static String EMBEDDED_SERVER_KEY = "embedded";
  /**
   * 用户类型 0超管，1统筹管理员，2审核管理员 3 审核业务员 4 统筹业务员
   */
  public final static String SUPER_ADMIN = "0";
  public final static String FIRST_ADMIN = "1";
  public final static String SECOND_ADMIN = "2";
  public final static String COMMON_USER = "3";
  public final static String FIRST_COMMON_USER = "4";
  public final static Integer SUPER_ADMIN_ID = 1;
  /**
   * 统筹中心管理员
   */
  public final static String PARENT_KEY = "0";

  /**
   * 窗口是否在服务中 1：服务中 2：空闲中
   */
  public final static Integer ISUSE = 1;
  /** 数据权限过滤 */
  public static final String SQL_FILTER = "sql_filter";
  /**
   * X86 地址常量
   */
  public static final String OFFICE_WINDOWS_SERVICE = "/api/Telecommunication/CallDevice";
  /**
   * 人脸比对x86接口
   */
  public static final String FACE_ALiGNMENT = "/api/Hard/Recognition";

  public static List<String> suffixList = new ArrayList<String>(2);
  /**
   * @Fields CMS_IMAGE_URI : 存储网关图片地址uri
   */
  public static String CMS_IMAGE_URI = "";
  /**
   * @Fields CMS_IMAGE_URI_KEY : 存储网关图片地址uri的key
   */
  public static String CMS_IMAGE_URI_KEY = "cms_image_uri";
  /**
   * 打印队列
   */
  public static LinkedBlockingQueue<Print> printLinkedQueue = new LinkedBlockingQueue<>();
  /**
   * 在线业务员等待业务队列
   */
  public static LinkedBlockingQueue<SysUserBean> userBeanLinkedQueue = new LinkedBlockingQueue<>();

  /**
   * 用户状态 0离线，1在线
   */
  public static String USER_OFF_LINE = "0";
  public static String USER_IN_LINE = "1";

    /**
     * 窗口使用状态 1 使用中 2空闲中 3等待中
   */
  public static Integer WINDOW_IS_USER = 1;
  public static Integer WINDOW_NO_USE = 2;
    public static Integer WINDOW_WAIT= 3;


  static {
    suffixList.add("doc");
    suffixList.add("docx");
//        suffixList.add("JPG");
  }

  /**
   * 终端形态 0：未分配 1：村终端 2：镇终端 3：默认终端即统筹 4：高拍仪终端'
   */
  public static final int FORM_ZERO = 0;
  public static final int FORM_ONE = 1;
  public static final int FORM_TWO = 2;
  public static final int FORM_THREE = 3;
  public static final int FORM_FOUR = 4;
  /**
   * 中心类型Type  1：统筹中心(协助) 2：审核中心 3：服务中心 4：统筹中心（受理）'
   */
  public static final Long CENTER_ONE = 1L;
  public static final Long CENTER_TWO = 2L;
  public static final Long CENTER_THREE = 3L;
  public static final Long CENTER_FOUR = 4L;

  /**
   * 部门状态
   */
  public static final int DEPT_STATE_ENABLE = 1;
  public static final int DEPT_STATE_DISABLE = -1;
  public static final int DEPT_STATE_DELETE = 0;

  /**
   *  当用户为超级管理员时，用户部门id=0
   */
  public static final int SUPER_ADMIN_DEPTID_TYPE = 0;
  /**
   * 部门最多三个
   */
  public static final Integer deptLimitNumber = 3;

  /**
   * 中心类型 1可用 0删除 0不可用
   */
  public static final int SERVICECENTER_ENABLE = 1;
  public static final int SERVICECENTER_DELETE = 0;
  public static final int SERVICECENTER_DISABLE = -1;


  /**
   * 前端通信type常量
   */
  public static final String NEW_CONNECTION_RELATION = "0";
  public static final String HIGH_SHOTMETER = "1";
  public static final String CARD_READER = "2";
  public static final String SIGN_ATURE_BOARD = "3";
  public static final String PRINTER = "4";
  public static final String FACE_ALIGNMENT = "5";
  public static final String HEART_BEAT = "10";
  public static final String BUSINESS_INFO = "6";
  public static final String BUSINESS_COUNT = "7";
  public static final String BUSINESS_CONNECTION_RELATION = "8";
  public static final String EQUIPMENT__STATUS="9";
  public static  final String SCUVERSION="11";
  public static  final String SCURESTART="12";
  public static  final String SCUALLSTATE="8";
  public static final String ALL_PROCESS = "01";
  public static final String DEPARTMENTAL_PROCESS = "02";
  public static  final String DEP_UPDATE_TYPE="17";
  public static final String H5_DETECTION = "6";
  public static final String H5_32_DETECTION = "13";

  /**
   * 叫号机业务模式"01", "一窗综办","02", "分部门办理"
   */
  public static final String ANDROID_BUSINESS_TYPE_ALL = "01";
  public static final String ANDROID_BUSINESS_TYPE_DEPARTMENTAL = "02";



  public static final String BUSINESS_TO_DO = "15";//业务待办
  public static final String BUSINESS_TYPE = "16";//业务种类
  public static final String BUSINESS_JHJ_DELETE="18";//叫号机删除业务推送type

  /**
   * 业务等级
   */
  public static final int BUSINESS_GRADE = 1;

  /**
   * 盖章
   */
  public static final int SEAL = 1;

  /**
   * 手机号正则
   */
  public static final String MOBILEPHONE_REGX = "^[1](([3][0-9])|([4][0-9])|([5][0-9])|([6][0-9])|([7][0-9])|([8][0-9])|([9][0-9]))[0-9]{8}$";
  /**
   * 邮箱正则
   */
  public static final String EMAIL_REGX = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
  /**
   *登录名正则
   */
  public static final String LOGINNAME_REGX = "^[a-zA-Z0-9_]{0,16}$";
  /**
   *用户姓名正则
   */
  public static final String USERNAME_REGX = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{0,16}$";
  /**
   *密码正则
   */
  public static final String PASSWORD_REGX = "^\\w+${6,16}";
  /**
   * 过滤其他
   */
  public static final int isRests = 1;
  /**
   *
   */
  public static final String WEBSOCKET_USERNAME = "name";

  public static final String CHECK_WEBSOCKET_HEALTH_FLAG = "webSocketHealthFlag";

  public static final String WEBSOCKET_UNHEALTH_NUMBER = "webSocketUnHealthNumber";

    /**
     * 叫号机检测数据key
     */
    public static final String ANDROID_DETECTION_KEY = "AndroidData:";
    /**
     * SCU检测数据Key
     */
    public static final String SCU_DETECTION_KEY = "ScuData:";
    /**
     * 终端检测数据Key
     */
    public static final String DEVICE_DETECTION_KEY = "DeviceData:";

}
