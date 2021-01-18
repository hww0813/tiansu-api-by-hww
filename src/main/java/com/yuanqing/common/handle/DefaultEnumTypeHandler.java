package com.yuanqing.common.handle;

import com.yuanqing.framework.web.enums.BaseEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * string转枚举
 *
 * @author xucan
 **/
@MappedJdbcTypes(value = JdbcType.VARCHAR, includeNullJdbcType = true)
public class DefaultEnumTypeHandler extends BaseTypeHandler<BaseEnum> {

    private Class<BaseEnum> type;

    public DefaultEnumTypeHandler() {
    }

    public DefaultEnumTypeHandler(Class<BaseEnum> type) {
        if (type == null) throw new IllegalArgumentException("Type argument cannot be null");
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BaseEnum parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public BaseEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convert(rs.getString(columnName));
    }

    @Override
    public BaseEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convert(rs.getString(columnIndex));
    }

    @Override
    public BaseEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convert(cs.getString(columnIndex));
    }

    private BaseEnum convert(String columnValue) {
        BaseEnum[] objs = type.getEnumConstants();
        for (BaseEnum em : objs) {
            if (em.getValue().equals(columnValue)) {
                return em;
            }
        }
        return null;
    }
}
