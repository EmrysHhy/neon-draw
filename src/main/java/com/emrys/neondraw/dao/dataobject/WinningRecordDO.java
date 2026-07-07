package com.emrys.neondraw.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 中奖记录表 winning_record
 *
 * @author Emrys
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WinningRecordDO extends BaseDO {

    private Long activityId;
    private String activityName;
    private Long prizeId;
    private String prizeName;
    private String prizeTier;
    private Long winnerId;
    private String winnerName;
    private String winnerEmail;
    private Encrypt winnerPhoneNumber;
    private Date winningTime;
}
