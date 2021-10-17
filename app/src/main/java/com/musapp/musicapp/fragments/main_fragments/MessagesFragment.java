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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.MessageDashboardRecyclerAdapter;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;
import com.musapp.musicapp.model.Conversation;
import com.musapp.musicapp.model.Message;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.utils.StringUtils;

import java.util.Iterator;

public class MessagesFragment extends Fragment {

    private SetToolBarAndNavigationBarState mSetToolBarAndNavigationBarState;
    private RecyclerView mRecyclerView;
    private MessageDashboardRecyclerAdapter mMessageDashboardRecyclerAdapter;


    private MessageDashboardRecyclerAdapter.OnDashboardItemClick mOnDashboardItemClick = new MessageDashboardRecyclerAdapter.OnDashboardItemClick() {
        @Override
        public void onDashboardItemClick(Conversation conversation) {
            ConversationFragment conversationFragment = new ConversationFragment();
            conversationFragment.setToolBarAndNavigationBarState(mSetToolBarAndNavigationBarState);
            Bundle bundle = new Bundle();
            bundle.putString("CHAT_ID", conversation.getChatId());
            conversationFragment.setArguments(bundle);
            quit(conversationFragment);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_messages, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view_fragment_messages_chats);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        loadConversations();
        mSetToolBarAndNavigationBarState.setTitle(R.string.title_dashboard);

    }

    private void init() {
        mMessageDashboardRecyclerAdapter = new MessageDashboardRecyclerAdapter();
        mMessageDashboardRecyclerAdapter.setOnDashboardItemClick(mOnDashboardItemClick);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mMessageDashboardRecyclerAdapter);
    }


    private void loadConversations() {
        for (final String chatId : CurrentUser.getCurrentUser().getChatsId()) {
            FirebaseRepository.setMessageLastItemListener(chatId, new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              //      DataSnapshot messageSnapshot = dataSnapshot.getChildren().iterator().next();
                    final Message message = dataSnapshot.getValue(Message.class);
                    FirebaseRepository.getUserByPrimaryKey(message.getCreatorId(), new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Conversation conversation = new Conversation(dataSnapshot.getValue(User.class).getNickName()
                                    , message.getMessageText(), StringUtils.millisecondsToDateString(message.getCreationDate()));
                            conversation.setChatId(chatId);
                            conversation.setUserProfile(dataSnapshot.getValue(User.class).getUserInfo().getImageUri());
                            if (mMessageDashboardRecyclerAdapter.getItemCount() > 0) {
                                int index = mMessageDashboardRecyclerAdapter.conversationIndex(conversation);
                                if (index > -1) {
                                    mMessageDashboardRecyclerAdapter.changeItem(conversation, index);
                                } else
                                    mMessageDashboardRecyclerAdapter.addConversation(conversation);
                            } else
                                mMessageDashboardRecyclerAdapter.addConversation(conversation);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });
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
            });}




        /*    FirebaseRepository.setMessageListenerForChatById(chatId, new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //TODO update specific conversation item
                    final Message message = dataSnapshot.getValue(Message.class);
                    FirebaseRepository.getUserByPrimaryKey(message.getCreatorId(), new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Conversation conversation = new Conversation(dataSnapshot.getValue(User.class).getNickName(), message.getMessageText(), message.getCreationDate());
                            conversation.setChatId(chatId);
                            conversation.setUserProfile(dataSnapshot.getValue(User.class).getUserInfo().getImageUri());
                            if (mMessageDashboardRecyclerAdapter.getItemCount() > 0) {
                                int index = mMessageDashboardRecyclerAdapter.conversationIndex(conversation);
                                if (index > -1) {
                                    mMessageDashboardRecyclerAdapter.changeItem(conversation, index);
                                } else
                                    mMessageDashboardRecyclerAdapter.addConversation(conversation);
                            } else
                                mMessageDashboardRecyclerAdapter.addConversation(conversation);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }*

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
            });*/
        }


    public void setSetToolBarAndNavigationBarState(SetToolBarAndNavigationBarState setToolBarAndNavigationBarState) {
        mSetToolBarAndNavigationBarState = setToolBarAndNavigationBarState;
    }

    private void quit(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.layout_activity_app_container, fragment).commit();
    }

    public MessagesFragment() {
        //required
    }
}
