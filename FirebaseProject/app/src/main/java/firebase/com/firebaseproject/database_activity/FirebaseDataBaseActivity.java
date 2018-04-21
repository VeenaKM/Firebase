package firebase.com.firebaseproject.database_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import firebase.com.firebaseproject.BaseActivity;
import firebase.com.firebaseproject.R;
import firebase.com.firebaseproject.dataModel.User;

public class FirebaseDataBaseActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "FirebaseDatabase";
    FirebaseDatabase database;
    DatabaseReference myRef;
    private ValueEventListener mPostListener;

    @BindView(R.id.username)
    EditText et_username;
    @BindView(R.id.emailId)
    EditText et_emailId;
    @BindView(R.id.writeToDb)
    Button btnWriteToDb;

    int i = 0;
    @BindView(R.id.userDetails)
    TextView tv_userDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_database);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        btnWriteToDb.setOnClickListener(this);


    }


    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        myRef.child("users").child(userId).setValue(user);


    }

    @Override
    public void onClick(View v) {
        writeNewUser(String.valueOf(i), et_username.getText().toString(), et_emailId.getText().toString());
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User post = dataSnapshot.child("users").child("0").getValue(User.class);
                Log.d(TAG, "Value is: "+post);

                assert post != null;
                tv_userDetails.setText("Username = "+post.username+"\n"
                        + "Email = "+post.email);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(FirebaseDataBaseActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        myRef.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;


    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            myRef.removeEventListener(mPostListener);
        }

    }
}
