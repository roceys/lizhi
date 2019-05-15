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
public class AlbumBean {

    /**
     * msg : 成功
     * code : 1
     * data : {"size":1,"count":37,"page":1,"list":[{"id":1,"name":"被禁忌的游戏(2004)","img":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557916203394&di=69ef30df8cd71e47c7d6900f0ea62e5c&imgtype=0&src=http%3A%2F%2Fimg1.doubanio.com%2Fview%2Fphoto%2Fl%2Fpublic%2Fp218478"}]}
     */

    private String msg;
    private int code;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        /**
         * size : 1
         * count : 37
         * page : 1
         * list : [{"id":1,"name":"被禁忌的游戏(2004)","img":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557916203394&di=69ef30df8cd71e47c7d6900f0ea62e5c&imgtype=0&src=http%3A%2F%2Fimg1.doubanio.com%2Fview%2Fphoto%2Fl%2Fpublic%2Fp218478"}]
         */

        private int size;
        private int count;
        private int page;
        private List<ListBean> list;

        @NoArgsConstructor
        @Data
        public static class ListBean {
            /**
             * id : 1
             * name : 被禁忌的游戏(2004)
             * img : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557916203394&di=69ef30df8cd71e47c7d6900f0ea62e5c&imgtype=0&src=http%3A%2F%2Fimg1.doubanio.com%2Fview%2Fphoto%2Fl%2Fpublic%2Fp218478
             */

            private int id;
            private String name;
            private String img;
        }
    }
}
