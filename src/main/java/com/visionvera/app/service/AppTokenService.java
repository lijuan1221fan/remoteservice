package com.visionvera.app.service;

import com.visionvera.app.entity.AppToken;

/**
 * @author EricShen
 * @date 2019-03-13
 */
public interface AppTokenService {

    /**
     * token查询appToken
     *
     * @param token
     * @return
     */
    AppToken queryByToken(String token);

    /**
     * 生成token
     *
     * @param userId 用户ID
     * @return 返回token信息
     */
    AppToken createToken(long userId);

    /**
     * 设置token过期
     *
     * @param userId 用户ID
     */
    void expireToken(long userId);
    /**
     * 重置过期时间
     */
    void updateExpire(AppToken appToken);
    /**
     * appUserId查询appToken
     *
     * @param appUserId
     * @return
     */
    AppToken queryByAppUserId(Long appUserId);

}
