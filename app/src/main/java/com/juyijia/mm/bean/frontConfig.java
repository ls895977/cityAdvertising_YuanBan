package com.juyijia.mm.bean;

import java.util.List;

public class frontConfig {


    /**
     * ret : 200
     * data : {"code":1,"msg":"获取成功","info":[{"id":"32","module":"banner","link":"www.baidu.com","icon":"/data/upload/20200425/5ea39d4f74b10.png","type":"1","sort":"100"},{"id":"30","module":"超市","link":"https://discuz.juyijia.cn/plugin.php?id=tom_tcmall&site=1&mod=index","icon":"/data/upload/20200424/5ea2ad3858c51.png","type":"2","sort":"10"},{"id":"29","module":"外卖","link":"https://waimai.juyijia.cn/addons/we7_wmall/template/vue/index.html?menu=#/pages/home/index?i=2","icon":"/data/upload/20200424/5ea2ad5118722.png","type":"2","sort":"9"},{"id":"28","module":"直播","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ad7529e24.png","type":"2","sort":"8"},{"id":"27","module":"蔬菜","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ad9273839.png","type":"2","sort":"7"},{"id":"26","module":"水果","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2adb0028f9.png","type":"2","sort":"6"},{"id":"25","module":"鲜花礼品","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2adcab1241.png","type":"2","sort":"5"},{"id":"24","module":"家居装修","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ae225d1b8.png","type":"2","sort":"4"},{"id":"33","module":"招聘求职","link":"www.xiaomi.com","icon":"/data/upload/20200425/5ea39de75865e.png","type":"3","sort":"4"},{"id":"23","module":"烘焙蛋糕","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ae3868898.png","type":"2","sort":"3"},{"id":"34","module":"名医在线","link":"www.baidu.com","icon":"/data/upload/20200425/5ea39e0b1ccb3.png","type":"3","sort":"3"},{"id":"37","module":"红包套餐","link":"www.baidu.com","icon":"/data/upload/20200425/5ea3a3b83ed02.png","type":"4","sort":"3"},{"id":"22","module":"酒店住宿","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ae4dde933.png","type":"2","sort":"2"},{"id":"35","module":"家政维修","link":"www.baicu.com","icon":"/data/upload/20200425/5ea39e283400f.png","type":"2","sort":"2"},{"id":"38","module":"限时抢券","link":"www.baidu.com","icon":"/data/upload/20200425/5ea3a3d8aad57.png","type":"4","sort":"2"},{"id":"21","module":"旅游","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ae6e6dfbe.png","type":"2","sort":"1"},{"id":"36","module":"房屋租赁","link":"www.baidu.com","icon":"/data/upload/20200425/5ea39e407c063.png","type":"2","sort":"1"},{"id":"39","module":"0元专区","link":"www.baidu.com","icon":"/data/upload/20200425/5ea3a3f3f38e6.png","type":"4","sort":"1"}],"good_link":{"link":"https://waimai.juyijia.cn/addons/we7_wmall/template/vue/index.html?menu=#/pages/home/index?i=2"}}
     * msg :
     */

    private int ret;
    private DataBean data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * code : 1
         * msg : 获取成功
         * info : [{"id":"32","module":"banner","link":"www.baidu.com","icon":"/data/upload/20200425/5ea39d4f74b10.png","type":"1","sort":"100"},{"id":"30","module":"超市","link":"https://discuz.juyijia.cn/plugin.php?id=tom_tcmall&site=1&mod=index","icon":"/data/upload/20200424/5ea2ad3858c51.png","type":"2","sort":"10"},{"id":"29","module":"外卖","link":"https://waimai.juyijia.cn/addons/we7_wmall/template/vue/index.html?menu=#/pages/home/index?i=2","icon":"/data/upload/20200424/5ea2ad5118722.png","type":"2","sort":"9"},{"id":"28","module":"直播","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ad7529e24.png","type":"2","sort":"8"},{"id":"27","module":"蔬菜","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ad9273839.png","type":"2","sort":"7"},{"id":"26","module":"水果","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2adb0028f9.png","type":"2","sort":"6"},{"id":"25","module":"鲜花礼品","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2adcab1241.png","type":"2","sort":"5"},{"id":"24","module":"家居装修","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ae225d1b8.png","type":"2","sort":"4"},{"id":"33","module":"招聘求职","link":"www.xiaomi.com","icon":"/data/upload/20200425/5ea39de75865e.png","type":"3","sort":"4"},{"id":"23","module":"烘焙蛋糕","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ae3868898.png","type":"2","sort":"3"},{"id":"34","module":"名医在线","link":"www.baidu.com","icon":"/data/upload/20200425/5ea39e0b1ccb3.png","type":"3","sort":"3"},{"id":"37","module":"红包套餐","link":"www.baidu.com","icon":"/data/upload/20200425/5ea3a3b83ed02.png","type":"4","sort":"3"},{"id":"22","module":"酒店住宿","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ae4dde933.png","type":"2","sort":"2"},{"id":"35","module":"家政维修","link":"www.baicu.com","icon":"/data/upload/20200425/5ea39e283400f.png","type":"2","sort":"2"},{"id":"38","module":"限时抢券","link":"www.baidu.com","icon":"/data/upload/20200425/5ea3a3d8aad57.png","type":"4","sort":"2"},{"id":"21","module":"旅游","link":"www.baidu.com","icon":"/data/upload/20200424/5ea2ae6e6dfbe.png","type":"2","sort":"1"},{"id":"36","module":"房屋租赁","link":"www.baidu.com","icon":"/data/upload/20200425/5ea39e407c063.png","type":"2","sort":"1"},{"id":"39","module":"0元专区","link":"www.baidu.com","icon":"/data/upload/20200425/5ea3a3f3f38e6.png","type":"4","sort":"1"}]
         * good_link : {"link":"https://waimai.juyijia.cn/addons/we7_wmall/template/vue/index.html?menu=#/pages/home/index?i=2"}
         */

        private int code;
        private String msg;
        private GoodLinkBean good_link;
        private List<InfoBean> info;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public GoodLinkBean getGood_link() {
            return good_link;
        }

        public void setGood_link(GoodLinkBean good_link) {
            this.good_link = good_link;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class GoodLinkBean {
            /**
             * link : https://waimai.juyijia.cn/addons/we7_wmall/template/vue/index.html?menu=#/pages/home/index?i=2
             */

            private String link;

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }
        }

        public static class InfoBean {
            /**
             * id : 32
             * module : banner
             * link : www.baidu.com
             * icon : /data/upload/20200425/5ea39d4f74b10.png
             * type : 1
             * sort : 100
             */

            private String id;
            private String module;
            private String link;
            private String icon;
            private String type;
            private String sort;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getModule() {
                return module;
            }

            public void setModule(String module) {
                this.module = module;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }
        }
    }
}
