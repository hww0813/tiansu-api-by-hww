package com.yuanqing.framework.security.service;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.exception.user.UserLoginFailException;
import com.yuanqing.common.exception.user.UserNotRoleException;
import com.yuanqing.common.utils.file.FileUtils;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.exception.CustomException;
import com.yuanqing.common.exception.user.UserPasswordNotMatchException;
import com.yuanqing.common.utils.MessageUtils;
import com.yuanqing.framework.manager.AsyncManager;
import com.yuanqing.framework.manager.factory.AsyncFactory;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.framework.security.LoginUser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private IMacsConfigService macsConfigService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
//        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
//        String captcha = redisCache.getCacheObject(verifyKey);
//        redisCache.deleteObject(verifyKey);
//        if (captcha == null)
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
//            throw new CaptchaExpireException();
//        }
//        if (!code.equalsIgnoreCase(captcha))
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
//            throw new CaptchaException();
//        }


        List<MacsConfig> cnt = macsConfigService.selectMacsConfigByTypeAndName(new MacsConfig("system", "failLoginCnt"));
        List<MacsConfig> time = macsConfigService.selectMacsConfigByTypeAndName(new MacsConfig("system", "failedLoginTime"));

        int failedLoginCnt = Integer.parseInt(cnt.get(0).getValue());
        int failedLoginTime = Integer.parseInt(time.get(0).getValue());


        String key = String.format("%s", username);
        Integer failedCnt = redisCache.getCacheObject(key);

        // 用户验证
        Authentication authentication = null;
        try
        {
            if(failedCnt == null){
                failedCnt = 0;
            }else if(failedLoginCnt<failedCnt+1){
                throw new UserLoginFailException(failedCnt,failedLoginTime);
            }
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            //登录成功 失败次数归0
            redisCache.setCacheObject(key,0);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                //登录失败次数+1
                redisCache.setCacheObject(key,(failedCnt+1),failedLoginTime, TimeUnit.MINUTES);
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }


        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        if(loginUser.getUser().getRoles().size()<1){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.role.not.match")));
            throw new UserNotRoleException();
        }
        // 生成token
        return tokenService.createToken(loginUser);
    }
}
