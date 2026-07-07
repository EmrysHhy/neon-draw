package com.emrys.lotterysystem.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 管理员（可登录，邮箱/手机号全局唯一）
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminDO extends BaseDO {
    private String userName;
    private String email;
    private Encrypt phoneNumber;
    private String password;
}
