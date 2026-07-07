package com.emrys.neondraw.controller;

import com.emrys.neondraw.common.pojo.CommonResult;
import com.emrys.neondraw.controller.convert.ControllerConverter;
import com.emrys.neondraw.controller.param.CreateGroupParam;
import com.emrys.neondraw.controller.param.ReNewGroupMembersParam;
import com.emrys.neondraw.controller.result.GroupResult;
import com.emrys.neondraw.service.GroupService;
import com.emrys.neondraw.service.dto.GroupDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping("/create")
    public CommonResult<Long> createGroup(@Validated @RequestBody CreateGroupParam param,
                                          @RequestHeader("user_token") String token) {
        log.info("create group, param: {}", param);
        return CommonResult.success(groupService.createGroup(param, token));
    }

    @RequestMapping("/renew-members")
    public CommonResult<Long> renewGroupMembers(@Validated @RequestBody ReNewGroupMembersParam param,
                                                @RequestHeader("user_token") String token) {
        log.info("renew group members, param: {}", param);
        return CommonResult.success(groupService.renewGroupMembers(param, token));
    }

    @RequestMapping("/find-list")
    public CommonResult<List<GroupResult>> findGroupList(@RequestHeader("user_token") String token) {
        log.info("find group list");
        List<GroupDTO> list = groupService.findGroupList(token);
        return CommonResult.success(ControllerConverter.convertToGroupResultList(list));
    }

    @RequestMapping("/delete")
    public CommonResult<Boolean> deleteGroup(@RequestParam Long groupId,
                                             @RequestHeader("user_token") String token) {
        log.info("delete group, groupId: {}", groupId);
        groupService.deleteGroup(groupId, token);
        return CommonResult.success(true);
    }
}
