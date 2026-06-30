package com.emrys.lotterysystem.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Emrys
 * content:
 */
@Getter
@AllArgsConstructor
public enum PrizeTierEnum {

    //未中奖
    FIRST_PRIZE(1,"一等奖"),
    //已中奖
    SECOND_PRIZE(2,"二等奖"),
    THIRD_PRIZE(3,"三等奖"),
    SPECIAL_PRIZE(4,"特等奖");
    private final Integer code;
    private final String message;

    /**
     * 根基传入的String返回对应的枚举类型
     * @param name
     * @return
     */
    public static PrizeTierEnum forName(String name) {
        for (PrizeTierEnum activityEnum : PrizeTierEnum.values()) {
            if (activityEnum.name().equals(name)) {
                return activityEnum;
            }
        }
        return null;
    }
}
