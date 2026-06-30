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

     //活动错误码
    ErrorCode ACTIVITY_PARAM_EMPTY= new ErrorCode(301, "创建活动参数param为空");
    ErrorCode ACTIVITY_DETAIL_CACHE_ERROR= new ErrorCode(302, "添加缓存失败");
    ErrorCode ACTIVITY_DETAIL_CACHE_ERROR2= new ErrorCode(303, "获取缓存失败");
     //活动与人员错误码
    ErrorCode ACTIVITY_USER_NOT_EXIST= new ErrorCode(401, "创建人员信息不存在");
     //活动与奖品错误码
     ErrorCode ACTIVITY_PRIZE_NOT_EXIST= new ErrorCode(501, "创建奖品信息信息不存在");
    ErrorCode ACTIVITY_PRIZE_TOO_MACH = new ErrorCode(502, "奖品数量太多");
    ErrorCode ACTIVITY_PRIZE_TIER_ERROR = new ErrorCode(503, "奖品等级错误");
    //图片错误码
    ErrorCode PICTURE_UPLOAD_FAILED = new ErrorCode(601, "图片上传失败");
}
