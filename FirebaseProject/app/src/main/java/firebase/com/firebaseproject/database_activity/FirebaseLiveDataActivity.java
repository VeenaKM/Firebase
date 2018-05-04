package firebase.com.firebaseproject.database_activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import firebase.com.firebaseproject.R;
import firebase.com.firebaseproject.dataModel.User;
import firebase.com.firebaseproject.viewModel.FirebaseViewModel;

public class FirebaseLiveDataActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    private static final String TAG = "FirebaseDatabase";
//    FirebaseDatabase database;
//    DatabaseReference myRef;
//    private ValueEventListener mPostListener;

    @BindView(R.id.username)
    EditText et_username;
    @BindView(R.id.emailId)
    EditText et_emailId;
    @BindView(R.id.writeToDb)
    Button btnWriteToDb;

    int i = 0;
    @BindView(R.id.userDetails)
    TextView tv_userDetails;

    FirebaseViewModel viewModel;
    ArrayList<String> usersArr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        ButterKnife.bind(this);

        // Obtain a new or prior instance of FirebaseViewModel from the
        // ViewModelProviders utility class.
        viewModel = ViewModelProviders.of(this).get(FirebaseViewModel.class);

        LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                usersArr=new ArrayList<>();
                if(dataSnapshot!=null && dataSnapshot.getValue()!=null) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                        User post = dataSnapshot.child("users").getValue(User.class);
                        User post = snap.getValue(User.class);
                        Log.d(TAG, "Value is: " + post);

//                        tv_userDetails.setText("Username = " + post.username + "\n"
//                                + "Email = " + post.email);

                        usersArr.add("Username="+post.username);
                        usersArr.add("Email="+post.email);
                        tv_userDetails.setText(usersArr.toString());
                    }
                }
            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        ref.addValueEventListener(listener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        ref.removeEventListener(listener);
//    }
//
//    private ValueEventListener listener= new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            if (dataSnapshot!=null && dataSnapshot.getValue()!=null)
//            {
//                String ticker = dataSnapshot.child("ticker").getValue(String.class);
//                tvTicker.setText(ticker);
//                Float price = dataSnapshot.child("price").getValue(Float.class);
//                tvPrice.setText(String.format(Locale.getDefault(), "%.2f", price));
//
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//            // handle any errors
//            Log.e(LOG_TAG, "Database error", databaseError.toException());
//
//        }
//    };
}
