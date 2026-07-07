package com.emrys.neondraw.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Emrys
 * content:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityDO extends BaseDO{
    private String activityName;
    private String description;
    private String status;
}
