package com.dhr.simplepushstreamutil.util;

import com.dhr.simplepushstreamutil.bean.FromServerBean;
import com.dhr.simplepushstreamutil.ui.form.MainForm;
import com.google.gson.Gson;
import org.apache.mina.core.session.IoSession;

public class ParseMessageUtil {
    public static final int TYPE_OPENPORT = 0;
    public static final int TYPE_GETFORMATLIST = 1;
    public static final int TYPE_GETM3U8 = 2;
    public static final int TYPE_PUSHSTREAMTOLIVEROOM = 3;
    public static final int TYPE_LOGIN = 4;
    public static final int TYPE_SAVELOGININFO = 5;
    public static final int TYPE_REMOVELOGININFO = 6;
    public static final int TYPE_GETAREALIST = 7;
    public static final int TYPE_UPDATETITLEANDOPENLIVEROOM = 8;
    public static final int TYPE_OPENLIVEROOM = 9;
    public static final int TYPE_CLOSELIVEROOM = 10;
    public static final int TYPE_TOMYLIVEROOM = 11;
    public static final int TYPE_LIVEROOMISOPEN = 12;
    public static final int TYPE_STOPPUSHSTREAM = 13;
    private Gson gson;
    private CommandUtil commandUtil;

    public ParseMessageUtil(MainForm mainForm, IoSession session) {
        gson = new Gson();
        commandUtil = new CommandUtil(mainForm, session);
    }

    /**
     * 解析数据并分发操作
     *
     * @param fromServerBean
     */
    public void parse(FromServerBean fromServerBean) {
        if (null != fromServerBean) {
            switch (fromServerBean.getType()) {
                case TYPE_OPENPORT:
//                    commandUtil.openPort();
                    break;
                case TYPE_GETFORMATLIST:
                    commandUtil.getFormatList(fromServerBean);
                    break;
                case TYPE_GETM3U8:
                    commandUtil.getM3u8Url(fromServerBean);
                    break;
                case TYPE_PUSHSTREAMTOLIVEROOM:
                    commandUtil.pushStreamToLiveRoom(fromServerBean);
                    break;
                case TYPE_LOGIN:
                    commandUtil.login(fromServerBean);
                    break;
                case TYPE_SAVELOGININFO:
                    commandUtil.saveLoginInfo(fromServerBean);
                    break;
                case TYPE_REMOVELOGININFO:
                    commandUtil.removeLoginInfo(fromServerBean);
                    break;
                case TYPE_GETAREALIST:
                    commandUtil.getAreaList(fromServerBean);
                    break;
                case TYPE_UPDATETITLEANDOPENLIVEROOM:
                    commandUtil.updateTitleAndOpenLiveRoom(fromServerBean);
                    break;
                case TYPE_OPENLIVEROOM:
                    commandUtil.openLiveRoom(fromServerBean);
                    break;
                case TYPE_CLOSELIVEROOM:
                    commandUtil.closeLiveRoom(fromServerBean);
                    break;
                case TYPE_TOMYLIVEROOM:
                    commandUtil.toMyLIveRoom(fromServerBean);
                    break;
                case TYPE_LIVEROOMISOPEN:
                    commandUtil.liveRoomIsOpen(fromServerBean);
                    break;
                case TYPE_STOPPUSHSTREAM:
                    commandUtil.stopPushStream(fromServerBean);
                    break;
            }
        }
    }

}