package chat.firebase.com.firebasechatapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import chat.firebase.com.firebasechatapplication.model.Message;
import chat.firebase.com.firebasechatapplication.model.User;
import chat.firebase.com.firebasechatapplication.viewholder.MessageViewHolder;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";
    private static final String REQUIRED = "Required";


    private Button btnBack;
    private Button btnSend;
    private EditText edtSentText;
    private RecyclerView rcvListMessage;

    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter mAdapter;

    private FirebaseUser user;

    ProgressBar progressBar;
    private DatabaseReference mDatabase;
    private DatabaseReference mMessageReference;
    private ChildEventListener mMessageListener;

    private ArrayList<Message> messageList;
    boolean userScrolled = false;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    int total_items_to_load=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btnSend = (Button) findViewById(R.id.btn_send);
        btnBack = (Button) findViewById(R.id.btn_back);
        edtSentText = (EditText) findViewById(R.id.edt_sent_text);
        rcvListMessage = (RecyclerView) findViewById(R.id.rcv_list_message);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mMessageReference = FirebaseDatabase.getInstance().getReference("messages");
        user = FirebaseAuth.getInstance().getCurrentUser();

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        rcvListMessage.setLayoutManager(mLayoutManager);

        messageList = new ArrayList<>();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMessage();
                edtSentText.setText("");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        rcvListMessage.setHasFixedSize(true);
        rcvListMessage.setLayoutManager(layoutManager);


        attachRecyclerViewAdapter();
        // Scroll to bottom on new messages
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                rcvListMessage.smoothScrollToPosition(mAdapter.getItemCount());



            }

        });

        rcvListMessage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // If scroll state is touch scroll then set userScrolled
                // true
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Here get the child count, item count and visibleitems
                // from layout manager
                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
//
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;


                if (dy < 0) //check for scroll up
                {

                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstCompletelyVisibleItemPosition();
                    Log.d("TAG", "Scrolling" + "****" + visibleItemCount + " " + pastVisiblesItems + totalItemCount);
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) <= totalItemCount) {
                            loading = false;
//                            Log.v("...", "Last Item !"+messageList.get(0).time);
                            //Do pagination.. i.e. fetch new data
                            total_items_to_load = total_items_to_load + 5;
                            Log.v("...", "total_items_to_load !" + total_items_to_load+" "+pastVisiblesItems);
                            progressBar.setVisibility(View.VISIBLE);

                            refreshData("");

                        }
                    }
                }else {
                    if (totalItemCount > 0 && endHasBeenReached) {
                        //you have reached to the bottom of your recycler view
                        total_items_to_load=5;
                        Log.e("TAG", "Reached end");

                    }
                }
            }
        });

    }

    private void refreshData(String time) {
        Log.d("TAG", "Scrolling" + "****");
        Query query = mMessageReference.orderByChild("time").startAt(time).limitToLast(total_items_to_load);
        loading=true;
//        if (!messageList.isEmpty())
//          messageList.clear();
        query.addChildEventListener(childEventListener);
        // copy for removing at onStop()
        mMessageListener = childEventListener;
        attachRecyclerViewAdapter();
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            // A new message has been added
            // onChildAdded() will be called for each node at the first time
            Message message = dataSnapshot.getValue(Message.class);
            messageList.add(message);
            Log.e(TAG, "onChildAdded2:" + message.body);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.e(TAG, "onChildChanged2:" + dataSnapshot.getKey());

            // A message has changed
            Message message = dataSnapshot.getValue(Message.class);
            Toast.makeText(MessageActivity.this, "onChildChanged2: " + message.body, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.e(TAG, "onChildRemoved:" + dataSnapshot.getKey());

            // A message has been removed
            Message message = dataSnapshot.getValue(Message.class);
            Toast.makeText(MessageActivity.this, "onChildRemoved: " + message.body, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            Log.e(TAG, "onChildMoved:" + dataSnapshot.getKey());

            // A message has changed position
            Message message = dataSnapshot.getValue(Message.class);
            Toast.makeText(MessageActivity.this, "onChildMoved: " + message.body, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "postMessages:onCancelled", databaseError.toException());
            Toast.makeText(MessageActivity.this, "Failed to load Message.", Toast.LENGTH_SHORT).show();
        }
    };
    private void attachRecyclerViewAdapter() {
        if (mAdapter!=null)
        mAdapter.cleanup();
        final Query query = mMessageReference.limitToLast(total_items_to_load);

        mAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class, R.layout.item_message, MessageViewHolder.class, query) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                viewHolder.authorView.setText(model.author);
                viewHolder.timeView.setText(model.time);
                viewHolder.bodyView.setText(model.body);

            }
        };
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rcvListMessage.setAdapter(mAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();

//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                // A new message has been added
//                // onChildAdded() will be called for each node at the first time
//                Message message = dataSnapshot.getValue(Message.class);
//                messageList.add(message);
//
//                Log.e(TAG, "onChildAdded:" + message.body);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.e(TAG, "onChildChanged:" + dataSnapshot.getKey());
//
//                // A message has changed
//                Message message = dataSnapshot.getValue(Message.class);
//                Toast.makeText(MessageActivity.this, "onChildChanged: " + message.body, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Log.e(TAG, "onChildRemoved:" + dataSnapshot.getKey());
//
//                // A message has been removed
//                Message message = dataSnapshot.getValue(Message.class);
//                Toast.makeText(MessageActivity.this, "onChildRemoved: " + message.body, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.e(TAG, "onChildMoved:" + dataSnapshot.getKey());
//
//                // A message has changed position
//                Message message = dataSnapshot.getValue(Message.class);
//                Toast.makeText(MessageActivity.this, "onChildMoved: " + message.body, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e(TAG, "postMessages:onCancelled", databaseError.toException());
//                Toast.makeText(MessageActivity.this, "Failed to load Message.", Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        mMessageReference.addChildEventListener(childEventListener);
//
//        // copy for removing at onStop()
//        mMessageListener = childEventListener;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mMessageListener != null) {
            mMessageReference.removeEventListener(mMessageListener);
        }

        for (Message message: messageList) {
            Log.e(TAG, "listItem: " + message.body);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mAdapter.cleanup();
    }

    private void submitMessage() {
        final String body = edtSentText.getText().toString();

        if (TextUtils.isEmpty(body)) {
            edtSentText.setError(REQUIRED);
            return;
        }

        // User data change listener
        mDatabase.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    Log.e(TAG, "onDataChange: User data is null!");
                    Toast.makeText(MessageActivity.this, "onDataChange: User data is null!", Toast.LENGTH_SHORT).show();
                    return;
                }

                writeNewMessage(body);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "onCancelled: Failed to read user!");
            }
        });
    }

    private void writeNewMessage(String body) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Message message = new Message(getUsernameFromEmail(user.getEmail()), body, time);

        Map<String, Object> messageValues = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        String key = mDatabase.child("messages").push().getKey();

        childUpdates.put("/messages/" + key, messageValues);
        childUpdates.put("/user-messages/" + user.getUid() + "/" + key, messageValues);

        Log.e(TAG, "messages "+messageValues);
        mDatabase.updateChildren(childUpdates);
    }

    private String getUsernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}
