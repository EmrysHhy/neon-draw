package com.emrys.neondraw.common.exception;

import com.emrys.neondraw.common.errorcode.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Emrys
 * content:
 */
@Data
@EqualsAndHashCode(callSuper = true)//采用父类的equlas和hashcode方法
public class ServiceException extends RuntimeException {
    private Integer code;
    private String message;

    public ServiceException() {

    }

    public ServiceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public ServiceException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

}
