package com.emrys.lotterysystem.controller.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GroupResult implements Serializable {
    private Long groupId;
    private String name;
    private String description;
    private Integer memberCount;
    private List<Long> userIds;
}
