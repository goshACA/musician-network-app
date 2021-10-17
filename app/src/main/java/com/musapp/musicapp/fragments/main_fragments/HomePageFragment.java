package com.musapp.musicapp.fragments.main_fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.adapters.FeedRecyclerAdapter;
import com.musapp.musicapp.enums.SearchMode;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;
import com.musapp.musicapp.model.Post;

import com.musapp.musicapp.utils.FragmentShowUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePageFragment extends Fragment {

    private FeedRecyclerAdapter feedRecyclerAdapter;
    private final int limit = 10;

    private ProgressBar mProgressBar;
    private Spinner mSearchModeSpinner;
    private SearchView searchView;
    private SearchMode mSearchMode = SearchMode.POST_SEARCH;

    private List<Post> posts;
    public static final String ARG_POST = "current_post";
    private SetToolBarAndNavigationBarState mSetToolBarAndNavigationBarState;
    private RecyclerView recyclerView;
    private final String SEARCH_POST = "Search by Posts";
    private final String SEARCH_USER = "Search by UserName";
    private AppMainActivity.MusicPlayerServiceConnection mPlayerServiceConnection;
    private AppMainActivity.ClickListener mClickListener;
    private FeedRecyclerAdapter.FragmentTransactionListener mTransactionListener;
    private SetToolBarAndNavigationBarState mToolBarAndNavigationBarState;

    public void setToolBarAndNavigationBarState(SetToolBarAndNavigationBarState toolBarAndNavigationBarState) {
        mToolBarAndNavigationBarState = toolBarAndNavigationBarState;
    }

    public void setTransactionListener(FeedRecyclerAdapter.FragmentTransactionListener transactionListener) {
        mTransactionListener = transactionListener;
    }

    private  SwipeRefreshLayout swipeRefreshLayout;
    public void setSetToolBarAndNavigationBarState(SetToolBarAndNavigationBarState setToolBarAndNavigationBarState) {
        mSetToolBarAndNavigationBarState = setToolBarAndNavigationBarState;
    }

    public AdapterView.OnItemSelectedListener mItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
            switch (item){
                case SEARCH_POST:
                    mSearchMode = SearchMode.POST_SEARCH;
                    if (feedRecyclerAdapter != null){
                        feedRecyclerAdapter.setSearchMode(SearchMode.POST_SEARCH);
                    }
                    break;
                case SEARCH_USER:
                    mSearchMode = SearchMode.USERNAME_SEARCH;
                    if (feedRecyclerAdapter != null){
                        feedRecyclerAdapter.setSearchMode(SearchMode.USERNAME_SEARCH);
                    }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                mSearchModeSpinner.setVisibility(View.VISIBLE);
            }else{
                mSearchModeSpinner.setVisibility(View.GONE);
            }
        }
    };

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
                    //TODO open extended post fragment
                    mClickListener.postClickListener(post);

                }
            };



    public void setClickListener(AppMainActivity.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setPlayerServiceConnection(AppMainActivity.MusicPlayerServiceConnection connection){
        mPlayerServiceConnection = connection;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_home_page, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_fragment_home_page_posts);
        mProgressBar = rootView.findViewById(R.id.progressbar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.darkPurple), android.graphics.PorterDuff.Mode.MULTIPLY);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_feed);
        mSearchModeSpinner = rootView.findViewById(R.id.spinner_home_page_fragment_search_mode_drop_down_);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        posts = new ArrayList<>();
        initRecyclerView(recyclerView);
        loadFirstPosts(limit);
        initSearchSpinner();
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getChildCount();
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int totalItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    if(feedRecyclerAdapter.getData().size() >= limit){
                        setProgressBarVisibility(View.VISIBLE);
                        loadPostsFromDatabase(feedRecyclerAdapter.getData().get(feedRecyclerAdapter.getData().size() - limit + 1).getPublishedTime(), limit);}
                }

            }});
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNewPostsAfterRefresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        mSetToolBarAndNavigationBarState.setTitle(R.string.title_home);
    }

    private void initSearchSpinner(){
        mSearchModeSpinner.setOnItemSelectedListener(mItemSelectedListener);
        ArrayAdapter<CharSequence> spinnerDataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.search_modes, R.layout.search_mode_spinner_item);
        spinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSearchModeSpinner.setAdapter(spinnerDataAdapter);
    }

    private void initRecyclerView(RecyclerView view){
        feedRecyclerAdapter = new FeedRecyclerAdapter();
        feedRecyclerAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        feedRecyclerAdapter.setOnUserImageListener(mOnUserImageListener);
        feedRecyclerAdapter.setTransactionListener(mTransactionListener);
        //feedRecyclerAdapter.setInnerItemClickListener(mInnerItemOnClickListener);
        feedRecyclerAdapter.setPlayerServiceConnection(mPlayerServiceConnection);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        view.setAdapter(feedRecyclerAdapter);
    }



    private void loadFirstPosts(int limit){
        FirebaseRepository.getAllPosts(limit, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> list = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Post post = postSnapshot.getValue(Post.class);
                    if(!feedRecyclerAdapter.getData().contains(post)){
                        list.add(post);
                    }
                }
                Collections.reverse(list);
                feedRecyclerAdapter.setData(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPostsFromDatabase(long lastPostTime,final int limit){

      FirebaseRepository.getPosts(limit, lastPostTime, new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              List<Post> list = new ArrayList<>();
              boolean showProgress = false;
              for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                  Post post = postSnapshot.getValue(Post.class);
                  if(!feedRecyclerAdapter.getData().contains(post)){
                      list.add(post);
                      showProgress = true;
                  }
                 // if(!showProgress)
                   //   setProgressBarVisibility(View.GONE);
              }
              Collections.reverse(list);
              posts.addAll(list);
              if(list.size() > 0) {
                  feedRecyclerAdapter.setData(list);
                  feedRecyclerAdapter.notifyItemRangeChanged(feedRecyclerAdapter.getItemCount() - limit + 1, limit);
              } setProgressBarVisibility(View.GONE);
             // posts.clear();

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

    }

    private void loadNewPostsAfterRefresh(){
       /* FirebaseRepository.getNewPost(feedRecyclerAdapter.getData().get(0).getPublishedTime(), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> posts =  new ArrayList<>();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    posts.add(postSnapshot.getValue(Post.class));
                }
                swipeRefreshLayout.setRefreshing(false);
                feedRecyclerAdapter.setData(posts, 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
       FirebaseRepository.getNewPost(limit, new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               Post post = dataSnapshot.getValue(Post.class);
               if(!feedRecyclerAdapter.getData().contains(post)){
                   Log.i("POSTTT", post.getPrimaryKey() + "mm");
                    feedRecyclerAdapter.addPostItem(post, 0);
                    recyclerView.smoothScrollToPosition(0);
                   }
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });/*
       FirebaseRepository.getNewPost(limit, new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               List<Post> list = new ArrayList<>();
               boolean showProgress = false;
               for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                   Post post = postSnapshot.getValue(Post.class);
                   if(!feedRecyclerAdapter.getData().contains(post)){
                       list.add(post);
                       showProgress = true;
                   }
                   if(!showProgress)
                       setProgressBarVisibility(View.GONE);
               }
               Collections.reverse(list);
               if(list.size() > 0) {
                   feedRecyclerAdapter.setData(list);
                   feedRecyclerAdapter.notifyItemRangeChanged(feedRecyclerAdapter.getItemCount() - limit + 1, limit);
               } setProgressBarVisibility(View.GONE);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });*/


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_fragment_search_add, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search_home_fragment_menu_item);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextFocusChangeListener(mOnFocusChangeListener);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //TODO search
                feedRecyclerAdapter.getFilter().filter(s);
                return false;
            }
        });
        menuItem = menu.findItem(R.id.action_add_home_fragment_menu_item);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_home_fragment_menu_item:
                //TODO open new fragment for add new post
                AddPostFragment fragment = new AddPostFragment();
                fragment.setSetToolBarAndNavigationBarState(mSetToolBarAndNavigationBarState);
                beginTransaction(fragment);
                break;
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        mSetToolBarAndNavigationBarState.showToolBar();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void beginTransaction(Fragment fragment){
        if(fragment.isAdded())
            return;
        FragmentShowUtils.setPreviousFragment(this);
        getFragmentManager().beginTransaction()

                .addToBackStack(null)
                .replace(R.id.layout_activity_app_container, fragment)
              //  .add(R.id.layout_activity_app_container, fragment)
                .commit();

    }

    public HomePageFragment() {
        //Required
    }

    private void setProgressBarVisibility(int visibility){
        mProgressBar.setVisibility(visibility);
    }

}
