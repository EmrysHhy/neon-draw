package com.emrys.lotterysystem.common.exception;

import com.emrys.lotterysystem.common.errorcode.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Emrys
 * content:
 */
@Data
@EqualsAndHashCode(callSuper = true)//采用父类的equlas和hashcode方法
public class ControllerException extends RuntimeException{
    private Integer code;
    private String message;
    public ControllerException() {

    }

    public ControllerException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public ControllerException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
