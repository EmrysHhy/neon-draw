package com.emrys.neondraw.service.enums;

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

    SPECIAL_PRIZE(0,"特等奖"),
    FIRST_PRIZE(1,"一等奖"),
    SECOND_PRIZE(2,"二等奖"),
    THIRD_PRIZE(3,"三等奖");
    private final Integer code;
    private final String message;

    /** 排序权重，数值越小越靠前（特等奖、一等奖、二等奖…） */
    public int sortCode() {
        return code != null ? code : Integer.MAX_VALUE;
    }

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
