package com.musapp.musicapp.fragments.main_fragments.profile_menu_items;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;

public class AboutUsFragment extends Fragment {

    private TextView mEmailLink;
    private TextView mGitLink;
    public static final String EMAIL_LINK = "";
    public static final String GIT_LINK = "https://github.com/EddyMiH/MusicianNetwork";
    private SetToolBarAndNavigationBarState mSetToolBarAndNavigationBarState;

    public void setSetToolBarAndNavigationBarState(SetToolBarAndNavigationBarState setToolBarAndNavigationBarState) {
        mSetToolBarAndNavigationBarState = setToolBarAndNavigationBarState;
    }

    public AboutUsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        mEmailLink = view.findViewById(R.id.text_about_us_fragment_email_link);
        mGitLink = view.findViewById(R.id.text_about_us_fragment_git_link);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmailLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(EMAIL_LINK));
                startActivity(intent);
            }
        });
        mGitLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(GIT_LINK));
                startActivity(intent);
            }
        });
        mSetToolBarAndNavigationBarState.setTitle(R.string.title_about_us);
    }
}
