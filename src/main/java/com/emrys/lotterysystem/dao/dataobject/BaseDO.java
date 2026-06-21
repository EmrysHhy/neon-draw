package com.emrys.lotterysystem.dao.dataobject;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content: 数据向上返回的封装类(父类)
 */
@Data
public class BaseDO implements Serializable {
        private Long id;
        private Long gmtCreate;
        private Long gmtModified;
}
