package com.emrys.lotterysystem.controller.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CreateGroupParam implements Serializable {
    @NotBlank(message = "小组名称不能为空")
    private String name;
    private String description;
    /** 可选：创建时一并加入的普通用户 id 列表 */
    private List<Long> userIds;
}
