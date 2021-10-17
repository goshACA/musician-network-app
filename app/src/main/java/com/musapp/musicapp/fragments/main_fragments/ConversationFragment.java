package com.musapp.musicapp.fragments.main_fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.ChatMessageAdapter;
import com.musapp.musicapp.adapters.MessageDashboardRecyclerAdapter;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase_messaging_notifications.NotifyMessage;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.HandleBackPressWithFirebase;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;
import com.musapp.musicapp.model.Chat;
import com.musapp.musicapp.model.Message;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.service.BoundService;
import com.musapp.musicapp.utils.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ConversationFragment extends Fragment implements HandleBackPressWithFirebase {
    private String userPrimaryKey = "none";
    private static String chatId = "none";
    private static String userId = "none";
    private User mUser;
    private Chat mChat;
    private EditText inputText;
    private Button enterInput;
    private RecyclerView mRecyclerView;
    private ChatMessageAdapter mRecyclerAdapter;
    private BoundService.NotificationBinder mNotificationBinder;
    private boolean isBind = false;

   private ServiceConnection mServiceConnection = new ServiceConnection() {
       @Override
       public void onServiceConnected(ComponentName name, IBinder service) {
           mNotificationBinder = (BoundService.NotificationBinder) service;
           isBind = true;
           connectService();
       }

       @Override
       public void onServiceDisconnected(ComponentName name) {
           mNotificationBinder = null;
           isBind = false;
       }
   };

    private SetToolBarAndNavigationBarState mToolBarAndNavigationBarState;

    public void setToolBarAndNavigationBarState(SetToolBarAndNavigationBarState toolBarAndNavigationBarState) {
        mToolBarAndNavigationBarState = toolBarAndNavigationBarState;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        inputText = view.findViewById(R.id.edit_text_write_message);
        enterInput = view.findViewById(R.id.action_send_message);
        mRecyclerView = view.findViewById(R.id.recycler_view_fragment_conversation_messages);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enterInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inputText.getText().toString().isEmpty()){
                    sendMessage(inputText.getText().toString());
                    inputText.setText("");
                }

            }
        });
        init();
       if(userPrimaryKey.equals("nano"))
           return;

        if (getArguments() != null) {
            if (getArguments().getString("ID") != null) {
                userPrimaryKey = getArguments().getString("ID");
                userId = userPrimaryKey;
                loadUser();
                if (getArguments().getString("CHAT_ID") == null) {
                    final boolean[] check = {false};
                    createChatIfNeeded(check);
                }


            } else if (getArguments().getString("CHAT_ID") != null) {
                FirebaseRepository.getChatById(getArguments().getString("CHAT_ID"), new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mChat = dataSnapshot.getValue(Chat.class);
                        chatId = mChat.getPrimaryKey();
                        if (mChat.getFirstUserId().equals(CurrentUser.getCurrentUser().getPrimaryKey())) {
                            userPrimaryKey = mChat.getSecondUserId();
                            loadUser();
                        } else {
                            userPrimaryKey = mChat.getFirstUserId();
                            loadUser();
                        }
                        userId = userPrimaryKey;
                        loadMessages();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

    }

    private void connectService(){
        mNotificationBinder.setBindState(true);
        Bundle args = new Bundle();
        args.putString("fragmentName", ConversationFragment.class.getSimpleName());
        args.putString("id", userPrimaryKey);
        mNotificationBinder.setBindFragmentBundle(args);


    }


    private void loadUser(){
        Intent intent = new Intent(getActivity(), BoundService.class);
        getActivity().bindService(intent, mServiceConnection,  Context.BIND_AUTO_CREATE);


        FirebaseRepository.getUserByPrimaryKey(userPrimaryKey, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                if (mUser != null){
                    mToolBarAndNavigationBarState.setTitle(mUser.getFullName());
                }else{
                    mToolBarAndNavigationBarState.setTitle("Message");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init(){
        mRecyclerAdapter = new ChatMessageAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
    private void createChatIfNeeded(final boolean check[]) {

      final Chat chat = new Chat();
      chat.setFirstUserId(CurrentUser.getCurrentUser().getPrimaryKey());
      chat.setSecondUserId(userPrimaryKey);

      FirebaseRepository.getChatByFirstUserId(CurrentUser.getCurrentUser().getPrimaryKey(), new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for(DataSnapshot chatSnapshot: dataSnapshot.getChildren()){
                  if(chat.equals(chatSnapshot.getValue(Chat.class))){
                      check[0] = true;
                      mChat = chatSnapshot.getValue(Chat.class);
                      chatId = mChat.getPrimaryKey();
                      loadMessages();
                      break;}}
                      if(!check[0]){
                            FirebaseRepository.getChatByFirstUserId(userPrimaryKey, new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot chatSnapshot: dataSnapshot.getChildren()){
                                        if(chat.equals(chatSnapshot.getValue(Chat.class))){
                                            check[0] = true;
                                            mChat = chatSnapshot.getValue(Chat.class);
                                            chatId = mChat.getPrimaryKey();
                                            loadMessages();
                                            break;}}
                                            if(!check[0])
                                                createChat();
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

    private void createChat() {
        final Chat chat = new Chat();
        chat.setFirstUserId(CurrentUser.getCurrentUser().getPrimaryKey());
        chat.setSecondUserId(userPrimaryKey);
        FirebaseRepository.createChat(chat, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseRepository.setChatInnerPrimaryKey(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> lastChild = dataSnapshot.getChildren().iterator();
                        chat.setPrimaryKey(lastChild.next().getKey());
                        FirebaseRepository.updateChat(chat);
                        CurrentUser.getCurrentUser().addChatId(chat.getPrimaryKey());
                        mUser.addChatId(chat.getPrimaryKey());
                        FirebaseRepository.updateUserChatsId(CurrentUser.getCurrentUser().getPrimaryKey(), CurrentUser.getCurrentUser().getChatsId());
                        FirebaseRepository.updateUserChatsId(userPrimaryKey, mUser.getChatsId());
                        mChat = chat;
                        chatId = chat.getPrimaryKey();
                        loadMessages();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendMessage(String msgText) {
//        DateFormat simple = new SimpleDateFormat("dd MMM HH:mm", Locale.US);
//        Date date = new Date(System.currentTimeMillis());
        Message message = new Message(msgText, CurrentUser.getCurrentUser().getPrimaryKey(), System.currentTimeMillis()
                , CurrentUser.getCurrentUser().getUserInfo().getImageUri());
        new NotifyMessage(mUser.getToken(), CurrentUser.getCurrentUser().getNickName() + " wrote new message"
                , message.getMessageText(),userPrimaryKey, CurrentUser.getCurrentUser().getPrimaryKey()
                , CurrentUser.getCurrentUser().getUserInfo().getImageUri()
                , System.currentTimeMillis(), chatId).execute();
        if(mChat == null){
            Toast.makeText(getActivity(), "Ooops, something went wrong", Toast.LENGTH_LONG).show();
            return;
        }
        mChat.addMessage(message);
        FirebaseRepository.updateChat(mChat);
    }

    private void loadMessages(){
        FirebaseRepository.loadMessages(mChat.getPrimaryKey(), new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("datasnapp", dataSnapshot.toString());
                mRecyclerAdapter.addMessage(dataSnapshot.getValue(Message.class));
                mRecyclerView.scrollToPosition(mRecyclerAdapter.getItemCount() - 1);
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
        });
    }

    @Override
    public void OnBackPressed(OnSuccessListener<Void> onSuccessListener) {
        if(mChat == null)
            return;

        mChat.setFirstUserLastSeen(StringUtils.millisecondsToDateString(System.currentTimeMillis()));
        FirebaseRepository.updateChat(mChat, onSuccessListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        if(isBind)
        getActivity().unbindService(mServiceConnection);
        isBind = false;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isBind)
        getActivity().unbindService(mServiceConnection);
        isBind = false;
    }
}
