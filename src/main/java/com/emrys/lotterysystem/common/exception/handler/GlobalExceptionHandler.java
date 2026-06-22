package com.emrys.lotterysystem.common.exception.handler;

import com.emrys.lotterysystem.common.errorcode.GlobalErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ControllerException;
import com.emrys.lotterysystem.common.exception.ServiceException;
import com.emrys.lotterysystem.common.pojo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author Emrys
 * content:  当出现异常的时候,这个类会把异常返回到前端
 */
@RestControllerAdvice   //捕获全局的异常
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)  //捕获ServiceException异常
    public CommonResult<?> serviceException(ServiceException e) {
        //打印日志
        log.error("服务层出现异常", e);
        return CommonResult.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());//
    }
    @ExceptionHandler(ControllerException.class)  //捕获ServiceException异常
    public CommonResult<?> controllerException(ControllerException e) {
        //打印日志
        log.error("控制层出现异常", e);
        return CommonResult.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());//
    }

    @ExceptionHandler(Exception.class)  //捕获ServiceException异常
    public CommonResult<?> exception(Exception e) {
        //打印日志
        log.error("出现异常", e);
        return CommonResult.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());//
    }
}
