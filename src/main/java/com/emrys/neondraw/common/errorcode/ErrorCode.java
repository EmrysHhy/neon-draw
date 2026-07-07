package com.emrys.neondraw.common.errorcode;

import lombok.Data;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class ErrorCode {

    private int code;
    private String message;
    ErrorCode(int code,String message) {
        this.code = code;
        this.message = message;
    }

}
