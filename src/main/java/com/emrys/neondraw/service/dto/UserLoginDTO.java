package com.emrys.neondraw.service.dto;

import com.emrys.neondraw.service.enums.UserIdentityEnum;
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
