package com.dhr.simplepushstreamutil.util;

import com.dhr.simplepushstreamutil.bean.FromServerBean;
import com.dhr.simplepushstreamutil.bean.ResolutionBean;
import com.dhr.simplepushstreamutil.entity.LiveAreaListEntity;
import com.dhr.simplepushstreamutil.ui.form.MainForm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CommandUtil {
    private MainForm mainForm;
    private Gson gson;

    public CommandUtil(MainForm mainForm) {
        this.mainForm = mainForm;
        this.gson = new Gson();
    }

    public void getFormatList(FromServerBean fromServerBean) {
        int code = fromServerBean.getCode();
        if (0 == code) {
            List<ResolutionBean> resolutionBeans = gson.fromJson(fromServerBean.getResult(), new TypeToken<List<ResolutionBean>>() {
            }.getType());
            mainForm.getFormatListSuccess(resolutionBeans);
        } else {
            mainForm.getFormatListFail(fromServerBean.getResult());
        }
    }

    public void pushStreamToLiveRoom(FromServerBean fromServerBean) {
        int code = fromServerBean.getCode();
        if (0 == code) {
            mainForm.pushStreamToLiveRoomSuccess(fromServerBean.getResult());
        } else {
            mainForm.pushStreamToLiveRoomFail(fromServerBean.getResult());
        }
    }

    public void login(FromServerBean fromServerBean) {
        int code = fromServerBean.getCode();
        if (0 == code) {
            mainForm.getBilibiliAccountDialog().loginSuccess(fromServerBean.getResult());
        } else {
            mainForm.getBilibiliAccountDialog().loginFail(fromServerBean.getResult());
        }
    }

    public void saveLoginInfo(FromServerBean fromServerBean) {
        int code = fromServerBean.getCode();
        if (0 == code) {
            mainForm.getBilibiliAccountDialog().saveLoginInfoSuccess(fromServerBean.getResult());
        } else {
            mainForm.getBilibiliAccountDialog().saveLoginInfoFail(fromServerBean.getResult());
        }
    }

    public void removeLoginInfo(FromServerBean fromServerBean) {
        mainForm.getBilibiliAccountDialog().removeLoginInfoSuccess(fromServerBean.getResult());
    }

    public void openLiveRoom(FromServerBean fromServerBean) {
        mainForm.openLiveRoomFail(fromServerBean.getResult());
    }

    public void getAreaList(FromServerBean fromServerBean) {
        int code = fromServerBean.getCode();
        if (0 == code) {
            List<LiveAreaListEntity> resolutionBeans = gson.fromJson(fromServerBean.getResult(), new TypeToken<List<LiveAreaListEntity>>() {
            }.getType());
            mainForm.getAreaListSuccess(resolutionBeans);
        } else {
            mainForm.getAreaListFail(fromServerBean.getResult());
        }
    }

    public void updateTitleAndOpenLiveRoom(FromServerBean fromServerBean) {
        int code = fromServerBean.getCode();
        if (0 == code) {
            mainForm.updateTitleAndOpenLiveRoomSuccess(fromServerBean.getResult());
        } else {
            mainForm.updateTitleAndOpenLiveRoomFail(fromServerBean.getResult());
        }
    }

    public void closeLiveRoom(FromServerBean fromServerBean) {
        int code = fromServerBean.getCode();
        if (0 == code) {
            mainForm.closeLiveRoomSuccess(fromServerBean.getResult());
        } else {
            mainForm.closeLiveRoomFail(fromServerBean.getResult());
        }
    }

    public void toMyLIveRoom(FromServerBean fromServerBean) {
        int code = fromServerBean.getCode();
        if (0 == code) {
            mainForm.toMyLIveRoomSuccess(fromServerBean.getResult());
        } else {
            mainForm.toMyLIveRoomFail(fromServerBean.getResult());
        }
    }

    public void liveRoomIsOpen(FromServerBean fromServerBean) {
        int code = fromServerBean.getCode();
        if (0 == code) {
            mainForm.liveRoomIsOpenSuccess(fromServerBean.getResult());
        } else {
            mainForm.liveRoomIsOpenFail(fromServerBean.getResult());
        }
    }

    public void stopPushStream(FromServerBean fromServerBean) {
        mainForm.stopPushStreamSuccess(fromServerBean.getResult());
    }
}
