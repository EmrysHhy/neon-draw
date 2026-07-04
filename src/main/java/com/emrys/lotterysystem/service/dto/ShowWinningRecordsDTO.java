package com.emrys.lotterysystem.service.dto;

import com.emrys.lotterysystem.service.enums.PrizeTierEnum;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class ShowWinningRecordsDTO {
    private Long winnerId;
    private String winnerName;
    private String PrizeName;
    private PrizeTierEnum PrizeTier;
    private Date winningTime;
}
