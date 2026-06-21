package com.emrys.lotterysystem.dao.dataobject;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author Emrys
 * content:mybatis存放在jvm的数据,可以转换成SQL中的类型
 */
@NoArgsConstructor
@Getter
public class Encrypt {
    private String value;
    public Encrypt(String value) {
        this.value = value;
    }
}
