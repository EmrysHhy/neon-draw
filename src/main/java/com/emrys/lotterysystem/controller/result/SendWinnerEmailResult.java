package com.emrys.lotterysystem.controller.result;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class SendWinnerEmailResult implements Serializable {
    String to;
    String winnerName;
    String activityName;
    String prizeName;
    String prizeTierMessage;
    String winningTime;
}
