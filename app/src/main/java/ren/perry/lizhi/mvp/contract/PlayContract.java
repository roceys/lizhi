package ren.perry.lizhi.mvp.contract;

import java.util.List;

import ren.perry.lizhi.entity.Music;
import ren.perry.mvplibrary.base.BaseModel;
import ren.perry.mvplibrary.base.BasePresenter;
import ren.perry.mvplibrary.base.BaseView;

/**
 * @author perrywe
 * @date 2019-05-17
 * WeChat  917351143
 */
public interface PlayContract {

    interface View extends BaseView {
        void onMusicSuccess(List<Music> music);
    }

    interface Model extends BaseModel {
        List<Music> getMusicList(int size, int page);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getMusicList(int size, int page);
    }
}
