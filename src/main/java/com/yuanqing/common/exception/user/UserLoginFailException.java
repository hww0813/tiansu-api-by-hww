package com.yuanqing.common.exception.user;

/**
 * Created by xucan on 2020-09-22 10:31
 * @author xucan
 */
public class UserLoginFailException extends UserException {

    private static final long serialVersionUID = 1L;

    public UserLoginFailException(int failNum, int lockTime)
    {
        super("user.password.retry.limit.exceed", new Object[] {failNum,lockTime});
    }
}
