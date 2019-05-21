package ren.perry.lizhi.mvp.model;

import java.util.List;

import ren.perry.lizhi.dao.DaoManager;
import ren.perry.lizhi.dao.MusicDao;
import ren.perry.lizhi.entity.Music;
import ren.perry.lizhi.mvp.contract.PlayContract;

/**
 * @author perrywe
 * @date 2019-05-17
 * WeChat  917351143
 */
public class PlayModel implements PlayContract.Model {
    @Override
    public List<Music> getMusicList(int size, int page) {
        MusicDao dao = DaoManager.get().getDaoSession().getMusicDao();
        return dao.queryBuilder().limit(size).offset((page - 1) * size).list();
    }
}
