package com.visionvera.app.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.app.annotation.AppLogin;
import com.visionvera.app.dao.AppTokenDao;
import com.visionvera.app.dao.AppUserDao;
import com.visionvera.app.dao.AppointmentInfoDao;
import com.visionvera.app.entity.AppToken;
import com.visionvera.app.entity.AppUser;
import com.visionvera.app.pojo.AppUserVo;
import com.visionvera.app.request.LoginRequest;
import com.visionvera.app.service.AppTokenService;
import com.visionvera.app.service.AppUserService;
import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.util.EncryptionUtil;
import com.visionvera.remoteservice.util.IdCardUtils;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.StringUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author EricShen
 * @date 2019-03-13
 */
@Service("appUserService")
public class AppUserServiceImpl implements AppUserService {

    @Resource
    private AppTokenService appTokenService;
    @Resource
    private AppUserDao appUserDao;
    @Resource
    private AppTokenDao appTokenDao;
    @Resource
    private AppointmentInfoDao appointmentInfoDao;

    private static Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);


    @Override
    public Map<String, Object> login(LoginRequest request) {
        AppUser user = getAppUserByLoginName(request.getLoginName());
        //密码错误
        if(user == null){
          logger.info("登录名不存在");
          throw new MyException("登录名不存在！");
        }
        if (!user.getPassword().equals(EncryptionUtil.encrypt(request.getPassword()))) {
            logger.info("登录名或密码错误");
            throw new MyException("登录名或密码错误");
        }
        //获取登录token
        AppToken appToken = appTokenService.createToken(user.getAppUserId());
        Map<String, Object> map = new HashMap<>(2);
        map.put("token", appToken.getToken());
        logger.info(
            "tokenEntity.getExpireTime().getTime() = " + appToken.getExpireTime().getTime());
        map.put("expire", appToken.getExpireTime().getTime());
        map.put("appUserId",user.getAppUserId());
        map.put("realName",user.getRealName());
        map.put("idCardNo",user.getIdCardNo());
        return map;
    }

    @Override
    public Map<String, Object> addUser(AppUserVo appUserVo) {
        //验证身份证的正确性
        if(appUserVo.getIdCardNo() != null) {
            if (!IdCardUtils.isIDCard18(appUserVo.getIdCardNo())) {
                return ResultUtils.error("身份证错误，请重新输入");
            }
        }
        AppUser user = appUserDao.getAppUserByName(appUserVo.getLoginName());
        if (user != null) {
            logger.info("用户名已存在");
            return ResultUtils.error("用户名已存在");
        }
        AppUser appUserByCardNum = appUserDao.getAppUserByCardNum(appUserVo.getIdCardNo());
        if (appUserByCardNum != null) {
            logger.info("身份证号已存在");
            return ResultUtils.error("身份证号已存在");
        }
        //加密
        String password = EncryptionUtil.encrypt(appUserVo.getPassword());
        appUserVo.setPassword(password);
        int i = appUserDao.addUser(appUserVo);
        if (i > 0) {
            return ResultUtils.ok("添加用户成功");
        }
        return ResultUtils.error("添加失败");
    }

    @Override
    public AppUser getAppUserByLoginName(String loginName) {
        return appUserDao.getAppUserByName(loginName);
    }

    @Override
    public Map<String, Object> updateUser(AppUserVo appUserVo) {
        //验证身份证的正确性
        if(appUserVo.getIdCardNo() != null) {
            if (!IdCardUtils.isIDCard18(appUserVo.getIdCardNo())) {
                return ResultUtils.error("身份证错误，请重新输入");
            }
        }
        AppUser user = appUserDao.getAppUserByName(appUserVo.getLoginName());
        if (user != null && !appUserVo.getAppUserId().equals(user.getAppUserId())) {
            return ResultUtils.error("用户名已存在");
        }
        if (appUserVo.getIdCardNo() != null) {
            AppUser appUserByNum = appUserDao.getAppUserByCardNum(appUserVo.getIdCardNo());
            if (appUserByNum != null && !appUserByNum.getAppUserId()
                .equals(appUserVo.getAppUserId())) {
                return ResultUtils.error("身份证号已存在");
            }
        }
        appUserVo.setUpdateTime(new Date());
        if (appUserVo.getPassword() != null) {
            //加密
            String password = EncryptionUtil.encrypt(appUserVo.getPassword());
            appUserVo.setPassword(password);
        }
        int i = appUserDao.updateUser(appUserVo);
        if (i > 0) {
          appointmentInfoDao.updateIdCardNoByAppUserId(appUserVo);
            return ResultUtils.ok("修改成功");
        }
        return ResultUtils.error("修改失败");
    }

    @Override
    public Map<String, Object> delUser(Long appUserId) {
        int i = appUserDao.delUser(appUserId);
        if (i > 0) {
            appTokenDao.deleteTokenByAppUserId(appUserId);
            return ResultUtils.ok("删除成功");
        }
        return ResultUtils.ok("删除失败");
    }

    @Override
    public Map<String, Object> getUsers(AppUserVo appUserVo) {
        PageHelper.startPage(appUserVo.getPageNum(), appUserVo.getPageSize());
        List<AppUser> userList = appUserDao.getUsers(appUserVo);
        PageInfo<AppUser> pageInfo = new PageInfo<AppUser>(userList);
        return ResultUtils.ok("查询成功", pageInfo);
    }

    @Override
    public Map<String, Object> getInfo(AppUser appUser) {
        /*String token = appTokenVo.getToken();
        AppToken tokenInfoByToken = appTokenDao.getTokenInfoByToken(token);
        long expireTime = tokenInfoByToken.getExpireTime().getTime();
        long now = System.currentTimeMillis();
        if (now > expireTime) {
            return ResultUtils.error("token过期");
        }*/
        AppUser userInfo = appUserDao.getInfo(appUser.getLoginName());
        if (userInfo == null) {
            return ResultUtils.error("用户信息不存在，请重新登录");
        }
        if (StringUtil.isEmpty(userInfo.getRealName()) && StringUtil
            .isEmpty(userInfo.getIdCardNo())) {
            return ResultUtils.error("请重新登录，实名认证后查看");
        }
        return ResultUtils.ok("查询成功", userInfo);
    }
    @AppLogin
    @Override
    public Map<String,Object> authentication(AppUser appUser,AppUserVo appUserVo){
        //验证身份证的正确性
        if(!IdCardUtils.isIDCard18(appUserVo.getIdCardNo())){
           return ResultUtils.error("身份证错误，请重新输入");
        }
        //校验身份证号码是否已经存在
        AppUserVo appVo = new AppUserVo();
        appVo.setIdCardNo(appUserVo.getIdCardNo());
        List<AppUser> user = appUserDao.getUsers(appVo);
        if(user.size() > 0){
            return ResultUtils.error("认证失败，身份证号码已被认证");
        }
        AppToken token  = appTokenService.queryByAppUserId(appUser.getAppUserId());
        appUserVo.setAppUserId(appUser.getAppUserId());
        appUserVo.setUpdateTime(new Date());

        int i = appUserDao.updateUser(appUserVo);
        String msg ="";
        Map<String, Object> map = new HashMap<>(2);
        if(i > 0){
            msg = "认证成功";
        }else{
            msg = "认证失败";
        }
        map.put("token", token.getToken());
        return ResultUtils.ok(msg, map);
    }

  /**  根据用户id 查询用户信息
   * @param appUserId
   * @return appUser
   */
  @Override
  public AppUser getAppUserByAppUserId(Long appUserId) {
      return appUserDao.getAppUserByAppUserId(appUserId);

  }

  @Override
  public Map<String, Object> updatePassword(AppUserVo appUserVo) {
    //加密
    String password = EncryptionUtil.encrypt(appUserVo.getPassword());
    appUserVo.setPassword(password);
    int i = appUserDao.updatePassword(appUserVo);
    if (i > 0) {
      return ResultUtils.ok("修改密码成功");
    }
    return ResultUtils.error("修改失败");
  }

  @Override
  public Map<String, Object> showInfo(Long appUserId) {
    AppUser appUser = appUserDao.showInfo(appUserId);
    if (appUser != null) {
      return ResultUtils.ok("查询成功", appUser);
    }
    return ResultUtils.error("无该用户信息，请刷新页面");
  }

}
