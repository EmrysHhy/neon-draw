package com.emrys.neondraw.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author Emrys
 * content:泛型,用于所有的页面返回
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageListDTO<T> {
    private Integer total;//总数量
    private List<T> records;//当前页数据


}
