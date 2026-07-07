package com.emrys.lotterysystem.controller.result;

import com.emrys.lotterysystem.service.dto.UserDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

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
