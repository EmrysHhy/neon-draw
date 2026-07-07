package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.AdminPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.AdminRegisterParam;
import com.emrys.lotterysystem.controller.param.AdminVerificationLoginParam;
import com.emrys.lotterysystem.service.dto.UserLoginDTO;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;

public interface AdminService {

    UserRegisterDTO register(AdminRegisterParam param);

    UserLoginDTO login(AdminPasswordLoginParam param);

    UserLoginDTO login(AdminVerificationLoginParam param);
}
