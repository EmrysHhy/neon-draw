package com.emrys.neondraw.common.pojo;

import com.emrys.neondraw.common.errorcode.GlobalErrorCodeConstants;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:统一返回类
 */
@Data
public class CommonResult<T> implements Serializable {
    private Integer code;
    private String message;//错误描述
    private T data;

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.setMessage("success");
        result.setData(data);
        return result;
    }
    public static <T> CommonResult<T> error(Integer code, String message) {
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.setMessage(message);
        return result;
    }
    public static <T> CommonResult<T> error( String message) {
        CommonResult<T> result = new CommonResult<>();
        result.code = GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode();
        result.setMessage(message);
        return result;
    }
}
