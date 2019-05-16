package ren.perry.lizhi.bean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author perrywe
 * @date 2019-05-16
 * WeChat  917351143
 */
@NoArgsConstructor
@Data
public class MusicBean {

    /**
     * msg : 成功
     * code : 1
     * data : {"size":1,"count":9,"albumId":1,"page":1,"list":[{"id":1,"name":"黑色信封","url":"http://yoerking.com/static/i/music/01.被禁忌的游戏(2004)/01黑色信封.mp3","pic":"http://img3.imgtn.bdimg.com/it/u=588116581,3757880076&fm=26&gp=0.jpg","singer":"李志","create_time":"2019-05-15 16:20:44.0"}]}
     */

    private String msg;
    private int code;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        /**
         * size : 1
         * count : 9
         * albumId : 1
         * page : 1
         * list : [{"id":1,"name":"黑色信封","url":"http://yoerking.com/static/i/music/01.被禁忌的游戏(2004)/01黑色信封.mp3","pic":"http://img3.imgtn.bdimg.com/it/u=588116581,3757880076&fm=26&gp=0.jpg","singer":"李志","create_time":"2019-05-15 16:20:44.0"}]
         */

        private int size;
        private int count;
        private int albumId;
        private int page;
        private List<ListBean> list;

        @NoArgsConstructor
        @Data
        public static class ListBean {
            /**
             * id : 1
             * name : 黑色信封
             * url : http://yoerking.com/static/i/music/01.被禁忌的游戏(2004)/01黑色信封.mp3
             * pic : http://img3.imgtn.bdimg.com/it/u=588116581,3757880076&fm=26&gp=0.jpg
             * singer : 李志
             * create_time : 2019-05-15 16:20:44.0
             */

            private int id;
            private String name;
            private String url;
            private String pic;
            private String singer;
            private String create_time;
        }
    }
}
