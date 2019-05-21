package ren.perry.lizhi.mvp.presenter;

import ren.perry.lizhi.mvp.contract.PlayContract;
import ren.perry.lizhi.mvp.model.PlayModel;

/**
 * @author perrywe
 * @date 2019-05-17
 * WeChat  917351143
 */
public class PlayPresenter extends PlayContract.Presenter {

    public PlayPresenter(PlayContract.View view) {
        mView = view;
        mModel = new PlayModel();
    }

    @Override
    public void getMusicList(int size, int page) {
        mView.onMusicSuccess(mModel.getMusicList(size, page));
    }
}
