package com.emrys.neondraw.controller;

import com.emrys.neondraw.common.pojo.CommonResult;
import com.emrys.neondraw.controller.convert.ControllerConverter;
import com.emrys.neondraw.controller.param.MemberRegisterParam;
import com.emrys.neondraw.controller.result.BaseUserInfoResult;
import com.emrys.neondraw.controller.result.UserRegisterResult;
import com.emrys.neondraw.service.MemberService;
import com.emrys.neondraw.service.dto.UserDTO;
import com.emrys.neondraw.service.dto.UserRegisterDTO;
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
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/register")
    public CommonResult<UserRegisterResult> register(@Validated @RequestBody MemberRegisterParam param,
                                                     @RequestHeader("user_token") String token) {
        log.info("member register, param: {}", param);
        UserRegisterDTO dto = memberService.register(param, token);
        return CommonResult.success(ControllerConverter.convertToUserRegisterResult(dto));
    }

    @RequestMapping("/find-list")
    public CommonResult<List<BaseUserInfoResult>> findMemberList(@RequestHeader("user_token") String token,
                                                                @RequestParam(required = false) String filter,
                                                                @RequestParam(required = false) Long groupId) {
        log.info("findMemberList, filter={}, groupId={}", filter, groupId);
        List<UserDTO> userDTOS = memberService.findMemberList(token, filter, groupId);
        return CommonResult.success(ControllerConverter.convertToBaseUserInfoResultList(userDTOS));
    }
}
