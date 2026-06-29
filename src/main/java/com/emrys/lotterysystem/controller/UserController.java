package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.controller.convert.ControllerConverter;
import com.emrys.lotterysystem.common.errorcode.ControllerErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ControllerException;
import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.controller.param.UserPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.UserRegisterParam;
import com.emrys.lotterysystem.controller.param.UserVarificationLoginParam;
import com.emrys.lotterysystem.controller.result.BaseUserInfoResult;
import com.emrys.lotterysystem.controller.result.UserLoginResult;
import com.emrys.lotterysystem.controller.result.UserRegisterResult;
import com.emrys.lotterysystem.service.UserService;
import com.emrys.lotterysystem.service.VarificationCodeService;
import com.emrys.lotterysystem.service.dto.UserDTO;
import com.emrys.lotterysystem.service.dto.UserLoginDTO;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;
import com.emrys.lotterysystem.service.enums.UserIdentityEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        return CommonResult.success(ControllerConverter.convertToUserRegisterResult(dto));
    }

    /**
     * 请求f发送验证码
     * @param email
     * @return
     */
    @RequestMapping("/varification-code/send")
    public CommonResult<?> sendVarificationCode(@Param("email") String email) {
        log.info("send varification code, email: {}", email);
        //调用service层发送验证码
        varificationCodeService.sendVarificationCode(email);
        return CommonResult.success(200);
    }



    /**
     * 密码登录接口
     */
    @RequestMapping("/password/login")
    public CommonResult<?> userLoginByPassword(@Validated @RequestBody UserPasswordLoginParam userPasswordLoginParam) {
        log.info("userLoginByPassword,{}", userPasswordLoginParam);
        UserLoginDTO userLoginDTO = userService.login(userPasswordLoginParam);
        return CommonResult.success(ControllerConverter.convertToUserLoginResult(userLoginDTO));
    }

    /**
     * 验证码登录接口(当前不支持手机验证码登录)
     * @param userVarificationLoginParam
     * @return
     */
    @RequestMapping("/varification/login")
    public CommonResult<?> userLoginByVarification(@Validated @RequestBody UserVarificationLoginParam userVarificationLoginParam) {
        log.info("userLoginByVarification,{}", userVarificationLoginParam);
        UserLoginDTO userLoginDTO = userService.login(userVarificationLoginParam);
        return CommonResult.success(ControllerConverter.convertToUserLoginResult(userLoginDTO));
    }

    /**
     * 查询人员列表
     * @param identity
     * @return
     */
    @RequestMapping("/base-user/find-list")
    public CommonResult<List<BaseUserInfoResult>> findBaseUserInfo(String identity){
        log.info("findBaseUserInfo, identity: {}", identity);
        List<UserDTO> userDTOS = userService.findUserInfo(UserIdentityEnum.forName(identity));
        return CommonResult.success(ControllerConverter.convertToBaseUserInfoResultList(userDTOS));
    }
}
