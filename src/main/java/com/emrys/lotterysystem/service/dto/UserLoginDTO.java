package com.emrys.lotterysystem.service.dto;

import com.emrys.lotterysystem.service.enums.UserIdentityEnum;
import lombok.Data;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class UserLoginDTO {
    private String token;
    private UserIdentityEnum mandatoryIdentity;
}
