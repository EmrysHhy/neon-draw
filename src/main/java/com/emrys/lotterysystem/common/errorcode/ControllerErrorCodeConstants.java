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
}
