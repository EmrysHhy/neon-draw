package com.emrys.neondraw.service.status;

import com.emrys.neondraw.service.dto.ConvertActivityStatusDTO;

/**
 *
 * @author Emrys
 * content:定义抽象类,方便子类扩展
 */
public abstract class StatusOperator {
    public abstract Integer sequence();

    /**
     * 执行状态转换
     *
     * @return true 表示转换成功，false 表示未转换或转换失败
     */
    public abstract Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO);
}
