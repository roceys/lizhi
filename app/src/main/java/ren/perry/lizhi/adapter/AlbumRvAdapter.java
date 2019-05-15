package ren.perry.lizhi.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import ren.perry.lizhi.R;
import ren.perry.lizhi.bean.AlbumBean;
import ren.perry.mvplibrary.utils.GlideMan;

/**
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
public class AlbumRvAdapter extends BaseQuickAdapter<AlbumBean.DataBean.ListBean, BaseViewHolder> {
    private GridLayoutManager lm;

    public AlbumRvAdapter(GridLayoutManager lm) {
        super(R.layout.item_album);
        this.lm = lm;
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumBean.DataBean.ListBean item) {
        ImageView iv = helper.getView(R.id.iv);
        ViewGroup.LayoutParams params = iv.getLayoutParams();
        params.height = lm.getWidth() / lm.getSpanCount() - 2 * iv.getPaddingLeft() - 2
                * ((ViewGroup.MarginLayoutParams) params).leftMargin;
        iv.setLayoutParams(params);

        new GlideMan.Builder()
                .load(item.getImg())
                .into(iv);

        helper.setText(R.id.tv, item.getName())
                .addOnClickListener(R.id.cv);
    }
}
