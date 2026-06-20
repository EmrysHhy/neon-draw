package com.emrys.lotterysystem.common.errorcode;

/**
 *
 * @author Emrys
 * content:
 */
public interface GlobalErrorCodeConstants {
    ErrorCode SUCCESS = new ErrorCode(200, "成功");
    ErrorCode UNKNOWN_ERROR = new ErrorCode(999, "未知错误");
    ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "服务器内部错误");
}
