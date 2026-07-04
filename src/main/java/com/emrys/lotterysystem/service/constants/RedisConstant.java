package com.emrys.lotterysystem.service.constants;

/**
 *
 * @author Emrys
 * content:
 */
public class RedisConstant {
    public static final String VARIFICATION_CODE_ = "varification_code_";
    public static final String  ACTIVITY_DETAIL_ = "activity_detail_";
    public static final String WINNING_ACTIVITY_PRIZE_RECORD_ = "winning_activity_prize_record_";
    public static final String WINNING_ACTIVITY_RECORD_ = "winning_activity_record_";
    /** 抽奖防重复提交锁，粒度：activityId + prizeId */
    public static final String DRAW_PRIZE_LOCK_ = "draw_prize_lock_";
    public static final Long WINNING_TIMEOUT = 60*60*24*7L;//保存7天中奖记录
    /** 抽奖防重复锁过期时间（秒），覆盖 MQ 消费窗口即可 */
    public static final Long DRAW_PRIZE_LOCK_TIMEOUT = 60L;
}
