package com.emrys.lotterysystem.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户小组
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserGroupDO extends BaseDO {
    private Long adminId;
    private String name;
    private String description;
}
