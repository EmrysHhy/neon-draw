package com.emrys.lotterysystem.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Emrys
 * content:
 */
@AllArgsConstructor
@Getter
public enum UserIdentityEnum {
    ADMIN("管理员"),
    NORMAL("普通用户");
    private final String message;
    /**
     * 是否在枚举里面
     */
    public static UserIdentityEnum forName(String name ){
        for (UserIdentityEnum userIdentityEnum : UserIdentityEnum.values()) {
            if (userIdentityEnum.getMessage().equals(name)) {
                return userIdentityEnum;
            }
        }
        return null;
    }
}
