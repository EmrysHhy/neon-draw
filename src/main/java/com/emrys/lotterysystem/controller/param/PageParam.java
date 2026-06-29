package com.emrys.lotterysystem.controller.param;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class PageParam implements Serializable {
    private Integer currentPage;//请求的页面
    private Integer pageSize;//每页的大小
    /**
     * 计算偏移量
     */
    public int getOffset() {
        return (currentPage - 1) * pageSize;
    }
}
