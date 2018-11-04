package com.dhr.simplepushstreamutil.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JschUtil {
    private ChannelExec channelExec;
    private Session session = null;
    private int timeout = 60000;

    /**
     * 连接远程服务器
     *
     * @param host     ip地址
     * @param userName 登录名
     * @param password 密码
     * @param port     端口
     * @throws Exception
     */
    public void versouSshUtil(String host, String userName, String password, int port) throws Exception {
        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(userName, host, port); // 根据用户名，主机ip，端口获取一个Session对象
        session.setPassword(password); // 设置密码
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
    }

    /**
     * 在远程服务器上执行命令
     *
     * @param cmd     要执行的命令字符串
     * @param charset 编码
     * @throws Exception
     */
    public List<String> runCmd(String cmd, String charset) throws Exception {
        channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(cmd);
        channelExec.setInputStream(null);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        InputStream in = channelExec.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName(charset)));
        List<String> result = new ArrayList<>();
        String buf = null;
        while ((buf = reader.readLine()) != null) {
            System.out.println(buf);
            result.add(buf);
        }
        reader.close();
        channelExec.disconnect();
        return result;
    }

    public void close() {
        try {
            if (null != channelExec && channelExec.isConnected()) {
                channelExec.disconnect();
            }
            if (null != session && session.isConnected()) {
                session.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
