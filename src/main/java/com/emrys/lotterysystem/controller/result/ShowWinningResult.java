package com.emrys.lotterysystem.controller.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class ShowWinningResult implements Serializable {
    private Long winnerId;
    private String winnerName;
    private String PrizeName;
    private String PrizeTier;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date winningTime;
}
