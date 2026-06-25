package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ServiceException;
import com.emrys.lotterysystem.common.utils.JWTUtil;
import com.emrys.lotterysystem.common.utils.MD5Util;
import com.emrys.lotterysystem.common.utils.RegexUtil;
import com.emrys.lotterysystem.controller.param.UserPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.UserRegisterParam;
import com.emrys.lotterysystem.controller.param.UserVarificationLoginParam;
import com.emrys.lotterysystem.dao.dataobject.Encrypt;
import com.emrys.lotterysystem.dao.dataobject.UserDO;
import com.emrys.lotterysystem.dao.mapper.UserMapper;
import com.emrys.lotterysystem.service.UserService;
import com.emrys.lotterysystem.service.VarificationCodeService;
import com.emrys.lotterysystem.service.dto.UserLoginDTO;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;
import com.emrys.lotterysystem.service.enums.UserIdentityEnum;
import io.jsonwebtoken.security.Password;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Emrys
 * content:具体实现
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    VarificationCodeService varificationCodeService;
    /**
     * 注册
     * @param param
     * @return
     */
    @Override
    public UserRegisterDTO register(UserRegisterParam param) {
        //校验参数信息
        checkRegisterInfo(param);
        //构造dao层数据类 对敏感信息进行加密
        UserDO userDO = new UserDO();
        userDO.setUserName(param.getName());
        userDO.setEmail(param.getMail());
        userDO.setPhoneNumber(new Encrypt(param.getPhoneNumber()));
        userDO.setIdentity(param.getIdentity());
        if(StringUtils.hasText(param.getPassword())){
            userDO.setPassword(MD5Util.setAndGetSecret(param.getPassword()));
        }

        //保存到数据库
        int ret = userMapper.insert(userDO);
        if(!(ret > 0)){
            log.warn("注册失败，数据库插入失败，param={}", param);
            return null;
        }
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();//这个是控制和服务之间交互的数据封装
        userRegisterDTO.setUserId(userDO.getId());
        return userRegisterDTO;
    }

    /**
     * 密码登录
     * @param userPasswordLoginParam
     * @return
     */
    @Override
    public UserLoginDTO login(UserPasswordLoginParam userPasswordLoginParam) {
        if(userPasswordLoginParam == null) {
            throw new ServiceException(ServiceErrorCodeConstants.LOGIN_INFO_IS_EMPTY);
        }
        //是不是手机号
        UserDO userDO = null;
        if(RegexUtil.checkMobile(userPasswordLoginParam.getLoginName())){
            userDO = userMapper.selectByPhoneNumber(new Encrypt(userPasswordLoginParam.getLoginName()));
        }else if(RegexUtil.checkMail(userPasswordLoginParam.getLoginName())){ //是不是电子邮箱
            userDO = userMapper.selectByMail(userPasswordLoginParam.getLoginName());
        }else{
            //登录用户名错误
            log.error("login failed, loginName is not phone number or email, loginName={}", userPasswordLoginParam.getLoginName());
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_OR_EMAIL_ERROR);
        }

        if(userDO == null){
            log.error("login failed, user not found, loginName={}", userPasswordLoginParam.getLoginName());
            throw new ServiceException(ServiceErrorCodeConstants.USER_NOT_FOUND);
        }
        //校验是否是管理员
        if (UserIdentityEnum.ADMIN.name().equalsIgnoreCase(userPasswordLoginParam.getMandatoryIdentity())
                && !UserIdentityEnum.ADMIN.name().equalsIgnoreCase(userDO.getIdentity())) {
            log.error("login failed, user is not admin, loginName={}", userPasswordLoginParam.getLoginName());
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        }
        //校验密码
        if(!MD5Util.verify(userPasswordLoginParam.getPassword(), userDO.getPassword())){
            if(userPasswordLoginParam.getPassword() == null){
                throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_EMPTY);// 密码为空
            }

            log.error("login failed, password is incorrect, loginName={}", userPasswordLoginParam.getLoginName());
            throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_ERROR);
        }
        //生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDO.getId());
        claims.put("identity", userDO.getIdentity());
        String token = JWTUtil.genJwtToken(claims);
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setMandatoryIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
        return userLoginDTO;
    }

    /**
     * 验证码登录
     * @param userVarificationLoginParam
     * @return
     */
    @Override
    public UserLoginDTO login(UserVarificationLoginParam userVarificationLoginParam) {
        //校验邮箱格式
        if(!RegexUtil.checkMail(userVarificationLoginParam.getEmail())){
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_ERROR);
        }
        //数据库获取
        UserDO userDO = userMapper.selectByMail(userVarificationLoginParam.getEmail());
        if(userDO == null) {
            log.error("login failed, user not found, email={}", userVarificationLoginParam.getEmail());
            throw new ServiceException(ServiceErrorCodeConstants.USER_NOT_FOUND);
        }
        //校验是否为管理员
        if (UserIdentityEnum.ADMIN.name().equalsIgnoreCase(userVarificationLoginParam.getMandatoryIdentity())
                && !UserIdentityEnum.ADMIN.name().equalsIgnoreCase(userDO.getIdentity())) {
            log.error("login failed, user is not admin, loginName={}", userVarificationLoginParam.getEmail());
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        }
        //校验验证码是否正确
        String code = varificationCodeService.getVarificationCode(userVarificationLoginParam.getEmail());
        if (!userVarificationLoginParam.getVerificationCode().equals(code)) {
            log.error("login failed, varification code is incorrect, email={}", userVarificationLoginParam.getEmail());
            throw new ServiceException(ServiceErrorCodeConstants.VARIFICATION_CODE_ERROR);
        }

        //生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDO.getId());
        claims.put("identity", userDO.getIdentity());
        String token = JWTUtil.genJwtToken(claims);
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setMandatoryIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
        return userLoginDTO;
    }

    /**
     * 检查注册参数
     * @param param
     */
    private void checkRegisterInfo(UserRegisterParam param) {
        log.info("校验各个参数值");
        if(param == null){
            throw new ServiceException(ServiceErrorCodeConstants.REGISTER_INFO_IS_EMPTY);
        }
        //校验邮箱  邮箱必须是唯一的 xxx@xx.XXX
        if(!RegexUtil.checkMail(param.getMail())){
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_ERROR);
        }
        if(mailHasBennUsed(param.getMail())){
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_HAS_BEEN_USED);
        }

        //校验手机号  手机号有固定格式   手机号必须是唯一的
        if(!RegexUtil.checkMobile(param.getPhoneNumber())){
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);
        }
        if(phoneNumberHasBennUsed(param.getPhoneNumber())){
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_HAS_BEEN_USED);
        }
        //校验管理员密码, 密码格式大于8位
        if (param.getIdentity().equalsIgnoreCase(UserIdentityEnum.ADMIN.name())) { //忽略大小写
            if(param.getPassword() == null){
                throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_EMPTY);
            }
            if(!RegexUtil.checkPassword(param.getPassword())){
                throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_ERROR);
            }
        }
        //校验身份
        if(UserIdentityEnum.forName(param.getIdentity()) == null){
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        }

    }

    /**
     * 手机号是否已经被注册了
     * @param phoneNumber
     * @return
     */
    private boolean phoneNumberHasBennUsed( String phoneNumber) {
        int ret = userMapper.countByPhoneNumber(new Encrypt(phoneNumber));
        return ret > 0;
    }

    /**
     * 邮箱是否已经被注册了
     * @param mail
     * @return
     */
    private boolean mailHasBennUsed(String mail) {
        int ret = userMapper.countByMail(mail);
        return ret > 0;
    }

    /**
     * 用户名是否已经被注册了
     * @param name
     * @return
     */
//    private boolean nameHasBennUsed(String name) {
//        int ret = userMapper.countByName(name);
//        return ret > 0;
//    }


}
