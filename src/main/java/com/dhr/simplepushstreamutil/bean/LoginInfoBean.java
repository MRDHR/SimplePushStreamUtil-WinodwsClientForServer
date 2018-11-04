package com.dhr.simplepushstreamutil.bean;

public class LoginInfoBean {
    /**
     * status : true
     * ts : 1537282098
     * data : {"url":"https://passport.bilibili.com/qrcode/h5/login?oauthKey=7fa085a031b05f48b403531f937c499e","oauthKey":"7fa085a031b05f48b403531f937c499e"}
     */

    private boolean status;
    private int ts;
    private DataBean data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * url : https://passport.bilibili.com/qrcode/h5/login?oauthKey=7fa085a031b05f48b403531f937c499e
         * oauthKey : 7fa085a031b05f48b403531f937c499e
         */

        private String url;
        private String oauthKey;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getOauthKey() {
            return oauthKey;
        }

        public void setOauthKey(String oauthKey) {
            this.oauthKey = oauthKey;
        }
    }
}
