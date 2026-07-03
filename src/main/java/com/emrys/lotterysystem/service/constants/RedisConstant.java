package com.emrys.lotterysystem.service.constants;

/**
 *
 * @author Emrys
 * content:
 */
public class RedisConstant {
    public static final String VARIFICATION_CODE_ = "varification_code_";
    public static final String  ACTIVITY_DETAIL_ = "activity_detail_";
    public static final String WINNING_ACTIVITY_PRIZE_RECORD_ = "winning_activity_prize_record";
    public static final String WINNING_ACTIVITY_RECORD_ = "winning_activity_record";
    public static final Long WINNING_TIMEOUT = 60*60*24*7L;//保存7天中奖记录
}
