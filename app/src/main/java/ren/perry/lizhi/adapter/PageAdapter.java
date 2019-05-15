package ren.perry.lizhi.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import lombok.Setter;

/**
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
public class PageAdapter extends FragmentPagerAdapter {

    @Setter
    private List<Fragment> fragments;

    @Setter
    private List<String> titles;

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
