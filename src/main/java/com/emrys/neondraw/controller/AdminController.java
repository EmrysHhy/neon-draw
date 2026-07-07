package com.emrys.neondraw.controller;

import com.emrys.neondraw.common.pojo.CommonResult;
import com.emrys.neondraw.controller.convert.ControllerConverter;
import com.emrys.neondraw.controller.param.AdminPasswordLoginParam;
import com.emrys.neondraw.controller.param.AdminRegisterParam;
import com.emrys.neondraw.controller.param.AdminVerificationLoginParam;
import com.emrys.neondraw.controller.result.UserLoginResult;
import com.emrys.neondraw.controller.result.UserRegisterResult;
import com.emrys.neondraw.service.AdminService;
import com.emrys.neondraw.service.VarificationCodeService;
import com.emrys.neondraw.service.dto.UserLoginDTO;
import com.emrys.neondraw.service.dto.UserRegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private VarificationCodeService varificationCodeService;

    @RequestMapping("/register")
    public CommonResult<UserRegisterResult> register(@Validated @RequestBody AdminRegisterParam param) {
        log.info("admin register, param: {}", param);
        UserRegisterDTO dto = adminService.register(param);
        return CommonResult.success(ControllerConverter.convertToUserRegisterResult(dto));
    }

    @RequestMapping("/varification-code/send")
    public CommonResult<?> sendVarificationCode(@RequestParam("email") String email) {
        log.info("send varification code, email: {}", email);
        varificationCodeService.sendVarificationCode(email);
        return CommonResult.success(200);
    }

    @RequestMapping("/password/login")
    public CommonResult<UserLoginResult> loginByPassword(@Validated @RequestBody AdminPasswordLoginParam param) {
        log.info("admin password login, loginName={}", param.getLoginName());
        UserLoginDTO userLoginDTO = adminService.login(param);
        return CommonResult.success(ControllerConverter.convertToUserLoginResult(userLoginDTO));
    }

    @RequestMapping("/varification/login")
    public CommonResult<UserLoginResult> loginByVerification(@Validated @RequestBody AdminVerificationLoginParam param) {
        log.info("admin verification login, email={}", param.getEmail());
        UserLoginDTO userLoginDTO = adminService.login(param);
        return CommonResult.success(ControllerConverter.convertToUserLoginResult(userLoginDTO));
    }
}
