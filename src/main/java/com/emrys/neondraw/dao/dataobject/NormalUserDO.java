package com.emrys.neondraw.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 普通用户/活动参与人员（不登录，归属某管理员，无邮箱/手机号唯一约束）
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NormalUserDO extends BaseDO {
    private Long adminId;
    private String userName;
    private String email;
    private Encrypt phoneNumber;
    /** 查询列表时关联 group_member 填充，非 normal_user 表字段 */
    private Long groupId;
    /** 查询列表时关联 group 表填充，非 normal_user 表字段 */
    private String groupName;
}
