package com.emrys.lotterysystem.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupDTO {
    private Long groupId;
    private String name;
    private String description;
    private Integer memberCount;
    /** 该组成员 userId 列表，供前端筛选使用 */
    private List<Long> userIds;
}
