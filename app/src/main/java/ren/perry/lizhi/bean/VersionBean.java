package ren.perry.lizhi.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
@NoArgsConstructor
@Data
public class VersionBean {

    /**
     * msg : 成功
     * code : 1
     * data : {"is_force":0,"version_code":3,"title":"发现新版本","content":"测试更新","url":"https://cdn.perry.ren/Tadpole_v2.1.0_perry_20190129_release.apk"}
     */

    private String msg;
    private int code;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        /**
         * is_force : 0
         * version_code : 3
         * title : 发现新版本
         * content : 测试更新
         * url : https://cdn.perry.ren/Tadpole_v2.1.0_perry_20190129_release.apk
         */

        private int is_force;
        private int version_code;
        private String title;
        private String content;
        private String url;
    }
}
