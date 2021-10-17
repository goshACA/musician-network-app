package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.adapters.FullScreenPagerAdapter;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;

import java.util.ArrayList;
import java.util.List;

public class FullScreenImageFragment extends Fragment {

    private List<String> mImageUrls;
    private ViewPager mViewPager;
    private int mPosition;
    public static final String IMAGE_DATA = "image_urls";
    public static final String IMAGE_POSITION = "image_position";

    private SetToolBarAndNavigationBarState mSetToolBarAndNavigationBarState;

    public void setSetToolBarAndNavigationBarState(SetToolBarAndNavigationBarState setToolBarAndNavigationBarState) {
        mSetToolBarAndNavigationBarState = setToolBarAndNavigationBarState;
    }

    public void setImageUrls(List<String> imageUrls) {
        mImageUrls = imageUrls;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_screen_image, container, false);
        mViewPager = view.findViewById(R.id.view_pager_full_screen_image_fragment);
        if(getArguments() != null){
            mImageUrls = getArguments().getStringArrayList(IMAGE_DATA);
            mPosition = getArguments().getInt(IMAGE_POSITION);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FullScreenPagerAdapter adapter = new FullScreenPagerAdapter(getContext(), mImageUrls);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mPosition);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSetToolBarAndNavigationBarState.setTitle("Photo");
        mSetToolBarAndNavigationBarState.hideNavigationBar();
        mSetToolBarAndNavigationBarState.showBackArrow(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        mSetToolBarAndNavigationBarState.showBackArrow(false);
        mSetToolBarAndNavigationBarState.showNavigationBar();
    }

    public FullScreenImageFragment() {
        mImageUrls = new ArrayList<>();
    }
}
