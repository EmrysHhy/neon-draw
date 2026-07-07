package com.emrys.neondraw.service;

import com.emrys.neondraw.controller.param.AdminPasswordLoginParam;
import com.emrys.neondraw.controller.param.AdminRegisterParam;
import com.emrys.neondraw.controller.param.AdminVerificationLoginParam;
import com.emrys.neondraw.service.dto.UserLoginDTO;
import com.emrys.neondraw.service.dto.UserRegisterDTO;

public interface AdminService {

    UserRegisterDTO register(AdminRegisterParam param);

    UserLoginDTO login(AdminPasswordLoginParam param);

    UserLoginDTO login(AdminVerificationLoginParam param);
}
