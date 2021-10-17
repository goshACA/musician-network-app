package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.adapters.FeedRecyclerAdapter;

import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;
import com.musapp.musicapp.model.Chat;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserProfileFragment extends Fragment {
    @BindView(R.id.circular_view_fragment_other_user_profile_image)
    CircleImageView userImage;
    @BindView(R.id.button_start_conversation)
    ImageView startConversationButton;
    @BindView(R.id.text_fragment_other_user_profile_fullname)
    TextView fullname;
    @BindView(R.id.text_fragment_other_user_profile_nickname)
    TextView nickname;
    @BindView(R.id.text_fragment_other_user_profile_more_info)
    TextView moreInfo;
    @BindView(R.id.text_fragment_other_user_profile_info_box)
    TextView infoBox;
    @BindView(R.id.recycler_fragment_profile_other_user_posts)
    RecyclerView postsRecyclerView;

    String userPrimaryKey;
    private SetToolBarAndNavigationBarState mSetToolBarAndNavigationBarState;
    private AppMainActivity.MusicPlayerServiceConnection mPlayerServiceConnection;
    private AppMainActivity.ClickListener mClickListener;

    public void setClickListener(AppMainActivity.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    private FeedRecyclerAdapter.FragmentTransactionListener mTransactionListener;

    public void setTransactionListener(FeedRecyclerAdapter.FragmentTransactionListener transactionListener) {
        mTransactionListener = transactionListener;
    }

    public void setSetToolBarAndNavigationBarState(SetToolBarAndNavigationBarState setToolBarAndNavigationBarState) {
        mSetToolBarAndNavigationBarState = setToolBarAndNavigationBarState;
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

    private FeedRecyclerAdapter postRecyclerViewAdapter = new FeedRecyclerAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoBox.setVisibility(View.VISIBLE);
            }
        });
        infoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoBox.setVisibility(View.GONE);
            }
        });
        startConversationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConversationWithUser(userPrimaryKey);
            }
        });

        postRecyclerViewAdapter.setData(new ArrayList<Post>());
        postRecyclerViewAdapter.setPlayerServiceConnection(mPlayerServiceConnection);
        postRecyclerViewAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        postRecyclerViewAdapter.setOnUserImageListener(mOnUserImageListener);
        postRecyclerViewAdapter.setTransactionListener(mTransactionListener);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postsRecyclerView.setAdapter(postRecyclerViewAdapter);

        showDataFromFireBase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_other_user_profile, container, false);
        ButterKnife.bind(this, view);
        if(getArguments() != null)
            userPrimaryKey = getArguments().getString(String.class.getSimpleName());
        return view;
    }

    public void setPlayerServiceConnection(AppMainActivity.MusicPlayerServiceConnection playerServiceConnection) {
        mPlayerServiceConnection = playerServiceConnection;
    }

    private void showDataFromFireBase(){
        FirebaseRepository.getUserByPrimaryKey(userPrimaryKey, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              final User  user = dataSnapshot.getValue(User.class);
                GlideUtil.setImageGlide(user.getUserInfo().getImageUri(), userImage);
                fullname.setText(user.getFullName());
                mSetToolBarAndNavigationBarState.setTitle(user.getFullName());

                nickname.setText(user.getNickName());
                infoBox.setText(user.getUserInfo().getAdditionalInfo());
                final List<Post> posts = new ArrayList<>();
                for(final String postId: user.getPostId()){
                    FirebaseRepository.getPostById(postId, new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            posts.add(dataSnapshot.getValue(Post.class));
                            if(posts.size() == user.getPostId().size()){
                                postRecyclerViewAdapter.setData(posts);
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

    private void startConversationWithUser(String userId){
        ConversationFragment conversationFragment = new ConversationFragment();
        conversationFragment.setToolBarAndNavigationBarState(mSetToolBarAndNavigationBarState);
        Bundle bundle = new Bundle();
        bundle.putString("ID", userId);
        conversationFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().add(R.id.layout_activity_app_container, conversationFragment).commit();
    }





}
