package com.emrys.lotterysystem.common.errorcode;

/**
 *
 * @author Emrys
 * content: 业务错误码，按模块分段（100 人员 / 301 活动 / 401 活动人员 / 501 活动奖品 / 601 图片 / 701 小组 / 801 Redis）
 */
public interface ServiceErrorCodeConstants {
    // 人员错误码 100-
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
    ErrorCode LOGIN_ACCOUNT_AMBIGUOUS = new ErrorCode(113, "存在多个匹配账号，无法登录");
    ErrorCode VARIFICATION_CODE_ERROR = new ErrorCode(116, "验证码错误");
    ErrorCode REGISTER_ADMIN_TOKEN_REQUIRED = new ErrorCode(114, "注册普通用户需要管理员登录");
    ErrorCode REGISTER_TOKEN_INVALID = new ErrorCode(115, "注册 token 无效或已过期");

    // 活动错误码 301-
    ErrorCode ACTIVITY_PARAM_EMPTY = new ErrorCode(301, "创建活动参数param为空");
    ErrorCode ACTIVITY_DETAIL_CACHE_ERROR = new ErrorCode(302, "添加缓存失败");
    ErrorCode ACTIVITY_DETAIL_CACHE_ERROR2 = new ErrorCode(303, "获取缓存失败");
    ErrorCode ACTIVITY_ID_EMPTY = new ErrorCode(304, "活动id为空");
    ErrorCode ACTIVITY_NOT_FOUND = new ErrorCode(305, "活动不存在");
    ErrorCode ACTIVITY_NOT_RUNNING = new ErrorCode(306, "活动当前不在运行中");
    ErrorCode STATUS_CONVERT_FAIL = new ErrorCode(307, "状态转换失败");

    // 活动与人员错误码 401-
    ErrorCode ACTIVITY_USER_NOT_EXIST = new ErrorCode(401, "创建人员信息不存在");
    ErrorCode ACTIVITY_WINNER_NOT_IN_ACTIVITY = new ErrorCode(402, "中奖人员不在活动参与人员中");
    ErrorCode ACTIVITY_WINNER_INVALID_STATUS = new ErrorCode(403, "中奖人员状态无效，存在已中奖人员");

    // 活动与奖品错误码 501-
    ErrorCode ACTIVITY_PRIZE_NOT_EXIST = new ErrorCode(501, "创建奖品信息信息不存在");
    ErrorCode ACTIVITY_PRIZE_TOO_MACH = new ErrorCode(502, "奖品数量太多");
    ErrorCode ACTIVITY_PRIZE_TIER_ERROR = new ErrorCode(503, "奖品等级错误");
    ErrorCode ACTIVITY_PRIZE_ALREADY_DRAW = new ErrorCode(504, "奖品已经被抽取");
    ErrorCode ACTIVITY_PRIZE_AMOUNT_EXCEED_WINNER = new ErrorCode(505, "奖品数量大于中奖人员数量");
    ErrorCode WINNING_RECORD_CACHE_ERROR = new ErrorCode(506, "中奖记录缓存失败");
    ErrorCode DRAW_PRIZE_DUPLICATE = new ErrorCode(507, "该奖品正在抽奖中，请勿重复提交");

    // 图片错误码 601-
    ErrorCode PICTURE_UPLOAD_FAILED = new ErrorCode(601, "图片上传失败");

    // 小组错误码 701-
    ErrorCode GROUP_NAME_DUPLICATE = new ErrorCode(701, "小组名称已存在");
    ErrorCode GROUP_CREATE_FAILED = new ErrorCode(702, "小组创建失败");
    ErrorCode GROUP_NOT_FOUND = new ErrorCode(703, "小组不存在");
    ErrorCode GROUP_MEMBER_EMPTY = new ErrorCode(704, "小组成员不能为空");
    ErrorCode GROUP_MEMBER_NOT_FOUND = new ErrorCode(705, "小组成员不存在");
    ErrorCode GROUP_MEMBER_IDENTITY_ERROR = new ErrorCode(706, "只能添加普通用户到小组");
    ErrorCode GROUP_MEMBER_NOT_BELONG_ADMIN = new ErrorCode(707, "用户不属于当前管理员");
    ErrorCode GROUP_DELETE_FAILED = new ErrorCode(708, "小组删除失败");
}
