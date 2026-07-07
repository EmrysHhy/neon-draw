package com.emrys.neondraw.service.dto;

import com.emrys.neondraw.service.enums.UserIdentityEnum;
import lombok.Data;

/**
 *
 * @author Emrys
 * content:这里放置服务层人员全属性
 */
@Data
public class UserDTO {
    private Long userId;
    private String userName;
    private String email;
    private UserIdentityEnum identity;
    private String phoneNumber;
    /** 当前所属小组 id，未分组时为 null */
    private Long groupId;
    /** 当前所属小组名称，未分组时为 null */
    private String groupName;

}
