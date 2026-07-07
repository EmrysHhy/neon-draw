package com.emrys.neondraw.controller.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ReNewGroupMembersParam implements Serializable {
    @NotNull(message = "小组id不能为空")
    private Long groupId;
    /** 目标成员列表；传 null 或空列表时不做变更 */
    private List<Long> userIds;
}
