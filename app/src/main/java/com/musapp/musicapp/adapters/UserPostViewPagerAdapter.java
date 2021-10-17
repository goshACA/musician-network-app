package com.musapp.musicapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.fragments.main_fragments.view_pager_fragments.UserFavoritePostPagerFragment;
import com.musapp.musicapp.fragments.main_fragments.view_pager_fragments.UserPostPagerFragment;

public class UserPostViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private int totalTabs;
    private AppMainActivity.ClickListener mClickListener;
    private FeedRecyclerAdapter.FragmentTransactionListener mTransactionListener;
    private AppMainActivity.MusicPlayerServiceConnection mPlayerServiceConnection;

    public void setPlayerServiceConnection(AppMainActivity.MusicPlayerServiceConnection playerServiceConnection) {
        mPlayerServiceConnection = playerServiceConnection;
    }

    public void setTransactionListener(FeedRecyclerAdapter.FragmentTransactionListener transactionListener) {
        mTransactionListener = transactionListener;
    }

    public void setClickListener(AppMainActivity.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public UserPostViewPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        mContext = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                //TODO open fragment with recycler view, where load user posts and showToolBar in it
                UserPostPagerFragment userPostPagerFragment = new UserPostPagerFragment();
                userPostPagerFragment.setClickListener(mClickListener);
                userPostPagerFragment.setTransactionListener(mTransactionListener);
                userPostPagerFragment.setPlayerServiceConnection(mPlayerServiceConnection);
                return userPostPagerFragment;
            case 1:
                //TODO open fragment with recycler view, where load user favorites posts and showToolBar in it
                UserFavoritePostPagerFragment userFavoritePostPagerFragment = new UserFavoritePostPagerFragment();
                userFavoritePostPagerFragment.setClickListener(mClickListener);
                userFavoritePostPagerFragment.setTransactionListener(mTransactionListener);
                userFavoritePostPagerFragment.setPlayerServiceConnection(mPlayerServiceConnection);
                return userFavoritePostPagerFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
