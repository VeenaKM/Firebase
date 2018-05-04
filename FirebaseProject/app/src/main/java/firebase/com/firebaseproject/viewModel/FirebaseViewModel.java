package firebase.com.firebaseproject.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.firebase.client.annotations.NotNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import firebase.com.firebaseproject.dataModel.User;

public class FirebaseViewModel extends ViewModel {

    private static final DatabaseReference DB_REF =
            FirebaseDatabase.getInstance().getReference().child("users");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(DB_REF);

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }

    public void writeNewUser(String name, String email) {
        User user = new User(name, email);
        DB_REF.push().setValue(user);
    }


}
