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

}
