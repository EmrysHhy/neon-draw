package com.emrys.neondraw.controller.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class createActivityResult implements Serializable {
    private Long activityId;  //活动的id


}
