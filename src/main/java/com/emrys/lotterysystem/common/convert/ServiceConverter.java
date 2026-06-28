package com.emrys.lotterysystem.common.convert;

import com.emrys.lotterysystem.dao.dataobject.UserDO;
import com.emrys.lotterysystem.service.dto.UserDTO;
import com.emrys.lotterysystem.service.enums.UserIdentityEnum;

import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
public class ServiceConverter {
    /**
     *
     * @param  userDOS
     * @return List<UserDTO>
     */
    public static List<UserDTO> convertToUserDTOList(List<UserDO> userDOS){

        return userDOS.stream().map(userDO -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userDO.getId());
            userDTO.setUserName(userDO.getUserName());
            userDTO.setEmail(userDO.getEmail());
            userDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
            userDTO.setPhoneNumber(userDO.getPhoneNumber().getValue());
            return userDTO;
        }).toList();
    }
}
