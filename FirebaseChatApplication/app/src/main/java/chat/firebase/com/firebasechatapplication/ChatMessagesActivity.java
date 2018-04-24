package chat.firebase.com.firebasechatapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chat.firebase.com.firebasechatapplication.model.ChatMessage;
import chat.firebase.com.firebasechatapplication.model.FileModel;
import chat.firebase.com.firebasechatapplication.model.User;
import chat.firebase.com.firebasechatapplication.util.MessagesAdapter;


public class ChatMessagesActivity extends AppCompatActivity {


    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String TAG = "ChatMessageActivity";
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private RecyclerView mChatsRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private EditText mMessageEditText;
    private ImageButton mSendImageButton,sendPhotoImagebutton;
    private DatabaseReference mMessagesDBRefSender,mMessagesDBRefReciver;
    private DatabaseReference mUsersRef;
    private List<ChatMessage> mMessagesList = new ArrayList<>();
    private MessagesAdapter adapter = null;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    //File
    private File filePathImageCamera;

    private StorageReference mStorageRefSender,mStorageRefReceiver;

    private String mReceiverId;
    private String mReceiverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);


        //initialize the views
        mChatsRecyclerView = (RecyclerView)findViewById(R.id.messagesRecyclerView);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendImageButton = (ImageButton)findViewById(R.id.sendMessageImagebutton);
        sendPhotoImagebutton = (ImageButton)findViewById(R.id.sendPhotoImagebutton);
        mChatsRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        mChatsRecyclerView.setLayoutManager(mLayoutManager);

        //get receiverId from intent
        mReceiverId = getIntent().getStringExtra("USER_ID");

        //init Firebase
        mMessagesDBRefSender = FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getUid()+"_"+mReceiverId);
        mMessagesDBRefReciver = FirebaseDatabase.getInstance().getReference().child("Messages").child(mReceiverId+"_"+FirebaseAuth.getInstance().getUid());
        mUsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //init Firebase storage
        mStorageRefSender = FirebaseStorage.getInstance().getReference().child("Photos").child("Users");
        mStorageRefReceiver = FirebaseStorage.getInstance().getReference().child("Photos").child("Users");


        /**listen to send message imagebutton click**/
        mSendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessageEditText.getText().toString();
                String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();


                if(message.isEmpty()){
                    Toast.makeText(ChatMessagesActivity.this, "You must enter a message", Toast.LENGTH_SHORT).show();
                }else {
                    //message is entered, send
                    sendMessageToFirebase(message, senderId, mReceiverId);
                }
            }
        });

        sendPhotoImagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               verifyStoragePermissions();

            }
        });



    }

    private void selectImage() {
        Log.d(TAG,"clicked");
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatMessagesActivity.this);
        builder.setTitle("Send photo");
        builder.setMessage("Choose a method to change photo");
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                photoGalleryIntent();
            }
        });
        builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                photoCameraIntent();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**Query and populate chat messages**/
        querymessagesBetweenThisUserAndClickedUser();


        /**sets title bar with recepient name**/
        queryRecipientName(mReceiverId);
    }



    private void sendMessageToFirebase(String message, String senderId, String receiverId){
        mMessagesList.clear();

        ChatMessage newMsg = new ChatMessage(message, senderId, receiverId,null);
//        mMessagesDBRef.child(senderId+"_"+receiverId).push().setValue(newMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
        mMessagesDBRefSender.push().setValue(newMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    //error
                    Toast.makeText(ChatMessagesActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ChatMessagesActivity.this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
                    mMessageEditText.setText("");
                    hideSoftKeyboard();
                }
            }
        });
      mMessagesDBRefReciver.push().setValue(newMsg).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void querymessagesBetweenThisUserAndClickedUser(){
        Log.e("receiver_id=","="+mReceiverId);
        Log.e("sender_id=","="+FirebaseAuth.getInstance().getUid());

        mMessagesDBRefSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMessagesList.clear();
                if(dataSnapshot!=null && dataSnapshot.getValue()!=null) {
                    Log.e("MesagesAvailable", "=" + dataSnapshot.getValue().toString());

                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        ChatMessage chatMessage = snap.getValue(ChatMessage.class);
                        mMessagesList.add(chatMessage);
                    }

                    /**populate messages**/
                    populateMessagesRecyclerView();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateMessagesRecyclerView(){
        adapter = new MessagesAdapter(mMessagesList, this);
        mChatsRecyclerView.setAdapter(adapter);

    }

    private void queryRecipientName(final String receiverId){

        mUsersRef.child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User recepient = dataSnapshot.getValue(User.class);
                mReceiverName = recepient.getDisplayName();

                try {
                    getSupportActionBar().setTitle(mReceiverName);
                    getActionBar().setTitle(mReceiverName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        StorageReference storageRef = storage.getReferenceFromUrl("gs://fir-project1-c7620.appspot.com/Photos").child("images");

        if (requestCode == IMAGE_GALLERY_REQUEST){
            if (resultCode == RESULT_OK){
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null){
                    sendFileFirebase(storageRef,selectedImageUri);
                }else{
                    //URI IS NULL
                }
            }
        }else if (requestCode == IMAGE_CAMERA_REQUEST){
            if (resultCode == RESULT_OK){
                if (filePathImageCamera != null && filePathImageCamera.exists()){
                    StorageReference imageCameraRef = storageRef.child(filePathImageCamera.getName()+"_camera");
                    sendFileFirebase(imageCameraRef, Uri.fromFile(filePathImageCamera));
                }else{
                    //IS NULL
                }
            }
        }

    }

    private void sendFileFirebase(StorageReference storageReference, final Uri file){
        if (storageReference != null){
            final String name = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
            StorageReference imageGalleryRef = storageReference.child(name+"_gallery");
            UploadTask uploadTask = imageGalleryRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,"onFailure sendFileFirebase "+e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG,"onSuccess sendFileFirebase");
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    FileModel fileModel = new FileModel("img",downloadUrl.toString(),name,"");
                    ChatMessage chatModel = new ChatMessage("",FirebaseAuth.getInstance().getUid(),mReceiverId,fileModel);
                    mMessagesDBRefSender.push().setValue(chatModel);
                    mMessagesDBRefReciver.push().setValue(chatModel);
                }
            });
        }else{
            //IS NULL
        }

    }


    private void photoCameraIntent(){
        String nomeFoto = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
        filePathImageCamera = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), nomeFoto+"camera.jpg");
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(ChatMessagesActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                filePathImageCamera);
        it.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
        startActivityForResult(it, IMAGE_CAMERA_REQUEST);
    }

    /**
     * Enviar foto pela galeria
     */
    private void photoGalleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_GALLERY_REQUEST);
    }
    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     */
    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(ChatMessagesActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    ChatMessagesActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else{
            // we already have permission, lets go ahead and call camera intent
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    selectImage();
                }
                break;
        }
    }
}



