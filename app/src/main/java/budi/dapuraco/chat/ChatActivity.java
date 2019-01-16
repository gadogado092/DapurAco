package budi.dapuraco.chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import budi.dapuraco.R;

public class ChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String idUserChat;
    private DatabaseReference RootRef;
    private FirebaseAuth mAuth;
    private String UID;
    private ImageButton btnSend;
    private EditText editMessage;
    private RecyclerView recyclerView;
    private List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private MessageAdapter messageAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private final int TOTAL_ITEMS_TO_LOAD=10;
    private int mCurrentPage=1;

    private int itempos=0;
    private String itemlastkey;
    private String itemprevkey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipechat);
        btnSend=(ImageButton)findViewById(R.id.imageButton);
        editMessage=(EditText) findViewById(R.id.editText);

        idUserChat=getIntent().getStringExtra("user");

        RootRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        UID= mAuth.getCurrentUser().getUid();

        RootRef.child("Users").child(idUserChat).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setTitle(dataSnapshot.child("name").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        RootRef.child("Chat").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(idUserChat)){
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("timeStamp", ServerValue.TIMESTAMP);

                    Map charUserMap = new HashMap();
                    charUserMap.put("Chat/"+UID+"/"+idUserChat, chatAddMap);
                    charUserMap.put("Chat/"+idUserChat+"/"+UID, chatAddMap);

                    RootRef.updateChildren(charUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError!=null){
                                Log.d("CHAT_LOG",databaseError.getMessage().toLowerCase().toString());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.message_list);
        messageAdapter= new MessageAdapter(messagesList);
        recyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(messageAdapter);

        loadmessage();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //mCurrentPage++;
                //messagesList.clear();
                itempos=0;
                loadmoremessage();
            }
        });
    }

    private void loadmoremessage() {
        DatabaseReference messageRef= RootRef.child("messages").child(UID).child(idUserChat);
        Query messageQuery=messageRef.orderByKey().endAt(itemlastkey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages= dataSnapshot.getValue(Messages.class);
                String messageKey=dataSnapshot.getKey();

                if (!itemprevkey.equals(messageKey)){
                    messagesList.add(itempos++,messages);
                }else {
                    itemprevkey=itemlastkey;
                }

                if (itempos==1){
                    itemlastkey=messageKey;
                }

                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messagesList.size()-1);
                swipeRefreshLayout.setRefreshing(false);
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

    private void loadmessage() {
        DatabaseReference messageRef= RootRef.child("messages").child(UID).child(idUserChat);
        Query messageQuery=messageRef.limitToLast(mCurrentPage*TOTAL_ITEMS_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                itempos++;
                if (itempos==1){
                    itemlastkey=dataSnapshot.getKey();
                    itemprevkey=dataSnapshot.getKey();
                }
                Messages messages= dataSnapshot.getValue(Messages.class);
                messagesList.add(messages);
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messagesList.size()-1);
                swipeRefreshLayout.setRefreshing(false);
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

    private void sendMessage() {
        String message=editMessage.getText().toString();

        if (!TextUtils.isEmpty(message)){

            String current_user_ref = "messages/"+ UID +"/"+idUserChat;
            String chat_user_ref = "messages/"+ idUserChat +"/"+UID;

            DatabaseReference user_message_push = RootRef.child("messages").child(UID).child(idUserChat).push();

            String push_id= user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen",false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from",UID);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref+"/"+push_id, messageMap);
            messageUserMap.put(chat_user_ref+"/"+push_id, messageMap);

            RootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError !=null){
                        Log.d("CHAT_LOG",databaseError.getMessage().toString());
                    }
                }
            });
            editMessage.setText("");
        }
    }
}
