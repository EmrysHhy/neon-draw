package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.common.errorcode.ControllerErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ControllerException;
import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.controller.param.UserRegisterParam;
import com.emrys.lotterysystem.controller.result.UserRegisterResult;
import com.emrys.lotterysystem.service.UserService;
import com.emrys.lotterysystem.service.VarificationCodeService;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Emrys
 * content:
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    VarificationCodeService varificationCodeService;
    /**
     * 注册接口
     */
    @RequestMapping("/register")
    public CommonResult<UserRegisterResult> userRegister(@Validated @RequestBody UserRegisterParam param) { //json转换成java对象
        log.info("user register, param: {}", param);
        UserRegisterDTO dto = userService.register(param);
        return CommonResult.success(convertToUserRegisterResult(dto));
    }

    /**
     * 请求f发送验证码
     * @param email
     * @return
     */
    @RequestMapping("/varification/send")
    public CommonResult<?> sendVarificationCode(String email) {
        log.info("send varification code, email: {}", email);
        //调用service层发送验证码
        varificationCodeService.sendVarificationCode(email);
        return CommonResult.success(200);
    }
    /**
     * 类型转换
     * @param userRegisterDTO
     * @return
     */
    private UserRegisterResult convertToUserRegisterResult(UserRegisterDTO userRegisterDTO) {
        UserRegisterResult result = new UserRegisterResult();
        if(userRegisterDTO == null){
            log.info("user register failed, userRegisterDTO is null");
            throw new ControllerException(ControllerErrorCodeConstants.REGISTER_ERROR);
        }
        result.setUserId(userRegisterDTO.getUserId());
        return result;
    }
}
