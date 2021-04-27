package com.yuanqing.common.exception.user;

/**
 * 用户账户没有角色（未授权）异常类
 * Created by xucan on 2020-11-18 17:59
 * @author xucan
 */
public class UserNotRoleException extends UserException {

    private static final long serialVersionUID = 1L;

    public UserNotRoleException() {
        super("user.password.role.not.match", null);
    }
}
