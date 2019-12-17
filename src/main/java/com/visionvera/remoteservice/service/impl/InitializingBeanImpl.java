package com.visionvera.remoteservice.service.impl;

import com.visionvera.common.utils.RedisUtils;
import com.visionvera.remoteservice.bean.CommonConfigBean;
import com.visionvera.remoteservice.bean.X86ConfigInformationBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.CommonConfigDao;
import com.visionvera.remoteservice.dao.EmbeddedServerDao;
import com.visionvera.remoteservice.dao.VcDevDao;
import com.visionvera.remoteservice.dao.X86ConfigInformationDao;
import com.visionvera.remoteservice.service.TaskLoadService;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;


/**
 * Title:InitializingBeanImpl Description: 加载完spring会执行 Company:
 *
 * @author quboka
 * @date 2017年9月19日 下午1:42:26
 */
@Service
public class InitializingBeanImpl implements InitializingBean {

  private Logger logger = LogManager.getLogger(getClass());
  // 确保只运行一次
  private static boolean firstInit = true;

  //x86版本号
  public static String version;
  //发送短信的地址
  public static String smsUrl;
  //发送短信的模板内容
  public static String smsMessage;

  public static HashMap<String,Object> map;

//	@Resource
//	private TerminalService terminalService;
//   @Resource
//   private JedisClient jedisClient;
//
//   @Resource
//   private RoleDao roleDao;
//   @Resource
//   private JedisPool jedisPool;

//	@Resource
//	private TerminalDao terminalDao ;

  @Resource
  private VcDevDao vcDevDao;

  @Resource
  private X86ConfigInformationDao x86ConfigInformationDao;

  @Resource
  private TaskLoadService taskLoadService;

  @Resource
  private CommonConfigDao commonConfigDao;

  @Resource
  private EmbeddedServerDao embeddedServerDao;

    @Resource
    private RedisUtils redisUtils;

  // 加载完spring会执行
  @Override
  public void afterPropertiesSet() {
    logger.info("spring加载完成");
    if (firstInit) {
      try {
        X86ConfigInformationBean configBean = new X86ConfigInformationBean();
        configBean.setColumnNameEn("version");
        List<X86ConfigInformationBean> list = x86ConfigInformationDao.getList(configBean);
        if (!CollectionUtils.isEmpty(list)) {
          version = list.get(0).getColumnValue();
        }
        CommonConfigBean url = commonConfigDao.getCommonConfigByName("meeting_url");
        CommonConfigBean message = commonConfigDao.getCommonConfigByName("sms_message");
        if(url != null){
          smsUrl = url.getCommonValue();
        }
        if(message != null){
          smsMessage = message.getCommonValue();
        }
        //map初始化
        map = new HashMap<>();
      } catch (Exception e) {
        e.printStackTrace();
      }

      try {
        //启动线程，发布订阅监听redis的key的有效期
        // new Thread(new JRedisKeyExpiredSubscriber(jedisPool)).start();
        //重置终端状态
        vcDevDao.resetTheState();
        embeddedServerDao.deleteAllEmbeddedServer();
          redisUtils.removePattern(CommonConstant.WEBSOCKET_KEY + "*");
      } catch (Exception e) {
        e.printStackTrace();
      }

      //心跳线程
      //new Thread(new HeartbeatThread(heartbeatService , roleDao ,jedisClient,vcDevDao)).start();

      try {
        //初始化任务负载量
        taskLoadService.setTaskLoadAndModify(null);
      } catch (Exception e) {
        e.printStackTrace();
      }

      try {
        //加载存储网关图片地址uri
        CommonConfigBean commonConfig = commonConfigDao
            .getCommonConfigByName(CommonConstant.CMS_IMAGE_URI_KEY);
        if (commonConfig != null && StringUtils.isNotEmpty(commonConfig.getCommonValue())) {
            CommonConstant.CMS_IMAGE_URI = commonConfig.getCommonValue();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      firstInit = false;
    }


  }


}
