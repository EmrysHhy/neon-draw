package com.emrys.lotterysystem.common.errorcode;

/**
 *
 * @author Emrys
 * content:
 */
public interface ControllerErrorCodeConstants {
    //人物错误码
    ErrorCode REGISTER_ERROR  = new ErrorCode(100,"注册失败");

    ErrorCode LOGIN_ERROR = new ErrorCode(101,"登录失败");;
    //奖品错误码
    ErrorCode FIND_PRIZE_LIST_ERROR = new ErrorCode(300,"查询奖品列表失败");
    //活动错误码
    ErrorCode CREATE_ACTIVITY_ERROR = new ErrorCode(400,"活动创建失败");;
}
