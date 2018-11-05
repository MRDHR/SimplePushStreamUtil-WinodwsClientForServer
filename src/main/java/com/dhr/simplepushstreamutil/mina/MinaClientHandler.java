package com.dhr.simplepushstreamutil.mina;


import com.dhr.simplepushstreamutil.bean.FromServerBean;
import com.dhr.simplepushstreamutil.ui.form.MainForm;
import com.dhr.simplepushstreamutil.util.ParseMessageUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.util.logging.Logger;

/**
 * 客户端业务处理逻辑
 *
 * @author aniyo blog: http://aniyo.iteye.com
 */
public class MinaClientHandler extends IoHandlerAdapter {
    private static final Logger LOG = Logger.getLogger("system");
    private ParseMessageUtil parseMessageUtil;

    public MinaClientHandler(MainForm mainForm) {
        parseMessageUtil = new ParseMessageUtil(mainForm);
    }

    // 当客户端连接进入时
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("incomming 客户端: " + session.getRemoteAddress());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        /**
         * 自定义异常处理， 要不然异常会被“吃掉”；
         */
        cause.printStackTrace();
    }

    // 当客户端发送消息到达时
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        FromServerBean fromServerBean = (FromServerBean) message;
        LOG.info("服务器返回的数据：" + fromServerBean.toString());
        parseMessageUtil.parse(fromServerBean);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("客户端与服务端断开连接.....");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out
                .println("one Client Connection" + session.getRemoteAddress());
    }

}