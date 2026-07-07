package com.emrys.neondraw.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Emrys
 * content:
 */
@AllArgsConstructor
@Getter
public enum ActivityUserStatusEnum {

    //未中奖
    NOT_WIN(1,"未中奖"),
    //已中奖
    WIN(2,"已中奖");
    private final Integer code;
    private final String message;

    /**
     * 根基传入的String返回对应的枚举类型
     * @param name
     * @return
     */
    public static ActivityUserStatusEnum forName(String name) {
        for (ActivityUserStatusEnum activityEnum : ActivityUserStatusEnum.values()) {
            if (activityEnum.name().equals(name)) {
                return activityEnum;
            }
        }
        return null;
    }
}
