package com.emrys.neondraw.dao.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Emrys
 * content: 数据向上返回的封装类(父类)
 */
@Data
public class BaseDO implements Serializable {
        private Long id;
        private LocalDateTime gmtCreate;
        private LocalDateTime gmtModified;
}
