package com.yuanqing.common.utils.ip;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;

import com.yuanqing.common.constant.RegexConstants;
import com.yuanqing.common.utils.StringUtils;

/**
 * 获取IP方法
 *
 * @author yuanqing
 */
public class IpUtils
{
    public static String getIpAddr(HttpServletRequest request){
        if (request == null)
        {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    public static boolean internalIp(String ip) {
        byte[] addr = textToNumericFormatV4(ip);
        return internalIp(addr) || "127.0.0.1".equals(ip);
    }

    private static boolean internalIp(byte[] addr) {
        if (StringUtils.isNull(addr) || addr.length < 2)
        {
            return true;
        }
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0)
        {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4)
                {
                    return true;
                }
            case SECTION_5:
                switch (b1)
                {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;
        }
    }

    public static Long ipToLong(String ipAddr) {

        if (ipAddr == null || "".equalsIgnoreCase(ipAddr)) {
            return null;
        }
        if (ipAddr.matches(RegexConstants.NUM)){
            return Long.parseLong(ipAddr);
        }
        Long[] ip = new Long[4];
        //先找到IP地址字符串中.的位置
        int position1 = ipAddr.indexOf(".");
        int position2 = ipAddr.indexOf(".", position1 + 1);
        int position3 = ipAddr.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(ipAddr.substring(0, position1).trim());
        ip[1] = Long.parseLong(ipAddr.substring(position1 + 1, position2).trim());
        ip[2] = Long.parseLong(ipAddr.substring(position2 + 1, position3).trim());
        ip[3] = Long.parseLong(ipAddr.substring(position3 + 1).trim());
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 把int->ip地址
     *
     * @param ipInt 数字型ip
     * @return String
     */
    public static String longToIp(long ipInt) {
        return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.')
                .append((ipInt >> 16) & 0xff).append('.').append(
                        (ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff))
                .toString();
    }

    /**
     * 将IPv4地址转换成字节
     *
     * @param text IPv4地址
     * @return byte 字节
     */
    public static byte[] textToNumericFormatV4(String text)
    {
        if (text.length() == 0)
        {
            return null;
        }

        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try
        {
            long l;
            int i;
            switch (elements.length)
            {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L))
                        return null;
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L))
                        return null;
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L))
                        return null;
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i)
                    {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L))
                            return null;
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L))
                        return null;
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i)
                    {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L))
                            return null;
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        }
        catch (NumberFormatException e)
        {
            return null;
        }
        return bytes;
    }

    public static String getHostIp()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
        }
        return "127.0.0.1";
    }

    public static String getHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
        }
        return "未知";
    }

    //将十进制整数形式转换成127.0.0.1形式的IP地址
    public static String longToIPv4(Long longIp) {
        if (null == longIp || longIp == 0L) {
            return "";
        }
        StringBuffer sb = new StringBuffer("");
        //直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        //将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        //将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }

    //将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
    public static Long IPv4ToLong(String strIp) {
        if (strIp == null || "".equalsIgnoreCase(strIp)) {
            return null;
        }
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1).trim());
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2).trim());
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3).trim());
        ip[3] = Long.parseLong(strIp.substring(position3 + 1).trim());
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static long getIp2long(String ip){
        ip = ip.trim();
        String[] ips = ip.split("\\.");
        long ip2long = 0L;
        for(int i = 0; i < 4; ++i){
            ip2long = ip2long << 8 | Integer.parseInt(ips[i]);
        }

        return ip2long;
    }
}
