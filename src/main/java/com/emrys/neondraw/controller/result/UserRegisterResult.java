package com.emrys.neondraw.controller.result;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:注册的同一返回
 */
@Data
@Slf4j
public class UserRegisterResult implements Serializable {
    Long userId;
}
