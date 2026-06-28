package com.emrys.lotterysystem.service.dto;

import com.emrys.lotterysystem.service.enums.UserIdentityEnum;
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

}
