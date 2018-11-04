package com.dhr.simplepushstreamutil.entity;

import java.util.List;

public class LiveAreaListEntity {

    /**
     * id : 1
     * name : 娱乐
     * list : [{"id":"21","parent_id":"1","old_area_id":"10","name":"视频唱见","act_id":"0","pk_status":"1","hot_status":1,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/72b93ddafdf63c9f0b626ad546847a3c03c92b6f.png","pinyin":"shipinchangjian","cate_id":"12","parent_name":"娱乐"},{"id":"30","parent_id":"1","old_area_id":"2","name":"视频催眠","act_id":"0","pk_status":"0","hot_status":1,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/f377fec718bb110529a9566f55683003cad45751.png","pinyin":"shipincuimian","cate_id":"1","parent_name":"娱乐"},{"id":"145","parent_id":"1","old_area_id":"6","name":"视频聊天","act_id":"0","pk_status":"1","hot_status":1,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/14a8c9c6d0a7685091db270cb523690b9e78b523.png","pinyin":"shipinliaotian","cate_id":"2","parent_name":"娱乐"},{"id":"160","parent_id":"1","old_area_id":"10","name":"唱见电台","act_id":"0","pk_status":"0","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/d22d7fafbf9b24e2bc3ce1df5eb9f006e6035e5d.png","pinyin":"changjiandiantai","cate_id":"12","parent_name":"娱乐"},{"id":"161","parent_id":"1","old_area_id":"2","name":"催眠电台","act_id":"0","pk_status":"0","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/d59c86b0232bcd1b405bb2220a462dee7bfe5891.png","pinyin":"cuimiandiantai","cate_id":"1","parent_name":"娱乐"},{"id":"162","parent_id":"1","old_area_id":"6","name":"聊天电台","act_id":"0","pk_status":"0","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/a95b25ebec1e009870c2e3dd823e8cae50fa5223.png","pinyin":"liaotiandiantai","cate_id":"2","parent_name":"娱乐"},{"id":"177","parent_id":"1","old_area_id":"6","name":"晚安电台","act_id":"0","pk_status":"0","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/1c16405510fe0d1d37b7e56ef194bd135d6ebb53.png","pinyin":"wanandiantai","parent_name":"娱乐"},{"id":"134","parent_id":"1","old_area_id":"2","name":"声优","act_id":"61","pk_status":"1","hot_status":0,"lock_status":"1","pic":"https://i0.hdslb.com/bfs/vc/b3197172e0309103233ac2a333156f2304ab6478.png","pinyin":"shengyou","cate_id":"12","parent_name":"娱乐"},{"id":"143","parent_id":"1","old_area_id":"2","name":"才艺","act_id":"0","pk_status":"1","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/a7a2cfad137c0db0d61aece9da2f77167466db64.png","pinyin":"caiyi","cate_id":"12","parent_name":"娱乐"},{"id":"136","parent_id":"1","old_area_id":"6","name":"美食","act_id":"0","pk_status":"1","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/a3580fcae212085cb2950b82b590caeaebedda81.png","pinyin":"meishi","cate_id":"2","parent_name":"娱乐"},{"id":"123","parent_id":"1","old_area_id":"6","name":"户外","act_id":"0","pk_status":"1","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/a97daf9b1c6d16900495fab1237d8218667920c1.png","pinyin":"huwai","cate_id":"2","parent_name":"娱乐"},{"id":"25","parent_id":"1","old_area_id":"2","name":"手工","act_id":"0","pk_status":"1","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/56fa9416b41b4aa3620749d16c1b1161c45dc0df.png","pinyin":"shougong","cate_id":"1","parent_name":"娱乐"},{"id":"28","parent_id":"1","old_area_id":"6","name":"萌宠","act_id":"0","pk_status":"0","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/b6814fc82bf62dd915e2b284e890770969c8ccd5.png","pinyin":"mengchong","cate_id":"2","parent_name":"娱乐"},{"id":"27","parent_id":"1","old_area_id":"6","name":"学习","act_id":"0","pk_status":"0","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/d5034f128ed95acf447e903c8082c9c5b6bd7271.png","pinyin":"xuexi","cate_id":"2","parent_name":"娱乐"},{"id":"33","parent_id":"1","old_area_id":"7","name":"映评馆","act_id":"0","pk_status":"0","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/44b85dd5ead7a02dd5d3396972811cd610793ae6.png","pinyin":"yingpingguan","cate_id":"3","parent_name":"娱乐"},{"id":"34","parent_id":"1","old_area_id":"7","name":"音乐台","act_id":"0","pk_status":"0","hot_status":0,"lock_status":"0","pic":"https://i0.hdslb.com/bfs/vc/8537694f4fe68ab0798dd5d493d3ca5deb908088.png","pinyin":"yinyuetai","cate_id":"3","parent_name":"娱乐"}]
     */

    private int id;
    private String name;
    private List<ListBean> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 21
         * parent_id : 1
         * old_area_id : 10
         * name : 视频唱见
         * act_id : 0
         * pk_status : 1
         * hot_status : 1
         * lock_status : 0
         * pic : https://i0.hdslb.com/bfs/vc/72b93ddafdf63c9f0b626ad546847a3c03c92b6f.png
         * pinyin : shipinchangjian
         * cate_id : 12
         * parent_name : 娱乐
         */

        private String id;
        private String parent_id;
        private String old_area_id;
        private String name;
        private String act_id;
        private String pk_status;
        private int hot_status;
        private String lock_status;
        private String pic;
        private String pinyin;
        private String cate_id;
        private String parent_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getOld_area_id() {
            return old_area_id;
        }

        public void setOld_area_id(String old_area_id) {
            this.old_area_id = old_area_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAct_id() {
            return act_id;
        }

        public void setAct_id(String act_id) {
            this.act_id = act_id;
        }

        public String getPk_status() {
            return pk_status;
        }

        public void setPk_status(String pk_status) {
            this.pk_status = pk_status;
        }

        public int getHot_status() {
            return hot_status;
        }

        public void setHot_status(int hot_status) {
            this.hot_status = hot_status;
        }

        public String getLock_status() {
            return lock_status;
        }

        public void setLock_status(String lock_status) {
            this.lock_status = lock_status;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getCate_id() {
            return cate_id;
        }

        public void setCate_id(String cate_id) {
            this.cate_id = cate_id;
        }

        public String getParent_name() {
            return parent_name;
        }

        public void setParent_name(String parent_name) {
            this.parent_name = parent_name;
        }
    }
}
