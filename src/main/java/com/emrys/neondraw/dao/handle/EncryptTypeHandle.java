package com.emrys.neondraw.dao.handle;


import com.emrys.neondraw.common.utils.AESUtil;
import com.emrys.neondraw.dao.dataobject.Encrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Emrys
 * content:mybatis格式转换处理器
 * 用于手机号对称加密
 */
@Slf4j
@MappedTypes(Encrypt.class)           //放在jvm里面的类型
@MappedJdbcTypes(JdbcType.VARCHAR)    // 放在MySQL里面的类型
public class EncryptTypeHandle extends BaseTypeHandler<Encrypt> {

    /**
     * 设置参数
     * @param ps   SQL预编译对象
     * @param i    需要赋值的索引位置
     * @param parameter 原本位置需要赋值的参数
     * @param jdbcType  JDBC类型
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Encrypt parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.getValue() == null) {
            ps.setString(i, null);
            return;
        }

        log.info("加密手机号：" + parameter.getValue());

        String str = AESUtil.encrypt(parameter.getValue());
        ps.setString(i, str);
    }

    /**
     * 获取值
     * @param rs  result
     * @param columnName  索引名
     * @return
     * @throws SQLException
     */
    @Override
    public Encrypt getNullableResult(ResultSet rs, String columnName) throws SQLException {
        log.info("获取值得到的加密内容：" + rs.getString(columnName));
        String str = AESUtil.decrypt(rs.getString(columnName));
        return str == null ? null : new Encrypt(str);
    }

    /**
     * 获取值
     * @param rs    result
     * @param columnIndex 索引
     * @return
     * @throws SQLException
     */
    @Override
    public Encrypt getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        log.info("获取值得到的加密内容：" + rs.getString(columnIndex));
        String str = AESUtil.decrypt(rs.getString(columnIndex));
        return str == null ? null : new Encrypt(str);
    }

    /**
     * 获取值
     * @param cs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public Encrypt getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        log.info("获取值得到的加密内容：" + cs.getString(columnIndex));
        String str = AESUtil.decrypt(cs.getString(columnIndex));
        return str == null ? null : new Encrypt(str);
    }
}
