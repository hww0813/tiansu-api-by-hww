package com.yuanqing.common.constant;

public interface IConstants {
    //redis缓存key值前缀
    public static final String CAMERA_HISTORY = "ts:CameraHistory";
    public static final String CAMERA = "ts:Camera";
    public static final String CLIENT = "ts:Client";
    public static final String CLIENT_TERMINAL = "ts:ClientTerminal";
    public static final String EXTERNAL_DEVICE = "ts:ExternalDevice";
    public static final String LOGIN_INFO = "ts:LoginInfo";
    public static final String SERVER_TREE = "ts:ServerTree";
    public static final String OPERATION_BEHAVIOR_SESSION = "ts:OperationBehaviorSession";

    public static final String TWO_FORMAT = "%s_%s";
    public static final String THREE_FORMAT = "%s_%s_%s";
}
