package com.emrys.neondraw.controller.result;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class BaseUserInfoResult implements Serializable {
    private Long userId;
    private String userName;
    private String identity;
    /** 当前所属小组 id，未分组时为 null */
    private Long groupId;
    /** 当前所属小组名称，未分组时为 null */
    private String groupName;

}
