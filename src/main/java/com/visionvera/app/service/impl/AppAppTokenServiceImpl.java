package com.visionvera.app.service.impl;

import com.visionvera.app.dao.AppTokenDao;
import com.visionvera.app.entity.AppToken;
import com.visionvera.app.service.AppTokenService;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author EricShen
 * @date 2019-03-13
 */
@Service("tokenService")
public class AppAppTokenServiceImpl implements AppTokenService {
    @Resource
    private AppTokenDao appTokenDao;
    /**
     * 12小时后过期
     * 
     */
    @Value("${token.keepTime}")
    private Integer EXPIRE;

    @Override
    public AppToken queryByToken(String token) {
        AppToken appToken = appTokenDao.getTokenInfoByToken(token);
        return appToken;
    }

    @Override
    public AppToken createToken(long userId) {
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 3600 * 1000);
        //生成token
        String token = generateToken();
        AppToken appToken = appTokenDao.getTokenInfoByAppUserId(userId);
        //保存或更新用户token
        if(appToken == null){
            appToken = new AppToken();
            appToken.setAppUserId(userId);
            appToken.setToken(token);
            appToken.setCreateTime(now);
            appToken.setUpdateTime(now);
            appToken.setExpireTime(expireTime);
            appTokenDao.addToken(appToken);
        }else{
            appToken.setUpdateTime(now);
            appToken.setExpireTime(expireTime);
            appTokenDao.updateToken(appToken);
        }
        return appToken;
    }

    @Override
    public void expireToken(long userId){
        Date now = new Date();
        AppToken token = new AppToken();
        token.setAppUserId(userId);
        token.setUpdateTime(now);
        token.setExpireTime(now);
        appTokenDao.updateToken(token);
    }

    @Override
    public void updateExpire(AppToken appToken) {
        appToken.setUpdateTime(new Date());
        appTokenDao.updateToken(appToken);
    }

    private String generateToken(){
        return UUID.randomUUID().toString().replace("-", "");
    }
    @Override
    public AppToken queryByAppUserId(Long appUserId){
        return appTokenDao.getTokenInfoByAppUserId(appUserId);
    }
}
