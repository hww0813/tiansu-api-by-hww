package com.yuanqing.common.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FtpUtil {

    /** 本地字符编码 */
    private static String LOCAL_CHARSET = "GBK";

    // FTP协议里面，规定文件名编码为iso-8859-1
    private static String SERVER_CHARSET = "ISO-8859-1";

    public static FTPClient connect(String ip, int port, String username, String password) throws Exception {

        FTPClient ftpClient = new FTPClient();
        ftpClient.enterLocalActiveMode();
        ftpClient.connect(ip, port);
        ftpClient.login(username, password);
        if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(
                "OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            LOCAL_CHARSET = "UTF-8";
        }
        ftpClient.setControlEncoding(LOCAL_CHARSET);
        return ftpClient;
    }

    public static void upload(File file, FTPClient ftpClient) throws Exception {

        String filePath = file.getPath();


        String[] str = null;

        if(SystemUtil.currSystem()){
            //liunx系统
            str = filePath.split("/");
        }else if(SystemUtil.macSystem()){
            //mac系统
            str = filePath.split("/");
        }else{
            str = str = filePath.split("\\\\");
        }

        String ftpPath = "";

        for (int i = str.length-3; i < str.length - 1; i++) {
            ftpClient.makeDirectory(str[i]);
            ftpClient.changeWorkingDirectory(str[i]);
        }



//        String[] str = filePath.split("/");
//        String ftpPath = "";
//        for (int i = 3; i < str.length - 1; i++) {
//            ftpClient.makeDirectory(str[i]);
//            ftpClient.changeWorkingDirectory(str[i]);
//        }

        InputStream inputStream = null;

        inputStream = new FileInputStream(file);
        ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
        ftpClient.storeFile(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"), inputStream);
        inputStream.close();
        ftpClient.logout();
    }

    public static void main(String[] args) {
        String filePath = "/user/local/tiansu/report/DayReport/20180824";
        String[] str = filePath.split("/");
        for (String s : str) {
            System.out.println(s);
        }
    }

}
