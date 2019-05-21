package ren.perry.lizhi.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author perrywe
 * @date 2019-05-17
 * WeChat  917351143
 */
@Data
@AllArgsConstructor
public class PlayActionEvent {
    public static final int ACTION_PLAY_ADD = 1001;         //播放新添加的
    public static final int ACTION_PLAY_LIST = 1002;        //从播放列表的
    public static final int ACTION_PLAY_PAUSE = 1003;       //暂停后的播放
    public static final int ACTION_PLAY_NEXT = 1004;        //下一曲
    public static final int ACTION_PLAY_PREV = 1005;        //上一曲
    public static final int ACTION_PAUSE = 1006;            //暂停
    public static final int ACTION_STOP = 1007;             //停止
    public static final int ACTION_DELETE = 1008;           //删除
    public static final int ACTION_DELETE_AND_NEXT = 1009;  //删除
    public static final int ACTION_PROGRESS = 1010;  //更新进度

    public PlayActionEvent(int action) {
        this.action = action;
    }

    private int action;

    private int totalProgress;
    private int currentProgress;
}
