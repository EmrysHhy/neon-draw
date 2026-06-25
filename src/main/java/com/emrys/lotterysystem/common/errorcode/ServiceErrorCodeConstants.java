package com.emrys.lotterysystem.common.errorcode;

/**
 *
 * @author Emrys
 * content:
 */
public interface ServiceErrorCodeConstants {
    //人员错误码
    ErrorCode REGISTER_INFO_IS_EMPTY = new ErrorCode(100, "注册信息为空");
    ErrorCode MAIL_ERROR = new ErrorCode(101, "邮箱格式错误");
    ErrorCode PHONE_NUMBER_ERROR = new ErrorCode(102, "手机号格式错误");
    ErrorCode PASSWORD_EMPTY = new ErrorCode(103, "管理员密码不能为空");
    ErrorCode PASSWORD_ERROR = new ErrorCode(104, "密码格式错误");
    ErrorCode IDENTITY_ERROR = new ErrorCode(105, "身份格式错误");
    ErrorCode MAIL_HAS_BEEN_USED = new ErrorCode(106, "邮箱已经被使用");
    ErrorCode PHONE_NUMBER_HAS_BEEN_USED = new ErrorCode(107, "手机号已经被使用");
    ErrorCode NAME_HAS_BEEN_USED = new ErrorCode(108, "用户名已经被使用了");
    ErrorCode MAIL_SEND_ERROR = new ErrorCode(109, "邮箱发送失败");
    ErrorCode LOGIN_INFO_IS_EMPTY = new ErrorCode(110, "登录信息为空");
    ErrorCode PHONE_NUMBER_OR_EMAIL_ERROR = new ErrorCode(111, "手机号或者邮箱不存在");
    ErrorCode USER_NOT_FOUND = new ErrorCode(112, "用户不存在");

    ErrorCode VARIFICATION_CODE_ERROR = new ErrorCode(113, "验证码错误");
}
