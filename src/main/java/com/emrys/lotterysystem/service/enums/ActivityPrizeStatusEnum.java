package com.emrys.lotterysystem.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Emrys
 * content:奖品状态
 */
@Getter
@AllArgsConstructor
public enum ActivityPrizeStatusEnum {

    //未抽取
    NOT_DRAW(1,"未抽取"),
    //已抽取
    DRAWN(2,"已抽取");
    private final Integer code;
    private final String message;

    /**
     * 根基传入的String返回对应的枚举类型
     * @param name
     * @return
     */
    public static ActivityPrizeStatusEnum forName(String name) {
        for (ActivityPrizeStatusEnum activityEnum : ActivityPrizeStatusEnum.values()) {
            if (activityEnum.name().equals(name)) {
                return activityEnum;
            }
        }
        return null;
    }
}
