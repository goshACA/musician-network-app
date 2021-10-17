package com.musapp.musicapp.fragments.main_fragments.view_pager_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.adapters.FeedRecyclerAdapter;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.model.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class UserPostPagerFragment extends Fragment {

    RecyclerView recyclerView;
    private AppMainActivity.ClickListener mClickListener;
    private AppMainActivity.MusicPlayerServiceConnection mPlayerServiceConnection;

    public void setPlayerServiceConnection(AppMainActivity.MusicPlayerServiceConnection playerServiceConnection) {
        mPlayerServiceConnection = playerServiceConnection;
    }

    public void setClickListener(AppMainActivity.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    private FeedRecyclerAdapter.FragmentTransactionListener mTransactionListener;

    public void setTransactionListener(FeedRecyclerAdapter.FragmentTransactionListener transactionListener) {
        mTransactionListener = transactionListener;
    }

    private FeedRecyclerAdapter.OnUserImageListener mOnUserImageListener = new FeedRecyclerAdapter.OnUserImageListener() {
        @Override
        public void onProfileImageClickListener(Post post) {
            mClickListener.userImageClickListener(post);
        }
    };

    private FeedRecyclerAdapter.OnItemSelectedListener mOnItemSelectedListener =
            new FeedRecyclerAdapter.OnItemSelectedListener() {
                @Override
                public void onItemSelected(Post post) {
                    mClickListener.postClickListener(post);
                }
            };

    private FeedRecyclerAdapter postRecyclerAdapter = new FeedRecyclerAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_user_posts_pager, container, false);
        recyclerView = view.findViewById(R.id.recycler_fragment_profile_user_posts);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUserPosts();
        initRecyclerView();

    }

    private void initRecyclerView(){
        postRecyclerAdapter.setOnUserImageListener(mOnUserImageListener);
        postRecyclerAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        postRecyclerAdapter.setTransactionListener(mTransactionListener);
        postRecyclerAdapter.setPlayerServiceConnection(mPlayerServiceConnection);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(postRecyclerAdapter);
    }

    private void loadUserPosts(){
        final List<Post> posts = new ArrayList<>();
        for(final String postId: CurrentUser.getCurrentUser().getPostId()){
            FirebaseRepository.getPostById(postId, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    posts.add(dataSnapshot.getValue(Post.class));
                    Log.d("ggggg", CurrentUser.getCurrentUser().getFavouritePostId().toString());
                    if(posts.size() == CurrentUser.getCurrentUser().getPostId().size()){
                        postRecyclerAdapter.setData(posts);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public UserPostPagerFragment() {
    }
}
