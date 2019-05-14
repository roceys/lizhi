package ren.perry.lizhi.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author perrywe
 * @date 2019-05-14
 * WeChat  917351143
 */
@NoArgsConstructor
@Data
public class SplashBean {


    /**
     * msg : 成功
     * code : 1
     * data : {"url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557831256573&di=4c47987d18b6e7d89c826b4de06445bb&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170220%2Fba7b0f5bcb0b4028b12cb10a7c48f850_th.jpeg"}
     */

    private String msg;
    private int code;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        /**
         * url : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557831256573&di=4c47987d18b6e7d89c826b4de06445bb&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170220%2Fba7b0f5bcb0b4028b12cb10a7c48f850_th.jpeg
         */

        private String url;
    }
}
