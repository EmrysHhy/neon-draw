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
public enum ActivityStatusEnum {

    RUNNING(1,"活动进行中"),
    COMPLETE(2,"活动已结束");
    private final Integer code;
    private final String message;

    /**
     * 根基传入的String返回对应的枚举类型
     * @param name
     * @return
     */
    public static ActivityStatusEnum forName(String name){
        for (ActivityStatusEnum activityEnum : ActivityStatusEnum.values()) {
            if (activityEnum.name().equals(name)) {
                return activityEnum;
            }
        }
        return null;
    }
}
