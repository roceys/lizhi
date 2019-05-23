package ren.perry.lizhi.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author perrywe
 * @date 2019-05-23
 * WeChat  917351143
 */
@NoArgsConstructor
@Data
public class AboutBean {

    /**
     * msg : 成功
     * code : 1
     * data : {"type":1,"content":"    《我爱南京》APP由个人开发者进行设计开发"}
     */

    private String msg;
    private int code;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        /**
         * type : 1
         * content :     《我爱南京》APP由个人开发者进行设计开发
         */

        private int type;
        private String content;
    }
}
