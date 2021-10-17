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
import com.musapp.musicapp.adapters.viewholders.FeedViewHolder;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.utils.GlideUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserFavoritePostPagerFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_user_profile_user_favorites_posts_pager, container, false);
        recyclerView = view.findViewById(R.id.recycler_fragment_profile_user_favorites_posts);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUserFavoritePosts();
        initRecyclerView();
    }

    private void initRecyclerView(){
        postRecyclerAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        postRecyclerAdapter.setOnUserImageListener(mOnUserImageListener);
        postRecyclerAdapter.setTransactionListener(mTransactionListener);
        postRecyclerAdapter.setPlayerServiceConnection(mPlayerServiceConnection);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(postRecyclerAdapter);
    }

    private void loadUserFavoritePosts(){
        final List<Post> posts = new ArrayList<>();
        FirebaseRepository.getCurrentUserFavouritePosts(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final long childCount = dataSnapshot.getChildrenCount();
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    FirebaseRepository.getPostById(childSnapshot.getValue(String.class), new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            posts.add(dataSnapshot.getValue(Post.class));
                            if(posts.size() == childCount){
                                postRecyclerAdapter.setData(posts);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public UserFavoritePostPagerFragment() {
    }
}
