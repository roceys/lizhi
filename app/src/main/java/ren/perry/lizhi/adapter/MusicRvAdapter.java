package ren.perry.lizhi.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import ren.perry.lizhi.R;
import ren.perry.lizhi.bean.MusicBean;

/**
 * @author perrywe
 * @date 2019-05-16
 * WeChat  917351143
 */
public class MusicRvAdapter extends BaseQuickAdapter<MusicBean.DataBean.ListBean, BaseViewHolder> {
    private String albumName;

    public MusicRvAdapter(String albumName) {
        super(R.layout.item_music);
        this.albumName = albumName;
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicBean.DataBean.ListBean item) {

        boolean isVisibleIndex = true;

        String infoStr = item.getSinger() + " - " + albumName;
        String indexStr = String.valueOf(helper.getAdapterPosition() + 1);
        helper.setText(R.id.tvIndex, indexStr)
                .setText(R.id.tvName, item.getName())
                .setText(R.id.tvInfo, infoStr)
                .setVisible(R.id.tvIndex, isVisibleIndex)
                .setVisible(R.id.ivIndex, !isVisibleIndex);

    }
}
