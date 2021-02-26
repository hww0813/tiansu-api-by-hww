package com.yuanqing.common.handle;

import com.yuanqing.common.utils.ip.IpUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

/**
 * IP integer转string
 *
 * @author jqchu
 * @version 1.0
 * @since 2017/11/13
 **/
@MappedJdbcTypes(value = {JdbcType.BIGINT, JdbcType.INTEGER, JdbcType.NUMERIC}, includeNullJdbcType = true)
public class IPTypeHandler implements TypeHandler<String> {
    @Override
    public void setParameter(PreparedStatement ps, int index, String ip, JdbcType jdbcType) throws SQLException {
        if (ip == null) {
            ps.setNull(index, Types.BIGINT);
        } else {
            ps.setLong(index, IpUtils.ipToLong(ip));//使用IPv4Util.ipToLong转换，在页面上IP作为条件查询时出错，需要关注下
        }
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        long l = rs.getLong(columnName);
        if (rs.wasNull()) {
            return null;
        }
        return IpUtils.longToIp(l);
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        long l = rs.getLong(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return IpUtils.longToIp(l);
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        long l = cs.getLong(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        return IpUtils.longToIp(l);
    }


}
