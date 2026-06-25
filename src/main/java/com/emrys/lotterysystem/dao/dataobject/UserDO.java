package com.emrys.lotterysystem.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Emrys
 * content:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDO extends BaseDO {
    private String userName;
    private String email;
    private String identity;
    private Encrypt phoneNumber;
    private String password;

}
