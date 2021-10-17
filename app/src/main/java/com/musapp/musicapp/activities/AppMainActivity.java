package com.musapp.musicapp.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.FeedRecyclerAdapter;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.BlankFragment;
import com.musapp.musicapp.fragments.main_fragments.ConversationFragment;
import com.musapp.musicapp.fragments.main_fragments.FullScreenImageFragment;
import com.musapp.musicapp.fragments.main_fragments.HomePageFragment;
import com.musapp.musicapp.fragments.main_fragments.MessagesFragment;
import com.musapp.musicapp.fragments.main_fragments.NotificationFragment;
import com.musapp.musicapp.fragments.main_fragments.OtherUserProfileFragment;
import com.musapp.musicapp.fragments.main_fragments.PostDetailsFragment;
import com.musapp.musicapp.fragments.main_fragments.ProfileFragment;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.preferences.RememberPreferences;
import com.musapp.musicapp.service.BoundService;
import com.musapp.musicapp.service.MusicPlayerService;
import com.musapp.musicapp.utils.GlideUtil;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppMainActivity extends AppCompatActivity {


    private NotificationFragment mNotificationFragment;
    private ProfileFragment mProfileFragment;
    private HomePageFragment mHomePageFragment;
    private MessagesFragment mMessagesFragment;
    private BottomNavigationView navigation;


    private SetToolBarAndNavigationBarState mToolBarTitle = new SetToolBarAndNavigationBarState() {
        @Override
        public void setTitle(int titleId) {
            getSupportActionBar().setTitle(getString(titleId));
        }

        @Override
        public void setTitle(String title) {
            getSupportActionBar().setTitle(title);
        }

        @Override
        public void hideToolBar() {
            getSupportActionBar().hide();
        }

        @Override
        public void showToolBar() {
            getSupportActionBar().show();
        }

        @Override
        public void hideNavigationBar() {
            if (navigation != null){
                navigation.setVisibility(View.GONE);
            }
        }

        @Override
        public void showNavigationBar() {
            if (navigation != null){
                navigation.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void showBackArrow(boolean state) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(state);
            if (state){

            }
        }

    };



    private ProfileFragment.ChangeActivity mChangeActivity = new ProfileFragment.ChangeActivity() {
        @Override
        public void changeActivity( Class nextActivity) {
           activityTransaction( nextActivity);

        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
              //     mTextMessage.setText(R.string.title_home);
                    mHomePageFragment.setPlayerServiceConnection(mPlayerServiceConnection);
                    beginTransaction(mHomePageFragment);
                    return true;

                case R.id.navigation_mail:
                    beginTransaction(mMessagesFragment);
                    return true;
                case R.id.navigation_notifications:
                    beginTransaction(mNotificationFragment);
                    return true;
                case R.id.navigation_profile:
                    beginTransaction(mProfileFragment);
                    return true;
            }
            return false;
        }
    };

    private FeedRecyclerAdapter.FragmentTransactionListener mTransaction = new FeedRecyclerAdapter.FragmentTransactionListener() {
        @Override
        public void openFragment(Fragment fragment) {
            if (fragment instanceof FullScreenImageFragment){
                ((FullScreenImageFragment) fragment).setSetToolBarAndNavigationBarState(mToolBarTitle);
            }
            beginTransaction(fragment);
        }
    };

    private ClickListener mClickListener = new ClickListener() {
        @Override
        public void userImageClickListener(Post post) {
            OtherUserProfileFragment otherUserProfileFragment = new OtherUserProfileFragment();
            Bundle args = new Bundle();
            args.putString(String.class.getSimpleName(), post.getUserId());
            otherUserProfileFragment.setArguments(args);
            otherUserProfileFragment.setSetToolBarAndNavigationBarState(mToolBarTitle);
            otherUserProfileFragment.setPlayerServiceConnection(mPlayerServiceConnection);
            otherUserProfileFragment.setClickListener(mClickListener);
            otherUserProfileFragment.setTransactionListener(mTransaction);
            beginTransaction(otherUserProfileFragment);
        }

        @Override
        public void postClickListener(Post post) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(HomePageFragment.ARG_POST, post);
            PostDetailsFragment fragment = new PostDetailsFragment();
            fragment.setPlayerServiceConnection(mPlayerServiceConnection);
            fragment.setClickListener(mClickListener);
            fragment.setArguments(bundle);
            fragment.setSetToolBarAndNavigationBarState(mToolBarTitle);
            beginTransaction(fragment);
        }

        @Override
        public void userCommentImageClickListener(String userId) {
            OtherUserProfileFragment otherUserProfileFragment = new OtherUserProfileFragment();
            Bundle args = new Bundle();
            args.putString(String.class.getSimpleName(), userId);
            otherUserProfileFragment.setArguments(args);
            otherUserProfileFragment.setSetToolBarAndNavigationBarState(mToolBarTitle);
            otherUserProfileFragment.setPlayerServiceConnection(mPlayerServiceConnection);
            otherUserProfileFragment.setClickListener(mClickListener);
            otherUserProfileFragment.setTransactionListener(mTransaction);
            beginTransaction(otherUserProfileFragment);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String customFragment = "def";
        if(getIntent() != null)
        customFragment = getIntent().getStringExtra("goto");
        setContentView(R.layout.activity_app_main);
        navigation = (BottomNavigationView) findViewById(R.id.navigation_activity_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        GlideUtil.setContext(this);
        setCurrentUser();
        init();
        navigation.setSelectedItemId(R.id.navigation_home);
        if(customFragment != null ){
            if(customFragment.equals("NotificationFragment"))
            beginTransaction(mNotificationFragment);

            else if(customFragment.equals("ConversationFragment")){
                ConversationFragment conversationFragment = new ConversationFragment();
                Bundle args = new Bundle();
                args.putString("CHAT_ID", getIntent().getStringExtra("CHAT_ID"));
                conversationFragment.setArguments(args);
                conversationFragment.setToolBarAndNavigationBarState(mToolBarTitle);
                beginTransaction(conversationFragment);
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.apply();
    }


    private void init(){
        mNotificationFragment = new NotificationFragment();
        mProfileFragment = new ProfileFragment();
        mHomePageFragment = new HomePageFragment();
        mHomePageFragment.setSetToolBarAndNavigationBarState(mToolBarTitle);
        mHomePageFragment.setClickListener(mClickListener);
        mHomePageFragment.setTransactionListener(mTransaction);
        mHomePageFragment.setToolBarAndNavigationBarState(mToolBarTitle);
        mMessagesFragment = new MessagesFragment();
        mMessagesFragment.setSetToolBarAndNavigationBarState(mToolBarTitle);
        mProfileFragment.setSetToolBarAndNavigationBarState(mToolBarTitle);
        mProfileFragment.setClickListener(mClickListener);
        mProfileFragment.setTransactionListener(mTransaction);
        mProfileFragment.setPlayerServiceConnection(mPlayerServiceConnection);
        mNotificationFragment.setSetToolBarAndNavigationBarState(mToolBarTitle);
        mNotificationFragment.setClickListener(mClickListener);
        mNotificationFragment.setFragmentTransactionListener(mTransaction);
        mProfileFragment.setSetToolBarAndNavigationBarState(mToolBarTitle);

        mProfileFragment.setChangeActivity(mChangeActivity);

    }

    private void beginTransaction(Fragment fragment){

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.layout_activity_app_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    

    private void activityTransaction(Class nextActivity){
        Intent intent = new Intent(this, nextActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(getString(R.string.quit), true );
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {


            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0 || count == 1) {
                finish();
                //additional code
            } else {
                getSupportFragmentManager().popBackStack();
            }

        }

    private void setCurrentUser(){

        if(RememberPreferences.getUser(this).equals("none")){
            RememberPreferences.saveState(AppMainActivity.this, true);
            activityTransaction(StartActivity.class);
        }

        FirebaseRepository.setCurrentUser(RememberPreferences.getUser(this), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null ){
                    RememberPreferences.saveState(AppMainActivity.this, true);
                    activityTransaction(StartActivity.class);}
                CurrentUser.setCurrentUser(dataSnapshot.getValue(User.class));
                CurrentUser.setCurrentFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private MusicPlayerService.LocalBinder mLocalBinder;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLocalBinder = (MusicPlayerService.LocalBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLocalBinder = null;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicPlayerService.class);
        this.bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public interface ClickListener {
        void userImageClickListener(Post post);
        void postClickListener(Post post);
        void userCommentImageClickListener(String userId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(this, "orientation changes", Toast.LENGTH_SHORT).show();
            mToolBarTitle.hideNavigationBar();
            mToolBarTitle.hideToolBar();
        }else{
            mToolBarTitle.showNavigationBar();
            mToolBarTitle.showToolBar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (mLocalBinder != null){
            mLocalBinder.stop();
            this.unbindService(mServiceConnection);

        }


       /* if(RememberPreferences.getState(this)){
            if(RememberPreferences.getUser(this).equals("none"))
                RememberPreferences.saveUser(this, "none");
            else
        RememberPreferences.saveUser(this, CurrentUser.getCurrentUser().getPrimaryKey());}*/
       /* SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", StartActivity.class.getName());
        editor.apply();*/
    }

    private MusicPlayerServiceConnection mPlayerServiceConnection =
            new MusicPlayerServiceConnection() {

                @Override
                public void play(String url) {
                    if(mLocalBinder != null){
                       mLocalBinder.play(url);
                    }
                }
                @Override
                public void pause() {
                    mLocalBinder.pause();
                }

                @Override
                public void seekTo(int progress) {
                    mLocalBinder.seekTo(progress);
                }


                @Override
                public void handleSeekBar( SeekBar seekBar, Button button) {
                    mLocalBinder.setSeekBar(seekBar);
                    mLocalBinder.setPlayPauseButton(button);
                }

                @Override
                public boolean isPlayerPlaying(String url) {
                    if (mLocalBinder.isPlaying() && mLocalBinder.getCurrentUrl().equals(url)){
                        //mLocalBinder.startSeekBarHandle();
                        return true;
                    }
                    return false;
                }

                @Override
                public void startSeekBarHandle() {
                    mLocalBinder.startSeekBarHandle();
                }
            };


    public interface MusicPlayerServiceConnection{
        void play(String url);
        void pause();
        void seekTo(int progress);
        void handleSeekBar(SeekBar seekBar, Button button);
        boolean isPlayerPlaying(String url);
        void startSeekBarHandle();
    }

}
