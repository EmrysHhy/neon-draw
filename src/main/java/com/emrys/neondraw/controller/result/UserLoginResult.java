package com.emrys.neondraw.controller.result;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:作为登录的同一返回
 */
@Data
@Slf4j
public class UserLoginResult implements Serializable {
    private String token;
    private String identity;

}
