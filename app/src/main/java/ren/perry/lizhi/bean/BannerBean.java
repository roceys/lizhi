package ren.perry.lizhi.bean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
@NoArgsConstructor
@Data
public class BannerBean {


    /**
     * msg : 成功
     * code : 1
     * data : {"list":[{"title":"反正我早已习惯不去相信","img":"https://cdn.perry.ren/banner_1.jpg","url":"https://mp.weixin.qq.com/s/NC5fVMILy6hLf3yRz45zcA"},{"title":"中国音乐节不完全指南","img":"https://cdn.perry.ren/banner_2.jpg","url":"https://mp.weixin.qq.com/s/MlpkJWdp2oo6hcRDxGl2yA"}]}
     */

    private String msg;
    private int code;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private List<ListBean> list;

        @NoArgsConstructor
        @Data
        public static class ListBean {
            /**
             * title : 反正我早已习惯不去相信
             * img : https://cdn.perry.ren/banner_1.jpg
             * url : https://mp.weixin.qq.com/s/NC5fVMILy6hLf3yRz45zcA
             */

            private String title;
            private String img;
            private String url;
        }
    }
}
