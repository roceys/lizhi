package ren.perry.lizhi.adapter;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import ren.perry.lizhi.R;
import ren.perry.lizhi.bean.MusicBean;
import ren.perry.lizhi.entity.Music;
import ren.perry.lizhi.helper.AudioPlayer;

/**
 * @author perrywe
 * @date 2019-05-16
 * WeChat  917351143
 */
public class MusicRvAdapter extends BaseQuickAdapter<MusicBean.DataBean.ListBean, BaseViewHolder> {
    private Activity activity;
    private String albumName;

    public MusicRvAdapter(Activity activity, String albumName) {
        super(R.layout.item_music);
        this.activity = activity;
        this.albumName = albumName;
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicBean.DataBean.ListBean item) {

        Music music = AudioPlayer.get(activity).getPlayMusic();
        boolean isVisiblePlaying = music != null && music.getM_id() == item.getId();

        String infoStr = item.getSinger() + " - " + albumName;
        String indexStr = String.valueOf(helper.getAdapterPosition() + 1);
        helper.setText(R.id.tvIndex, indexStr)
                .setText(R.id.tvName, item.getName())
                .setText(R.id.tvInfo, infoStr)

                .setVisible(R.id.tvIndex, !isVisiblePlaying)
                .setVisible(R.id.ivIndex, isVisiblePlaying)

                .addOnClickListener(R.id.rlItem, R.id.ibMore);
    }
}
