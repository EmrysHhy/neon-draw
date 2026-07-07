package com.emrys.neondraw.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 小组成员
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupMemberDO extends BaseDO {
    private Long groupId;
    private Long userId;
}
