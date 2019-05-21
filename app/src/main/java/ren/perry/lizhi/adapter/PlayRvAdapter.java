package ren.perry.lizhi.adapter;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.apache.commons.lang3.StringUtils;

import ren.perry.lizhi.R;
import ren.perry.lizhi.entity.Music;
import ren.perry.lizhi.helper.AudioPlayer;

/**
 * @author perrywe
 * @date 2019-05-17
 * WeChat  917351143
 */
public class PlayRvAdapter extends BaseQuickAdapter<Music, BaseViewHolder> {
    private Activity activity;

    public PlayRvAdapter(Activity activity) {
        super(R.layout.item_play);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, Music item) {
        Music music = AudioPlayer.get(activity).getPlayMusic();
        boolean isVisiblePlaying = music != null && music.getM_id() == item.getM_id();

        String infoStr = item.getSinger();
        if (!StringUtils.isEmpty(item.getAlbum())) {
            infoStr += " - " + item.getAlbum();
        }
        String indexStr = String.valueOf(helper.getAdapterPosition() + 1);
        helper.setText(R.id.tvIndex, indexStr)
                .setText(R.id.tvName, item.getName())
                .setText(R.id.tvInfo, infoStr)

                .setVisible(R.id.tvIndex, !isVisiblePlaying)
                .setVisible(R.id.ivIndex, isVisiblePlaying)

                .addOnClickListener(R.id.rlItem, R.id.ibMore);

    }
}
